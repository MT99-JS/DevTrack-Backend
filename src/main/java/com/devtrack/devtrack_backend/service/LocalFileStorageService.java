package com.devtrack.devtrack_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path uploadDirectory =
            Paths.get("uploads");

    @Override
    public String store(MultipartFile file) throws IOException {

        Files.createDirectories(uploadDirectory);

        String originalFileName = file.getOriginalFilename();

        String fileName =
                UUID.randomUUID() + "_" + originalFileName;

        Path targetPath =
                uploadDirectory.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                targetPath
        );

        return "/uploads/" + fileName;
    }

    @Override
    public void delete(String fileUrl) throws IOException {

        String fileName = fileUrl.substring(
                fileUrl.lastIndexOf("/") + 1
        );

        Path filePath = uploadDirectory.resolve(fileName);

        Files.deleteIfExists(filePath);
    }
}
