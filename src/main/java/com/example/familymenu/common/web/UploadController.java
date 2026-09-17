package com.example.familymenu.common.web;

import com.example.familymenu.common.api.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {
    private static final int MAX_IMAGE_SIZE = 640;
    private static final float JPEG_QUALITY = 0.82f;

    @PostMapping("/image")
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("请选择图片");
        if (file.getSize() > 10L * 1024 * 1024) throw new IllegalArgumentException("图片不能超过10MB");
        String type = file.getContentType() == null ? "" : file.getContentType();
        if (!type.startsWith("image/")) throw new IllegalArgumentException("只支持图片文件");
        String ext = "image/png".equals(type) ? ".png" : ".jpg";
        Path dir = Paths.get("uploads"); Files.createDirectories(dir);
        String name = UUID.randomUUID().toString().replace("-", "") + ext;
        BufferedImage source = ImageIO.read(file.getInputStream());
        if (source == null) throw new IllegalArgumentException("图片格式无法识别");
        BufferedImage resized = resize(source, MAX_IMAGE_SIZE, ".png".equals(ext));
        Path target = dir.resolve(name);
        if (".png".equals(ext)) ImageIO.write(resized, "png", target.toFile());
        else writeJpeg(resized, target);
        return ApiResponse.success("/uploads/" + name);
    }

    private BufferedImage resize(BufferedImage source, int maxSize, boolean keepAlpha) {
        double scale = Math.min(1.0, (double) maxSize / Math.max(source.getWidth(), source.getHeight()));
        int width = Math.max(1, (int) Math.round(source.getWidth() * scale));
        int height = Math.max(1, (int) Math.round(source.getHeight() * scale));
        int imageType = keepAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage resized = new BufferedImage(width, height, imageType);
        Graphics2D graphics = resized.createGraphics();
        try {
            if (!keepAlpha) {
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, width, height);
            }
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.drawImage(source, 0, 0, width, height, null);
        } finally {
            graphics.dispose();
        }
        return resized;
    }

    private void writeJpeg(BufferedImage image, Path target) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) throw new IOException("JPEG writer is unavailable");
        ImageWriter writer = writers.next();
        try (ImageOutputStream output = ImageIO.createImageOutputStream(target.toFile())) {
            writer.setOutput(output);
            ImageWriteParam parameters = writer.getDefaultWriteParam();
            parameters.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            parameters.setCompressionQuality(JPEG_QUALITY);
            writer.write(null, new IIOImage(image, null, null), parameters);
        } finally {
            writer.dispose();
        }
    }
}
