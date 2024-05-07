package com.GreenMindNetwork.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface S3ImageService {
    String uploadImage(MultipartFile file) throws IOException;
    List<String> allFiles();
    String preSignedUrl();
    InputStream getImage(String image);
    void deleteImage(String image);
}
