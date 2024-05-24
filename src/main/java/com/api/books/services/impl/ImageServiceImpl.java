package com.api.books.services.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.books.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    private static final String STATIC = "static/";

    @Override
    public Resource uploadCover(String fileName, Long userId) throws FileNotFoundException, MalformedURLException {
        Path rootFile = getPath(STATIC + userId + "/" + fileName);
        if(Files.exists(rootFile) && Files.isReadable(rootFile)) {
            return new UrlResource(rootFile.toUri());
        } else {
            rootFile = getPath("error.png");
            return new UrlResource(rootFile.toUri());
        }
    }

    public ResponseEntity<byte[]> uploadBlob(String fileName, Long userId) throws IOException {
        Path path = getPath(STATIC + userId + "/" + fileName);
        byte[] bytes = Files.readAllBytes(path);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }

    @Override
    public Resource uploadProfile(String fileName, Long userId) throws FileNotFoundException, MalformedURLException {
        Path rootFile = getPath(STATIC + userId + "/" + fileName);
        if(Files.exists(rootFile) && Files.isReadable(rootFile)) {
            return new UrlResource(rootFile.toUri());
        } else {
            rootFile = getPath("profile.png");
            return new UrlResource(rootFile.toUri());
        }
    }

    @Override
    public boolean removeImage(String filename) {
        if (filename == null || filename.length() == 0)
            return false;
        Path path = getPath(STATIC + filename);
        if (Files.exists(path) && Files.isReadable(path)) {
            try {
                Files.delete(path);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public String saveImage(MultipartFile file, Long userId) throws IOException {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null)
                return "";
            String randomizedName = UUID.randomUUID().toString() + "_" + fileName.replace(" ", "");
            Path filePath = getPath(STATIC + userId + "/" + randomizedName);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return randomizedName;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    private Path getPath(String fileName) {
        return Paths.get("src/main/resources/").resolve(fileName).toAbsolutePath();
    }
}