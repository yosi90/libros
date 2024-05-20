package com.api.books.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.books.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    private static final String STATIC = "static/";

    private final ResourceLoader resourceLoader;

    public ImageServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Resource uploadCover(String fileName, Long userId) throws FileNotFoundException, MalformedURLException {
        Resource resource = resourceLoader.getResource("classpath:" + STATIC + userId + "/" + fileName);
        if(resource.exists() && resource.isReadable())
            return resource;
        else
            return new ClassPathResource("error.png");
    }

    @Override
    public Resource uploadProfile(String fileName, Long userId) throws FileNotFoundException, MalformedURLException {
        Resource resource = resourceLoader.getResource("classpath:" + STATIC + userId + "/" + fileName);
        if(resource.exists() && resource.isReadable())
            return resource;
        else
            return new ClassPathResource("profile.png");
    }

    @Override
    public boolean removeImage(String filename) throws IOException {
        if (filename == null || filename.length() == 0)
            return false;
        Path path = getImageFilePath(filename);
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
            Path filePath = getImageFilePath(userId + "/" + randomizedName);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return randomizedName;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public Path getImageFilePath(String imageName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static");
        File file = resource.getFile();
        Path absolutePath = file.toPath().toAbsolutePath();
        return Paths.get(absolutePath.toString(), imageName);
    } 
}
