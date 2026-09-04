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
        String type = file.getContentType() == null ? "" : file.getContentType();
        if (!type.startsWith("image/")) throw new IllegalArgumentException("只支持图片文件");
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String ext = original.lastIndexOf('.') >= 0 ? original.substring(original.lastIndexOf('.')) : ".jpg";
        Path dir = Paths.get("uploads"); Files.createDirectories(dir);
        String name = UUID.randomUUID().toString().replace("-", "") + ext;
        Files.copy(file.getInputStream(), dir.resolve(name), StandardCopyOption.REPLACE_EXISTING);
        return ApiResponse.success("/uploads/" + name);
    }
}
