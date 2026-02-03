package com.debtcollection.service;

import com.debtcollection.dto.clientNote.ClientNoteCreateDto;
import com.debtcollection.dto.contactDetail.ContactDetailDto;
import com.debtcollection.dto.endClient.EndClientCreateDto;
import com.debtcollection.dto.endClient.EndClientDto;
import com.debtcollection.dto.endClient.EndClientUpdateDto;
import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.ContactType;
import com.debtcollection.entity.EndClient;
import com.debtcollection.mapper.EndClientMapper;
import com.debtcollection.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class EndClientExcelImportService {

    private final EndClientService endClientService;
    private final ClientNoteService clientNoteService;
    private final EndClientMapper endClientMapper;

    public void importFromExcel(MultipartFile file) {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = findSuitableSheet(workbook);
            if (sheet == null) {
                throw new RuntimeException("No suitable sheet found for end clients import");
            }

            DataFormatter fmt = new DataFormatter();
            Map<String, Integer> cols = mapHeaderColumns(sheet, fmt);

            List<String> required = List.of(
                    "endClientName", "totalDebt", "UserFirstName",
                    "UserLastName", "contactType", "contactValue"
            );
            for (String h : required) {
                if (!cols.containsKey(h)) {
                    throw new RuntimeException("Missing required header: " + h);
                }
            }

            Long authClientId = resolveClientIdFromAuth().orElse(null);
            boolean isAdmin = checkIfAdmin();
            Long authUserId = resolveAuthenticatedUserId().orElse(null);

            if (authClientId == null && !isAdmin) {
                throw new RuntimeException("Authenticated user has no clientId");
            }

            Map<String, EndClientCreateDto> buffer = new LinkedHashMap<>();
            int firstDataRow = sheet.getFirstRowNum() + 1;

            for (int r = firstDataRow; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                String endClientName = s(fmt, row, cols.get("endClientName"));
                if (isBlank(endClientName)) continue;

                BigDecimal totalDebt = bd(fmt, row, cols.get("totalDebt"));
                Long clientId = authClientId; // כרגע לא מחליפים, ניתן להוסיף אפשרות ל-sheet עם clientId

                String key = clientId + "|" + endClientName;
                EndClientCreateDto dto = buffer.computeIfAbsent(key, k -> {
                    EndClientCreateDto d = new EndClientCreateDto();
//                    d.setClients();
                    d.setName(endClientName);
                    d.setTotalDebt(totalDebt);
//                    d.setUsers(new ArrayList<>());
                    return d;
                });

                if (dto.getTotalDebt() == null && totalDebt != null) {
                    dto.setTotalDebt(totalDebt);
                }

                // --- עיבוד האנשים והקשרים ---
                String firstName = s(fmt, row, cols.get("UserFirstName"));
                String lastName = s(fmt, row, cols.get("UserLastName"));
                if (isBlank(firstName) && isBlank(lastName)) continue;

                String contactTypeRaw = s(fmt, row, cols.get("contactType"));
                String contactValue = s(fmt, row, cols.get("contactValue"));

//                UserDto User = findOrCreateUser(dto, firstName, lastName);
                if (!isBlank(contactTypeRaw) && !isBlank(contactValue)) {
                    ContactDetailDto contact = new ContactDetailDto();
                    contact.setType(parseContactType(contactTypeRaw));
                    contact.setValue(contactValue);
//                    if (User.getContacts() == null) User.setContacts(new ArrayList<>());
//                    User.getContacts().add(contact);
                }
            }

            // --- שמירה בסוף ---
            for (EndClientCreateDto dto : buffer.values()) {
                // בדיקה אם EndClient כבר קיים עבור אותו client
//                EndClient existing = endClientService.findByNameAndClient(dto.getName(), dto.getClientIds());
//                EndClientDto savedEndClientDto;
//                if (existing != null) {
//                    EndClientUpdateDto updateDto = toUpdateDto(dto);
//
//                    savedEndClientDto = endClientService.update(existing.getId(), updateDto);
//                } else {
//                    savedEndClientDto = endClientService.create(dto);
//                }

                // יצירת הערת יצירה
                if (authUserId != null) {
                    try {
                        ClientNoteCreateDto note = new ClientNoteCreateDto();
//                        note.setClientId(dto.getClientId());
                        note.setContent("End client '" + dto.getName() + "' created via Excel import");
                        note.setCreatedBy(authUserId);
                        clientNoteService.create(note);
                    } catch (Exception ignored) {}
                }

                // יצירת User לכל EndClient (רק אם לא קיים)
//                for (UserDto UserDto : dto.getUsers()) {
//                    if (UserDto.getContacts() == null) continue;
//                    for (ContactDetailDto contactDto : UserDto.getContacts()) {
//                        if (contactDto.getType() == ContactType.EMAIL && !isBlank(contactDto.getValue())) {
//                            String email = contactDto.getValue().trim();
//                            boolean userExists = endClientService.userExistsForClientAndEmail(dto.getClientId(), email);
//                            if (!userExists) {
//                                endClientService.createUserForEndClient(savedEndClientDto, email);
//                            }
//                        }
//                    }
//                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to import end clients from Excel", e);
        }
    }
    private EndClientUpdateDto toUpdateDto(EndClientCreateDto create) {
        EndClientUpdateDto u = new EndClientUpdateDto();
        u.setName(create.getName());
        u.setTotalDebt(create.getTotalDebt());
        // copy/transform Users if types match; adapt if UpdateDto expects different structure
        u.setUsers(create.getUsers());
        // set other fields present in your Update DTO as needed
        return u;}
    private Sheet findSuitableSheet(Workbook wb) {
        Sheet named = wb.getSheet("end_clients");
        if (named != null) return named;

        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet s = wb.getSheetAt(i);
            if (s != null && hasExpectedHeaders(s)) return s;
        }
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet s = wb.getSheetAt(i);
            if (s != null && s.getPhysicalNumberOfRows() > 0) return s;
        }
        return null;
    }

    private boolean hasExpectedHeaders(Sheet s) {
        DataFormatter fmt = new DataFormatter();
        Row header = s.getRow(s.getFirstRowNum());
        if (header == null) return false;
        Set<String> headers = new HashSet<>();
        for (Cell c : header) {
            headers.add(fmt.formatCellValue(c).trim());
        }
        return headers.containsAll(Set.of(
                "endClientName", "totalDebt", "UserFirstName", "UserLastName", "contactType", "contactValue"
        ));
    }

    private Map<String, Integer> mapHeaderColumns(Sheet sheet, DataFormatter fmt) {
        Row header = sheet.getRow(sheet.getFirstRowNum());
        if (header == null) throw new RuntimeException("Header row not found");
        Map<String, Integer> map = new HashMap<>();
        IntStream.range(0, header.getLastCellNum()).forEach(idx -> {
            Cell c = header.getCell(idx);
            if (c == null) return;
            String name = fmt.formatCellValue(c).trim();
            if (!name.isEmpty()) map.put(name, idx);
        });
        return map;
    }

    private Optional<Long> resolveClientIdFromAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Optional.empty();
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails user) return Optional.of(user.getClientId());
        if (principal instanceof Jwt jwt) {
            Object claim = jwt.getClaim("clientId");
            if (claim == null) claim = jwt.getClaim("client_id");
            if (claim instanceof Number n) return Optional.of(n.longValue());
            if (claim instanceof String s && !s.isBlank()) {
                try { return Optional.of(Long.parseLong(s)); } catch (NumberFormatException ignored) {}
            }
        }
        return Optional.empty();
    }

    private Optional<Long> resolveAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Optional.empty();
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails user) return Optional.ofNullable(user.getId());
        if (principal instanceof Jwt jwt) {
            Object claim = jwt.getClaim("userId");
            if (claim == null) claim = jwt.getClaim("user_id");
            if (claim == null) claim = jwt.getClaim("sub");
            if (claim instanceof Number n) return Optional.of(n.longValue());
            if (claim instanceof String s && !s.isBlank()) {
                try { return Optional.of(Long.parseLong(s)); } catch (NumberFormatException ignored) {}
            }
        }
        return Optional.empty();
    }

