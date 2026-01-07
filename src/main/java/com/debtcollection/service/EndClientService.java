package com.debtcollection.service;

import com.debtcollection.dto.contact.ClientContactDto;
import com.debtcollection.dto.contactDetail.ContactDetailDto;
import com.debtcollection.dto.endClient.EndClientCreateDto;
import com.debtcollection.dto.endClient.EndClientDto;
import com.debtcollection.dto.endClient.EndClientUpdateDto;
import com.debtcollection.dto.person.PersonDto;
import com.debtcollection.entity.*;
import com.debtcollection.mapper.EndClientMapper;
import com.debtcollection.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor

public class EndClientService {

    private final EndClientRepository endClientRepository;
    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final EndClientMapper mapper;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final ContactDetailRepository contactDetailRepository;





    // CREATE
    public EndClientDto create(EndClientCreateDto dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() ->
                        new RuntimeException("Client not found with id " + dto.getClientId())
                );

        EndClient endClient = new EndClient();
        endClient.setClient(client);
        endClient.setName(dto.getName());
        endClient.setTotalDebt(dto.getTotalDebt());

        EndClient savedEndClient = endClientRepository.save(endClient);
        String firstEmail = null;
        if (dto.getPersons() != null) {
            for (PersonDto personDto : dto.getPersons()) {
                Person person = new Person();
                person.setEndClient(savedEndClient);
                person.setFirstName(personDto.getFirstName());
                person.setLastName(personDto.getLastName());
                Person savedPerson = personRepository.save(person);
                if (personDto.getContacts() != null) {
                    for (ContactDetailDto c : personDto.getContacts()) {
                        if (c.getValue() != null && !c.getValue().trim().isEmpty()) {
                            ContactDetail cd = new ContactDetail();
                            cd.setPerson(savedPerson); // רק person, לא client/endClient כדי לא להפר את ה-CK
                            cd.setType(c.getType());
                            cd.setValue(c.getValue());
                            contactDetailRepository.save(cd);

                            if (firstEmail == null
                                    && c.getType() == ContactType.EMAIL
                                    && c.getValue() != null
                                    && !c.getValue().trim().isEmpty()) {
                                firstEmail = c.getValue().trim();
                            }
                        }
                    }
                }
            }
        }
        if (firstEmail != null) {
            String username = firstEmail; // או firstEmail + savedEndClient.getId()
            String rawPassword = generateRandomPassword();
            String passwordHash = passwordEncoder.encode(rawPassword);

            User user = new User();
            user.setUserName(username);
            user.setPassword(passwordHash);
            user.setEmail(firstEmail);
            user.setEndClient(savedEndClient);
            user.setRole(Role.END_CLIENT);
            user.setEnabled(true);
            userRepository.save(user);

            try {
                sendCredentialsMail(firstEmail, username, rawPassword);
            } catch (Exception e) {
                System.err.println("Failed to send credentials email to end client: " + e.getMessage());
            }
        } else {
            System.out.println("WARNING: No email found for end client contacts; skipping user creation");
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

        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() ->
                            new RuntimeException("Client not found with id " + dto.getClientId())
                    );
            entity.setClient(client);
        }

        return mapper.toResponseDto(entity);
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<EndClientDto> getAll() {
        return endClientRepository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
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
}
