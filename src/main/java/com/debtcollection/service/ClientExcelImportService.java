package com.debtcollection.service;

import com.debtcollection.dto.client.ClientCreateDto;
import com.debtcollection.dto.client.ClientDto;
import com.debtcollection.dto.clientNote.ClientNoteCreateDto;
import com.debtcollection.entity.Client;
import com.debtcollection.entity.EntityType;
import com.debtcollection.mapper.ClientMapper;
import com.debtcollection.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientExcelImportService {

    private final ClientService clientService;
    private static final DataFormatter formatter = new DataFormatter();
    private final ClientNoteService clientNoteService;
    private final ClientMapper clientMapper;

    public void importFromExcel(MultipartFile file) {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = findSuitableSheet(workbook);
            if (sheet == null) throw new IllegalArgumentException("No suitable sheet found for clients import");

            DataFormatter fmt = new DataFormatter();
            Map<String, Integer> cols = mapHeaderColumns(sheet, fmt);

            if (!cols.containsKey("clientName")) {
                throw new IllegalArgumentException("Missing required header: clientName");
            }

            Integer clientIdCol = cols.get("clientId"); // optional if your DTO supports it
            Integer noteCol = cols.get("noteContent"); // optional per-row note
            Integer noteCreatedByCol = cols.get("noteCreatedBy"); // optional per-row note author (numeric)

            Long authUserId = resolveAuthenticatedUserId().orElse(null);

            int firstDataRow = sheet.getFirstRowNum() + 1;
            for (int r = firstDataRow; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                String clientName = s(fmt, row, cols.get("clientName"));
                if (isBlank(clientName)) continue;

                Long clientIdFromSheet = clientIdCol != null ? lng(fmt, row, clientIdCol) : null;

                ClientCreateDto dto = new ClientCreateDto();
               // if (clientIdFromSheet != null) dto.setId(clientIdFromSheet); // optional, depends on DTO
                dto.setName(clientName);

                // set other fields if present in sheet (example: totalDebt) - omitted for brevity

                // persist client
                //Long createdClientId = clientService.createClient(dto); // assume returns created id; adapt if returns DTO

                Client created = clientService.createClient(dto,authUserId);
                Long createdClientId = (created != null) ? created.getId() : null;

                if (createdClientId != null) {
                    String noteContent = noteCol != null ? s(fmt, row, noteCol) : null;
                    Long sheetNoteCreatedBy = noteCreatedByCol != null ? lng(fmt, row, noteCreatedByCol) : null;
                    if (!isBlank(noteContent)) {
                        ClientNoteCreateDto note = new ClientNoteCreateDto();
                        note.setClientId(createdClientId);
                        note.setContent(noteContent);
                        note.setCreatedBy(sheetNoteCreatedBy != null ? sheetNoteCreatedBy : authUserId);
                        try { clientNoteService.create(note); } catch (Exception ignored) {}
                    } else if (authUserId != null) {
                        ClientNoteCreateDto note = new ClientNoteCreateDto();
                        note.setClientId(createdClientId);
                        note.setContent("Client '" + clientName + "' created via Excel import");
                        note.setCreatedBy(authUserId);
                        try { clientNoteService.create(note); } catch (Exception ignored) {}
                    }
                }
                // create note(s)
                String noteContent = noteCol != null ? s(fmt, row, noteCol) : null;
                if (!isBlank(noteContent)) {
                    ClientNoteCreateDto note = new ClientNoteCreateDto();
                    note.setClientId(createdClientId);
                    note.setContent(noteContent);
                    Long sheetNoteCreatedBy = noteCreatedByCol != null ? lng(fmt, row, noteCreatedByCol) : null;
                    note.setCreatedBy(sheetNoteCreatedBy != null ? sheetNoteCreatedBy : authUserId);
                    try { clientNoteService.create(note); } catch (Exception ignored) {}
                } else {
                    // generic import note
                    if (authUserId != null) {
                        ClientNoteCreateDto note = new ClientNoteCreateDto();
                        note.setClientId(createdClientId);
                        note.setContent("Client '" + clientName + "' created via Excel import");
                        note.setCreatedBy(authUserId);
                        try { clientNoteService.create(note); } catch (Exception ignored) {}
                    }
                }
            }
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            throw new RuntimeException("Failed to import clients from Excel", e);
        }
    }

    private Sheet findSuitableSheet(Workbook wb) {
        Sheet named = wb.getSheet("clients");
        if (named != null) return named;
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet s = wb.getSheetAt(i);
            if (s != null && s.getPhysicalNumberOfRows() > 0) return s;
        }
        return null;
    }
    private Map<String, Integer> mapHeaderColumns(Sheet sheet, DataFormatter fmt) {
        Row header = sheet.getRow(sheet.getFirstRowNum());
        if (header == null) throw new IllegalArgumentException("Header row not found");
        Map<String, Integer> map = new HashMap<>();
        IntStream.range(0, header.getLastCellNum()).forEach(idx -> {
            Cell c = header.getCell(idx);
            if (c == null) return;
            String name = fmt.formatCellValue(c).trim();
            if (!name.isEmpty()) map.put(name, idx);
        });
        return map;
    }

    private Optional<Long> resolveAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Optional.empty();

        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails user) {
            return Optional.ofNullable(user.getId());
        }
        if (principal instanceof Jwt jwt) {
            Object claim = jwt.getClaim("userId");
            if (claim == null) claim = jwt.getClaim("user_id");
            if (claim == null) claim = jwt.getClaim("sub");
            if (claim instanceof Number n) return Optional.of(n.longValue());
            if (claim instanceof String s && !s.isBlank()) {
                try { return Optional.of(Long.parseLong(s)); } catch (NumberFormatException ignored) {}
            }
        }
        try {
            String name = auth.getName();
            if (name != null && name.matches("\\d+")) return Optional.of(Long.parseLong(name));
        } catch (Exception ignored) {}
        return Optional.empty();
    }

    // helpers
    private String s(DataFormatter fmt, Row row, Integer idx) {
        if (row == null || idx == null) return null;
        Cell c = row.getCell(idx);
        return c == null ? null : fmt.formatCellValue(c).trim();
    }

    private Long lng(DataFormatter fmt, Row row, Integer idx) {
        String val = s(fmt, row, idx);
        if (val == null || val.isBlank()) return null;
        try { return new BigDecimal(val.replaceAll("[^0-9.-]", "")).longValue(); }
        catch (Exception e) { return null; }
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private ClientDto mapRowToClientDto(Row row) {
        ClientDto dto = new ClientDto();

        dto.setName(getString(row, 0));
        dto.setEntityType(EntityType.valueOf(getString(row, 1)));
        dto.setIdentificationNumber(getString(row, 2));
        dto.setPhone(getString(row, 3));
        dto.setEmail(getString(row, 4));
        dto.setAddress(getString(row, 5));
        dto.setVatNumber(getString(row, 6));
        dto.setPaymentModel(getString(row, 7));
        dto.setPaymentTerms(getString(row, 8));

        return dto;
    }

    private String getString(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            return null;
        }
        return formatter.formatCellValue(cell).trim();
    }


    private Long getSystemUserId() {
        return 1L; // או משתמש מחובר / SYSTEM
    }
}

