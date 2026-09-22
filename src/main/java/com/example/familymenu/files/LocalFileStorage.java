package com.example.familymenu.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LocalFileStorage {
    private static final Logger log = LoggerFactory.getLogger(LocalFileStorage.class);
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

    public String store(byte[] content, String extension, String category, Long familyId) throws IOException {
        String scope = category == null ? "" : category.trim();
        if (scope.isEmpty() || "dishes".equals(scope) || "default".equals(scope)) {
            return write(content, extension, new String[]{"dishes", "default"});
        }
        if ("avatars".equals(scope)) return write(content, extension, new String[]{"avatars"});
        if ("family".equals(scope)) {
            if (familyId == null || familyId.longValue() <= 0) throw new IllegalArgumentException("请选择家庭");
            return write(content, extension, new String[]{"dishes", "families", String.valueOf(familyId)});
        }
        throw new IllegalArgumentException("不支持的图片分类");
    }

    public Path findAvatar(String name) { return findUnder(name, new String[]{"avatars"}); }
    public Path findDefaultDish(String name) { return findUnder(name, new String[]{"dishes", "default"}); }
    public Path findFamilyDish(String familyId, String name) {
        if (familyId == null || !familyId.matches("[0-9]+")) return null;
        return findUnder(name, new String[]{"dishes", "families", familyId});
    }

    private String write(byte[] content, String extension, String[] folders) throws IOException {
        String name = UUID.randomUUID().toString().replace("-", "") + extension;
        Path dir = root;
        StringBuilder url = new StringBuilder("/api/files");
        for (String folder : folders) {
            if (!folder.matches("[A-Za-z0-9_-]+")) throw new IllegalArgumentException("不支持的图片分类");
            dir = dir.resolve(folder);
            url.append('/').append(folder);
        }
        Files.createDirectories(dir);
        Files.write(dir.resolve(name), content);
        url.append('/').append(name);
        return url.toString();
    }

    private Path findUnder(String name, String[] folders) {
        if (!isName(name)) return null;
        Path file = root;
        for (String folder : folders) {
            if (!folder.matches("[A-Za-z0-9_-]+")) return null;
            file = file.resolve(folder);
        }
        file = file.resolve(name).normalize();
        if (!file.startsWith(root) || !Files.isRegularFile(file)) return null;
        return file;
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
