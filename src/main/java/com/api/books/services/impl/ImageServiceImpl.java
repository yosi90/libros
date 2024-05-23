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
import org.springframework.beans.factory.annotation.Value;

import com.api.books.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${azure.appservice.static-path}")
    private String staticPath;

    private static final String STATIC = "static/";

    private final ResourceLoader resourceLoader;

    public ImageServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Resource uploadCover(String fileName, Long userId) {
        String filePath = staticPath + "/" + userId + "/" + fileName;
        return getResource(filePath, "error.png");
    }

    // @Override
    // public Resource uploadCover(String fileName, Long userId) throws
    // FileNotFoundException, MalformedURLException {
    // Resource resource = resourceLoader.getResource("classpath:" + STATIC + userId
    // + "/" + fileName);
    // if (resource.exists() && resource.isReadable())
    // return resource;
    // else
    // return new ClassPathResource("error.png");
    // }

    @Override
    public Resource uploadProfile(String fileName, Long userId) {
        String filePath = staticPath + "/" + userId + "/" + fileName;
        return getResource(filePath, "profile.png");
    }

    // @Override
    // public Resource uploadProfile(String fileName, Long userId) throws
    // FileNotFoundException, MalformedURLException {
    // Resource resource = resourceLoader.getResource("classpath:" + STATIC + userId
    // + "/" + fileName);
    // if (resource.exists() && resource.isReadable())
    // return resource;
    // else
    // return new ClassPathResource("profile.png");
    // }

    @Override
    public boolean removeImage(String filename) throws IOException {
        if (filename == null || filename.isEmpty())
            return false;

        Path path = Paths.get(staticPath, filename);
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

    // @Override
    // public boolean removeImage(String filename) throws IOException {
    // if (filename == null || filename.length() == 0)
    // return false;
    // Path path = getImageFilePath(filename);
    // if (Files.exists(path) && Files.isReadable(path)) {
    // try {
    // Files.delete(path);
    // return true;
    // } catch (IOException e) {
    // return false;
    // }
    // }
    // return false;
    // }

    @Override
    public String saveImage(MultipartFile file, Long userId) throws IOException {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null)
                return "";
            String randomizedName = UUID.randomUUID().toString() + "_" + fileName.replace(" ", "");
            String directoryPath = staticPath + "/" + userId;
            Path filePath = Paths.get(directoryPath, randomizedName);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath);
            return randomizedName;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    // @Override
    // public String saveImage(MultipartFile file, Long userId) throws IOException {
    //     try {
    //         String fileName = file.getOriginalFilename();
    //         if (fileName == null)
    //             return "";
    //         String randomizedName = UUID.randomUUID().toString() + "_" + fileName.replace(" ", "");
    //         Path filePath = getImageFilePath(userId + "/" + randomizedName);
    //         Files.createDirectories(filePath.getParent());
    //         Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    //         return randomizedName;
    //     } catch (IOException e) {
    //         throw new IOException(e);
    //     }
    // }

    private Resource getResource(String filePath, String defaultFileName) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path) && Files.isReadable(path))
                return new ClassPathResource(filePath);
            else
                return new ClassPathResource(defaultFileName);
        } catch (Exception e) {
            return new ClassPathResource(defaultFileName);
        }
    }

    // public Path getImageFilePath(String imageName) throws IOException {
    //     Resource resource = resourceLoader.getResource("classpath:static");
    //     File file = resource.getFile();
    //     Path absolutePath = file.toPath().toAbsolutePath();
    //     return Paths.get(absolutePath.toString(), imageName);
    // }
}