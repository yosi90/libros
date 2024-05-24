package com.api.books.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    
    Resource uploadCover(String fileName, Long userId) throws FileNotFoundException, MalformedURLException;
    
    ResponseEntity<byte[]> uploadBlob(String fileName, Long userId) throws IOException;

    Resource uploadProfile(String fileName, Long userId) throws FileNotFoundException, MalformedURLException;

    boolean removeImage(String filename) throws IOException;

    String saveImage(MultipartFile file, Long userId) throws IOException;
}
