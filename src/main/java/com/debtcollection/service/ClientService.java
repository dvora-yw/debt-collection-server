package com.debtcollection.service;

import ch.qos.logback.core.CoreConstants;
import com.debtcollection.dto.client.ClientCreateDto;
import com.debtcollection.dto.client.ClientDto;
import com.debtcollection.dto.client.ClientUpdateDto;
import com.debtcollection.dto.contact.ClientContactDto;
import com.debtcollection.entity.Client;
import com.debtcollection.entity.ClientContacts;
import com.debtcollection.entity.Role;
import com.debtcollection.entity.User;
import com.debtcollection.mapper.ClientContactMapper;
import com.debtcollection.mapper.ClientMapper;
import com.debtcollection.repository.ClientContactRepository;
import com.debtcollection.repository.ClientRepository;
import com.debtcollection.repository.UserRepository;
import com.debtcollection.security.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientContactRepository clientContactRepository;
    private final ClientMapper clientMapper;
    private final ClientContactMapper clientContactMapper;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    // CREATE
    @Transactional
    public Client createClient(ClientCreateDto dto,Long currentUserId) {

        Client client = new Client();
        client.setName(dto.getName());
        client.setEntityType(dto.getEntityType());
        client.setIdentificationNumber(dto.getIdentificationNumber());
        client.setAddress(dto.getAddress());
        client.setPhone(dto.getPhone());
        client.setEmail(dto.getEmail());
        client.setFax(dto.getFax());
        client.setNotes(dto.getNotes());
        client.setEstablishedDate(dto.getEstablishedDate());
        client.setVatNumber(dto.getVatNumber());
        client.setPaymentModel(dto.getPaymentModel());
        client.setPaymentTerms(dto.getPaymentTerms());
        client = clientRepository.save(client);

        // 2️⃣ יצירת USER
        if (dto.getContacts() != null){
            for (ClientContactDto u : dto.getContacts()) {
                String username = generateUsername(client);
                String rawPassword = generateRandomPassword();
                String passwordHash = passwordEncoder.encode(rawPassword);
                User user = new User();
                user.setUserName(u.getFirstName()+" "+u.getLastName());
                user.setPassword(passwordHash);
                user.setEmail(u.getEmail());
                user.setPhone(u.getPhone());
                user.setClient(client);
                user.setRole(Role.CLIENT_USER);
                user.setEnabled(true);
                user.setCreatedBy(currentUserId);

                user = userRepository.save(user);
                // שליחת מייל - אם נכשל, רק נדפיס לוג אבל לא נפיל את התהליך
                try {
                    if(user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                        sendCredentialsMail(user.getEmail(), user.getUserName(), rawPassword);
                    } else {
                        System.out.println("WARNING: Client has no valid email, skipping credentials mail");
                    }
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to send credentials email: " + e.getMessage());
                    // לא זורקים exception - אפשר להמשיך
                }
            }
        }





        if (dto.getContacts() != null) {
            for (ClientContactDto c : dto.getContacts()) {
                ClientContacts contact = new ClientContacts();
                contact.setFirstName(c.getFirstName());
                contact.setLastName(c.getLastName());
                contact.setRole(c.getRole());
                contact.setPhone(c.getPhone());
                contact.setEmail(c.getEmail());
                contact.setClient(client); // מחברים ללקוח עם ID תקין
//                client.getContacts().add(contact);
                contact = clientContactRepository.save(contact);
            }
        }
        client = clientRepository.save(client);


        return client;
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

    // READ - all
    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // READ - by id
    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Client not found with id " + id
                ));

        return clientMapper.toResponseDto(client);
    }
    // UPDATE
    public ClientDto updateClient(Long id, ClientUpdateDto dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Client not found with id " + id
                ));

        // clientMapper.updateEntityFromDto(dto, client);


        Client updated = clientRepository.save(client);
        return clientMapper.toResponseDto(updated);
    }

    // DELETE
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found with id " + id);
        }
        clientRepository.deleteById(id);
    }
}
