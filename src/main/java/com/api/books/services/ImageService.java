package com.api.books.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    
    Resource upload(String fileName, Long userId) throws FileNotFoundException, MalformedURLException;

    String saveImage(MultipartFile file, Long userId) throws IOException;
}
