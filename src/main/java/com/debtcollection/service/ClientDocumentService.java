package com.debtcollection.service;

import com.debtcollection.dto.clientDocument.ClientDocumentCreateDto;
import com.debtcollection.dto.clientDocument.ClientDocumentDto;
import com.debtcollection.dto.clientDocument.ClientDocumentUpdateDto;
import com.debtcollection.dto.clientDocument.FileResource;
import com.debtcollection.entity.ClientDocument;
import com.debtcollection.mapper.ClientDocumentMapper;
import com.debtcollection.repository.ClientDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientDocumentService {

    private final ClientDocumentRepository repository;
    private final ClientDocumentMapper mapper;
    @Value("${upload.root:uploads/client-documents}")
    private String uploadRoot;

    public List<ClientDocumentDto> listAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public List<ClientDocumentDto> listByClientId(Long clientId) {
        return repository.findByClientId(clientId).stream().map(mapper::toDto).toList();
    }

    public ClientDocumentDto getById(Long id) {
        return mapper.toDto(getEntity(id));
    }

    public ClientDocumentDto create(ClientDocumentCreateDto dto) {
        ClientDocument e = new ClientDocument();
        e.setClientId(dto.getClientId());
        e.setFileName(Optional.ofNullable(dto.getFileName()).orElse("unnamed"));
        e.setContentType(dto.getMimeType());
        e.setSize(null);
        e.setFilePath("JSON_ONLY"); // סימון: לא נשמר קובץ אמיתי
        e = repository.save(e);
        return mapper.toDto(e);
    }

    public ClientDocumentDto update(Long id, ClientDocumentUpdateDto dto) {
        ClientDocument e = getEntity(id);
        if (dto.getFileName() != null) e.setFileName(dto.getFileName());
        e = repository.save(e);
        return mapper.toDto(e);
    }

    public void delete(Long id) {
        ClientDocument e = getEntity(id);
        if (e.getFilePath() != null) {
            try { Files.deleteIfExists(Path.of(e.getFilePath())); } catch (Exception ignored) {}
        }
        repository.deleteById(id);
    }

    public ClientDocumentDto upload(Long clientId, MultipartFile file) {
        Path dest = storeFile(clientId, file); // מחזיר absolute path
        Path root = Path.of(uploadRoot).toAbsolutePath().normalize();
        String relative = root.relativize(dest).toString().replace('\\','/');

        ClientDocument e = new ClientDocument();
        e.setFilePath(relative); // שמור RELATIVE
        e.setClientId(clientId);
        e.setFileName(file.getOriginalFilename());
        e.setContentType(file.getContentType());
        e.setSize(file.getSize());
        e.setUploadedAt(Instant.now());

        e = repository.save(e);
        return mapper.toDto(e);
    }
    public ClientDocumentDto replaceFile(Long id, MultipartFile file) {
        ClientDocument e = getEntity(id);
        // מחיקת הישן (אם קיים)
        if (e.getFilePath() != null) {
            try {
                Path root = Path.of(uploadRoot).toAbsolutePath().normalize();
                Path old = root.resolve(e.getFilePath()).normalize();
                Files.deleteIfExists(old);
            } catch (Exception ignored) {}
        }
        Path dest = storeFile(e.getClientId(), file);
        Path root = Path.of(uploadRoot).toAbsolutePath().normalize();
        String relative = root.relativize(dest).toString().replace('\\','/');

        e.setFilePath(relative);
        e.setFileName(file.getOriginalFilename());
        e.setContentType(file.getContentType());
        e.setSize(file.getSize());
        e.setUploadedAt(Instant.now());
        e = repository.save(e);
        return mapper.toDto(e);
    }

    public FileResource getFileResource(Long id) {
        ClientDocument e = getEntity(id);
        if (e.getFilePath() == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not stored");

        Path root = Path.of(uploadRoot).toAbsolutePath().normalize();
        Path p = root.resolve(e.getFilePath()).normalize();

        // בטיחות: ודא שהקובץ בתוך תיקיית ה-root
        if (!p.startsWith(root)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid file path");
        }

        if (!Files.exists(p)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
        try {
            Resource res = new UrlResource(p.toUri());
            String mime = Optional.ofNullable(e.getContentType())
                    .orElseGet(() -> guessContentType(p, e.getFileName()));
            if (mime == null) mime = "application/octet-stream";
            return new FileResource(res, e.getFileName(), mime);
        } catch (MalformedURLException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read file");
        }
    }
    private Path storeFile(Long clientId, MultipartFile file) {
        try {
            Path root = Path.of(uploadRoot).toAbsolutePath().normalize();
            Path dir = root.resolve(String.valueOf(clientId));
            Files.createDirectories(dir);

            String safeName = UUID.randomUUID() + "-" + sanitize(file.getOriginalFilename());
            Path dest = dir.resolve(safeName);

            try (InputStream in = file.getInputStream()) {
                Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
            }

            return dest; // absolute
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file");
        }
    }
    private String sanitize(String name) {
        if (name == null) return "file";
        return name.replaceAll("[\\r\\n]", "_").trim();
    }

    private ClientDocument getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }
    private String guessContentType(Path p, String originalName) {
        String ext = Optional.ofNullable(originalName)
                .map(n -> n.contains(".") ? n.substring(n.lastIndexOf('.') + 1).toLowerCase() : "")
                .orElse("");
        return switch (ext) {
            case "pdf" -> "application/pdf";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls" -> "application/vnd.ms-excel";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default -> {
                try { yield Files.probeContentType(p); } catch (IOException e) { yield null; }
            }
        };
    }

}
