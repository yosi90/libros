package com.api.books.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.books.services.ImageService;

@RestController
@RequestMapping("api/v1/image")
public class ImageControllers {
    
    private final ImageService imageService;

    public ImageControllers(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/cover/{userId}/{imagePath}")
    public ResponseEntity<Resource> getCoverImage(@PathVariable Long userId, @PathVariable String imagePath) throws MalformedURLException, FileNotFoundException {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(imageService.uploadCover(imagePath, userId));
    }

    @GetMapping("/blob/{userId}/{imagePath:.+}")
    public ResponseEntity<byte[]> getCoverBlob(@PathVariable Long userId, @PathVariable String imagePath) throws IOException {
        return imageService.uploadBlob(imagePath, userId);
    }

    @GetMapping("/profile/{userId}/{imagePath}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable Long userId, @PathVariable String imagePath) throws MalformedURLException, FileNotFoundException {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(imageService.uploadProfile(imagePath, userId));
    }
}
