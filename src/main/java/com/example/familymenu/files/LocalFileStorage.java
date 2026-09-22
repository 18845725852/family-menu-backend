package com.example.familymenu.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LocalFileStorage {
    private static final Logger log = LoggerFactory.getLogger(LocalFileStorage.class);
    private static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("yyyyMM");
    private final Path root;

    public LocalFileStorage(FileStorageProperties properties) throws IOException {
        this.root = properties.root();
        Files.createDirectories(root);
        Files.createDirectories(properties.legacyDir());
        log.info("file storage directory: {}", root);
    }

    public Path root() {
        return root;
    }

    public String store(byte[] content, String extension) throws IOException {
        String month = YearMonth.now().format(MONTH);
        String name = UUID.randomUUID().toString().replace("-", "") + extension;
        Path dir = root.resolve(month);
        Files.createDirectories(dir);
        Files.write(dir.resolve(name), content);
        return "/api/files/" + month + "/" + name;
    }

    public Path find(String month, String name) {
        if (month == null || name == null) return null;
        if (!month.matches("[0-9]{6}") || !name.matches("[a-f0-9]{32}\\.(jpg|png)")) return null;
        Path file = root.resolve(month).resolve(name).normalize();
        if (!file.startsWith(root) || !Files.isRegularFile(file)) return null;
        return file;
    }
}
