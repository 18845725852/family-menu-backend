package com.example.familymenu.files;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "files")
public class FileStorageProperties {
    private String storageDir;

    public String getStorageDir() {
        return storageDir;
    }

    public void setStorageDir(String storageDir) {
        this.storageDir = storageDir;
    }

    public Path root() {
        String dir = storageDir == null ? "" : storageDir.trim();
        if (dir.isEmpty()) {
            dir = System.getProperty("user.home") + "/.family-menu/files";
        }
        return Paths.get(dir).toAbsolutePath().normalize();
    }

    public Path legacyDir() {
        return root().resolve("legacy");
    }
}
