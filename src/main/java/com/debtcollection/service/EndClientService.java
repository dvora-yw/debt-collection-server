package com.debtcollection.service;

import com.debtcollection.dto.accountBalance.AccountBalanceCreateDto;
import com.debtcollection.dto.accountBalance.AccountBalanceDto;
import com.debtcollection.dto.contact.ClientContactDto;
import com.debtcollection.dto.contactDetail.ContactDetailDto;
import com.debtcollection.dto.endClient.EndClientCreateDto;
import com.debtcollection.dto.endClient.EndClientDto;
import com.debtcollection.dto.endClient.EndClientFinancialSummaryDto;
import com.debtcollection.dto.endClient.EndClientUpdateDto;
import com.debtcollection.dto.payment.PaymentDto;
import com.debtcollection.dto.paymentCharge.PaymentChargeCreateDto;
import com.debtcollection.dto.paymentCharge.PaymentChargeDto;
import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsCreateDto;
import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsDto;
import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.*;
import com.debtcollection.mapper.EndClientMapper;
import com.debtcollection.mapper.PaymentMapper;
import com.debtcollection.repository.*;
import com.debtcollection.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class EndClientService {

    private final EndClientRepository endClientRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final EndClientMapper mapper;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final ContactDetailRepository contactDetailRepository;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final DebtNotificationService debtNotificationService;
    private final PaymentChargeService paymentChargeService;
    private final PaymentMapper paymentMapper;
    private final RecurringPaymentDetailsService recurringPaymentService;
    private final AccountBalanceService accountBalanceService;

    // CREATE
    public EndClientDto create(EndClientCreateDto dto) {


        // --- שליפת המשתמש הנוכחי ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in DB"));

        // --- הכנת רשימת Clients ---
        Client client = new Client();

        if (dto.getClientId() == null) {

            if (currentUser.getRole() == Role.ADMIN) {
                throw new RuntimeException("Admin must provide clientIds when creating EndClient");
            } else {
                // משתמש רגיל - מוסיפים רק את הלקוח שלו
                if (currentUser.getClient() == null) {
                    throw new RuntimeException("Current user is not linked to any client");
                }
                Long clientId = currentUser.getClient().getId();

                Client cl = clientRepository.findById(clientId)
                        .orElseThrow(() -> new RuntimeException("Client not found with id " + clientId));
                client = cl;
            }

        } else {
            // אם נשלחו clientIds ב-DTO
            client = clientRepository.findById(dto.getClientId())
                            .orElseThrow(() -> new RuntimeException("Client not found with id " + dto.getClientId()))
                 ;
        }
        Set<Client> clients=new HashSet<>();
        clients.add(client);
        // --- יצירת EndClient חדש ---
        EndClient endClient = new EndClient();
        endClient.setClients(clients);
        endClient.setName(dto.getName());
        endClient.setTotalDebt(dto.getTotalDebt());

        EndClient savedEndClient = endClientRepository.save(endClient);
        // --- יצירת Users ---
        if (dto.getUsers() != null) {
            for (UserDto userDto : dto.getUsers()) {

                if (userDto.getEmail() == null || userDto.getEmail().isBlank()) continue;

                boolean exists = userRepository.existsByEmailAndEndClientId(
                        userDto.getEmail().trim(), savedEndClient.getId());
                if (exists) continue;

                String rawPassword = generateRandomPassword();

                User user = new User();
                user.setUserName(userDto.getUserName());
                user.setEmail(userDto.getEmail().trim());
                user.setPhone(userDto.getPhone().trim());
                user.setIdentificationNumber(userDto.getIdentificationNumber());
                user.setPassword(passwordEncoder.encode(rawPassword));
                user.setEndClient(savedEndClient);
                user.setRole(Role.END_CLIENT_USER);
                user.setEnabled(true);

                userRepository.save(user);

                try {
                    sendCredentialsMail(user.getEmail(), user.getUserName(), rawPassword);
                } catch (Exception e) {
                    System.err.println("Failed to send credentials email to " + user.getEmail());
                }
            }
        } else {
            System.out.println("WARNING: No email found for end client contacts; skipping user creation");
        }

// 1) יתרת זכות התחלתית
        if (dto.getInitialPrepaidBalance() != null &&
                dto.getInitialPrepaidBalance().compareTo(BigDecimal.ZERO) > 0) {
            AccountBalanceCreateDto balanceDto = new AccountBalanceCreateDto(
                    savedEndClient.getId(),
                    dto.getInitialPrepaidBalance(),
                    "ILS",
                    LocalDate.now(),
                    LocalDate.now()
            );
            accountBalanceService.create(balanceDto);
        }

// 2) חיוב ראשוני + חיוב מחזורי (אם צריך)
        if (dto.getInitialChargeAmount() != null) {
            Long recurringId = null;
            if ("RECURRING".equalsIgnoreCase(dto.getInitialChargeType())) {
                RecurringPaymentDetailsCreateDto rpd = new RecurringPaymentDetailsCreateDto(
                        savedEndClient.getId(),
                        Optional.ofNullable(dto.getStartDate()).orElse(dto.getInitialChargeDueDate()),
                        dto.getIntervalValue(),
                        dto.getIntervalUnit(),
                        dto.getRecurringEndDate(),
                        null
                );
                RecurringPaymentDetailsDto savedRpd = recurringPaymentService.create(rpd);
                recurringId = savedRpd.getId();
            }

            PaymentChargeCreateDto chargeCreate = new PaymentChargeCreateDto();
            chargeCreate.setEndClientId(savedEndClient.getId());
            chargeCreate.setAmount(dto.getInitialChargeAmount());
            chargeCreate.setDueDate(dto.getInitialChargeDueDate());
            chargeCreate.setStatus("OPEN");
            chargeCreate.setType(dto.getInitialChargeType());
            chargeCreate.setRecurringPaymentId(recurringId);

          //  paymentChargeService.create(chargeCreate);
            // כאן מפעילים את מנגנון ההודעות על החוב החדש
            PaymentChargeDto savedCharge = paymentChargeService.create(chargeCreate);

            debtNotificationService.onNewDebtCreated(
                    client,                       // הלקוח (מי שחייבים בשמו)
                    savedEndClient,               // הלקוח קצה
                    savedCharge.getId(),          // מזהה החוב/חיוב
                    savedCharge.getAmount(),      // סכום החוב
                    savedCharge.getDueDate() != null
                            ? savedCharge.getDueDate().atStartOfDay()
                            : java.time.LocalDateTime.now()
            );
        }




        return mapper.toResponseDto(savedEndClient);
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    private String generateUsername(Client client) {
        return client.getEmail(); // או email + id אם צריך
    }

    public void sendCredentialsMail(String to, String username, String password) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("פרטי התחברות למערכת גביה");
        message.setText(
                "שלום,\n\n" +
                        "נוצר עבורך משתמש למערכת:\n" +
                        "שם משתמש: " + username + "\n" +
                        "סיסמה: " + password + "\n\n" +
                        "מומלץ להחליף סיסמה לאחר התחברות ראשונה."
        );
        mailSender.send(message);
    }

    // UPDATE
    public EndClientDto update(Long id, EndClientUpdateDto dto) {
        EndClient entity = endClientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("EndClient not found with id " + id)
                );

        mapper.updateEntityFromDto(dto, entity);
        // עדכון קשרים ל-Clients חדשים אם נשלחו
        if (dto.getClientIds() != null) {
            Set<Client> clients = dto.getClientIds().stream()
                    .map(cid -> clientRepository.findById(cid)
                            .orElseThrow(() -> new RuntimeException("Client not found with id " + cid)))
                    .collect(Collectors.toSet());

            entity.setClients(clients);
        }

        return mapper.toResponseDto(entity);
    }

    // מחזיר EndClient לפי שם ולקוח
    @Transactional(readOnly = true)
    public EndClient findByNameAndClient(String name, Long clientId) {
        return endClientRepository.findByNameAndClientId(name, clientId).orElse(null);
    }

    // בודק אם User כבר קיים עבור EndClient ו-email
    @Transactional(readOnly = true)
    public boolean userExistsForClientAndEmail(Long clientId, String email) {
        return userRepository.existsByEmailAndClientId(email, clientId);
    }


    // GET ALL
    @Transactional(readOnly = true)
    public List<EndClientDto> getAll() {
        return endClientRepository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }


    // GET BY ID
    @Transactional(readOnly = true)
    public EndClientDto getById(Long id) {
        return endClientRepository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() ->
                        new RuntimeException("EndClient not found with id " + id)
                );
    }
    @Transactional(readOnly = true)
    public List<EndClientDto> getAllById(Long clientId) {
        return endClientRepository.findByClientId(clientId)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }
    // DELETE
    public void delete(Long id) {
        endClientRepository.deleteById(id);
    }
    public EndClientFinancialSummaryDto getFinancialSummary(Long endClientId) {

        AccountBalanceDto balance = accountBalanceService.findByEndClientId(endClientId).orElse(null);

        List<RecurringPaymentDetailsDto> recurring =
                Optional.ofNullable(recurringPaymentService.findByEndClientId(endClientId))
                        .orElse(Collections.emptyList());

        // כל החיובים הפתוחים עבור הלקוח קצה (כולל חד פעמיים)
        List<PaymentChargeDto> openCharges =
                paymentChargeService.findByEndClientIdAndStatus(endClientId, "OPEN");
        // או אם אין לך שיטה כזו – findByEndClientId() ואז לסנן ל־status OPEN בצד הסרוויס

        List<PaymentDto> recentPayments = Collections.emptyList();

        return new EndClientFinancialSummaryDto(
                balance,
                recurring,
                openCharges,
                recentPayments
        );
    }}
