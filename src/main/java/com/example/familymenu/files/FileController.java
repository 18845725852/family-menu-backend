package com.example.familymenu.files;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {
    private final LocalFileStorage storage;

    public FileController(LocalFileStorage storage) {
        this.storage = storage;
    }

    @GetMapping("/api/files/{month:[0-9]{6}}/{name:.+}")
    public ResponseEntity<Resource> download(@PathVariable String month, @PathVariable String name) {
        return respond(storage.find(month, name), name);
    }

    @GetMapping("/api/files/{category:dishes|avatars}/{month:[0-9]{6}}/{name:.+}")
    public ResponseEntity<Resource> downloadCategorized(@PathVariable String category, @PathVariable String month,
                                                        @PathVariable String name) {
        return respond(storage.find(category, month, name), name);
    }

    private ResponseEntity<Resource> respond(Path file, String name) {
        if (file == null) return ResponseEntity.notFound().build();
        MediaType mediaType = name.endsWith(".png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic())
                .contentType(mediaType)
                .body(new FileSystemResource(file));
    }
}
