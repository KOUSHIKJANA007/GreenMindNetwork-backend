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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.GreenMindNetwork.service.FileService;



@Service
public class FileServiceImpl implements FileService{


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
	}
	@Override
	public void deleteImage(String path, String filename) throws IOException {
		String filePath=path+File.separator+filename;
		Files.delete(Paths.get(filePath));
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		String fullPath = path + File.separator + fileName;
        return new FileInputStream(fullPath);
	}

}
