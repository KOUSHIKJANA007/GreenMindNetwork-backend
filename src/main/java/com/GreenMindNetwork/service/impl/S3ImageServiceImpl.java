package com.GreenMindNetwork.service.impl;

import com.GreenMindNetwork.exception.ImageUploadException;
import com.GreenMindNetwork.service.S3ImageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
@Service
public class S3ImageServiceImpl implements S3ImageService {
    @Autowired
    private AmazonS3 client;
    @Value("${app.s3.bucket}")
    private String buckectName;
    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        String actualFileName=file.getOriginalFilename();
        String filename= UUID.randomUUID().toString()+actualFileName.substring(actualFileName.lastIndexOf("."));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
       try {
           PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(buckectName,filename,file.getInputStream(),objectMetadata));
           return filename;
       }catch (IOException e){
           throw new ImageUploadException("image upload failed");
       }
    }

    @Override
    public List<String> allFiles() {
        return null;
    }

    @Override
    public String preSignedUrl() {
        return null;
    }

    @Override
    public InputStream getImage(String image) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(buckectName, image);
            S3Object object = client.getObject(getObjectRequest);
            InputStream objectContent = object.getObjectContent();
            return objectContent;
    }

    @Override
    public void deleteImage(String image) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(buckectName, image);
        client.deleteObject(deleteObjectRequest);
    }
}
