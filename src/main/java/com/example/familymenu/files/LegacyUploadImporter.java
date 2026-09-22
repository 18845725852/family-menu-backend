package com.example.familymenu.files;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class LegacyUploadImporter implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(LegacyUploadImporter.class);
    private final FileStorageProperties properties;

    public LegacyUploadImporter(FileStorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        Path source = Paths.get("uploads").toAbsolutePath().normalize();
        Path target = properties.legacyDir();
        Files.createDirectories(target);
        if (!Files.isDirectory(source)) {
            log.info("file storage directory: {}", properties.root());
            return;
        }
        int copied = 0;
        try (DirectoryStream<Path> files = Files.newDirectoryStream(source)) {
            for (Path file : files) {
                if (!Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS)) continue;
                String name = file.getFileName().toString();
                if (name.startsWith(".")) continue;
                Path dest = target.resolve(name).normalize();
                if (!dest.startsWith(target)) continue;
                if (Files.exists(dest) && Files.size(dest) == Files.size(file)) continue;
                Files.copy(file, dest, StandardCopyOption.REPLACE_EXISTING);
                copied++;
            }
        }
        log.info("file storage directory: {}, legacy images copied: {}", properties.root(), copied);
    }
}
