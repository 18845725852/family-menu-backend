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

    public String store(byte[] content, String extension, String category) throws IOException {
        String folder = normalizeCategory(category);
        String month = YearMonth.now().format(MONTH);
        String name = UUID.randomUUID().toString().replace("-", "") + extension;
        Path dir = root.resolve(folder).resolve(month);
        Files.createDirectories(dir);
        Files.write(dir.resolve(name), content);
        return "/api/files/" + folder + "/" + month + "/" + name;
    }

    public Path find(String month, String name) {
        return find(null, month, name);
    }

    public Path find(String category, String month, String name) {
        if (!isMonth(month) || !isName(name)) return null;
        Path base = root;
        if (category != null) {
            if (!isCategory(category)) return null;
            base = root.resolve(category).normalize();
        }
        Path file = base.resolve(month).resolve(name).normalize();
        if (!file.startsWith(base) || !file.startsWith(root) || !Files.isRegularFile(file)) return null;
        return file;
    }

    private String normalizeCategory(String category) {
        String value = category == null ? "" : category.trim();
        if (value.isEmpty()) value = "dishes";
        if (!isCategory(value)) throw new IllegalArgumentException("不支持的图片分类");
        return value;
    }

    private boolean isCategory(String category) {
        return "dishes".equals(category) || "avatars".equals(category);
    }

    private boolean isMonth(String month) {
        return month != null && month.matches("[0-9]{6}");
    }

    private boolean isName(String name) {
        return name != null && name.matches("[a-f0-9]{32}\\.(jpg|png)");
    }
}
