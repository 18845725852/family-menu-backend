package com.example.familymenu.common.web;

import com.example.familymenu.common.api.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {
    @PostMapping("/image")
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("请选择图片");
        if (file.getSize() > 10L * 1024 * 1024) throw new IllegalArgumentException("图片不能超过10MB");
        String type = file.getContentType() == null ? "" : file.getContentType();
        if (!type.startsWith("image/")) throw new IllegalArgumentException("只支持图片文件");
        String ext = "image/png".equals(type) ? ".png" : ".jpg";
        Path dir = Paths.get("uploads"); Files.createDirectories(dir);
        String name = UUID.randomUUID().toString().replace("-", "") + ext;
        Files.copy(file.getInputStream(), dir.resolve(name), StandardCopyOption.REPLACE_EXISTING);
        return ApiResponse.success("/uploads/" + name);
    }
}
