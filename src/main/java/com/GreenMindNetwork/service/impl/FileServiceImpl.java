package com.GreenMindNetwork.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.GreenMindNetwork.exception.ImageUploadException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.GreenMindNetwork.service.FileService;



@Service
public class FileServiceImpl implements FileService{

	@Autowired
	private AmazonS3 client;
	@Override
	public  String uploadImage(String path, MultipartFile file) throws IOException {
		String name=file.getOriginalFilename();
		
		
		String randomID=UUID.randomUUID().toString();
		String filename1 = randomID.concat(name.substring(name.lastIndexOf(".")));
		
		File f=new File(path);
		if(!f.exists()) {
			f.mkdir();
		}
		String filepath=path + File.separator + filename1;
		Files.copy(file.getInputStream(), Paths.get(filepath));
		return filename1;



//		ObjectMetadata objectMetadata = new ObjectMetadata();
//		objectMetadata.setContentLength(file.getSize());
//		try {
//			PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(path,filename1,file.getInputStream(),objectMetadata));
//			return filename1;
//		}catch (IOException e){
//			throw new ImageUploadException("image upload failed");
//		}
	}
	@Override
	public void deleteImage(String path, String filename) throws IOException {
		String filePath=path+File.separator+filename;
		Files.delete(Paths.get(filePath));
//		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(path, filename);
//		client.deleteObject(deleteObjectRequest);
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		String fullPath = path + File.separator + fileName;
        return new FileInputStream(fullPath);


//		GetObjectRequest getObjectRequest = new GetObjectRequest(path, fileName);
//		S3Object object = client.getObject(getObjectRequest);
//		return object.getObjectContent();
	}

}