//    private UserDto findOrCreateUser(EndClientCreateDto dto, String firstName, String lastName) {
//        if (dto.getUsers() == null) dto.setUsers(new ArrayList<>());
//        return dto.getUsers().stream()
//                .filter(p -> Objects.equals(p.getFirstName(), firstName) && Objects.equals(p.getLastName(), lastName))
//                .findFirst()
//                .orElseGet(() -> {
//                    UserDto p = new UserDto();
//                    p.setFirstName(firstName);
//                    p.setLastName(lastName);
//                    p.setContacts(new ArrayList<>());
//                    dto.getUsers().add(p);
//                    return p;
//                });
//    }

    private ContactType parseContactType(String raw) {
        String norm = raw.trim().toUpperCase().replace(' ', '_');
        return ContactType.valueOf(norm);
    }

    private String s(DataFormatter fmt, Row row, Integer idx) {
        if (row == null || idx == null) return null;
        String val = fmt.formatCellValue(row.getCell(idx));
        return val == null ? null : val.trim();
    }

    private BigDecimal bd(DataFormatter fmt, Row row, Integer idx) {
        String val = s(fmt, row, idx);
        if (val == null || val.isBlank()) return null;
        try {
            String normalized = val.replace("₪", "").replace(",", "").trim();
            return new BigDecimal(normalized);
        } catch (Exception e) { return null; }
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private boolean checkIfAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}
