package com.lumiera.shop.lumierashop.service;

import com.lumiera.shop.lumierashop.global.exception.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.lumiera.shop.lumierashop.global.exception.code.ErrorCode.FILE_NOT_FOUND;
import static com.lumiera.shop.lumierashop.global.exception.code.ErrorCode.FILE_UPLOAD_FAILED;

@Service
public class FileUploadService {

    @Value("${file.dir}")
    private Path fileDir;

    public String saveImage(MultipartFile image) {
        validateImage(image);

        try {
            Files.createDirectories(getBaseDir());

            String storedFileName = createStoredFileName(image.getOriginalFilename());
            Path targetPath = getBaseDir().resolve(storedFileName).normalize();

            image.transferTo(targetPath.toFile());

            return "/images/" + storedFileName;
        } catch (IOException e) {
            throw new CustomException(FILE_UPLOAD_FAILED);
        }
    }

    public List<String> saveImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile image : images) {
            if (image == null || image.isEmpty()) {
                continue;
            }

            imageUrls.add(saveImage(image));
        }

        return imageUrls;
    }

    private void validateImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new CustomException(FILE_NOT_FOUND);
        }
    }

    private Path getBaseDir() {
        return fileDir.toAbsolutePath().normalize();
    }

    private String createStoredFileName(String originalFilename) {
        String extension = extractExtension(originalFilename);
        return UUID.randomUUID() + "." + extension;
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new CustomException(FILE_UPLOAD_FAILED);
        }

        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}