package com.GreenMindNetwork.config;

import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.service.S3ImageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class S3Controller {
    @Autowired
    private S3ImageService s3ImageService;
    @PostMapping("/s3/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = s3ImageService.uploadImage(file);
        return ResponseEntity.ok(uploadImage);
    }

    @GetMapping("/s3/upload/{image}")
    public void getImage(@PathVariable String image,HttpServletResponse response) throws IOException{
        InputStream resource = this.s3ImageService.getImage(image);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
    @DeleteMapping("/s3/upload/{image}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable String image){
        this.s3ImageService.deleteImage(image);
        return new ResponseEntity<>(new ApiResponse("Image delete successfully",true), HttpStatus.OK);
    }
}
