package com.GreenMindNetwork.controller;

import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.NgoSocialImageDto;
import com.GreenMindNetwork.service.FileService;
import com.GreenMindNetwork.service.NgoSocialImageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/socialImage")
public class NgoSocialImageController {
    @Autowired
    private NgoSocialImageService ngoSocialImageService;
    @Autowired
    private FileService fileService;
    @Value("${project.image.social}")
    private  String path;

    @PostMapping("/{ngoId}")
    public ResponseEntity<NgoSocialImageDto> postNgoSocialImage(@RequestBody NgoSocialImageDto ngoSocialImageDto,@PathVariable Integer ngoId){

        NgoSocialImageDto ngoSocialImageDto1 = this.ngoSocialImageService.postNgoSocialImage(ngoSocialImageDto, ngoId);
        return new ResponseEntity<>(ngoSocialImageDto1, HttpStatus.CREATED);
    }

    @PutMapping("/{socialId}")
    public ResponseEntity<NgoSocialImageDto> updateNgoSocialImage(@RequestBody NgoSocialImageDto ngoSocialImageDto,@PathVariable Integer socialId){

        NgoSocialImageDto ngoSocialImageDto1 = this.ngoSocialImageService.updateNgoSocialImage(ngoSocialImageDto, socialId);
        return new ResponseEntity<>(ngoSocialImageDto1, HttpStatus.OK);
    }
    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<NgoSocialImageDto>> getSocialPostByNgo(@PathVariable Integer ngoId){
        List<NgoSocialImageDto> postByNgo = this.ngoSocialImageService.getPostByNgo(ngoId);
        return ResponseEntity.ok(postByNgo);
    }
    @DeleteMapping("/{socialId}")
    public ResponseEntity<ApiResponse> deleteNgoSocialImage(@PathVariable Integer socialId){
        this.ngoSocialImageService.deleteNgoSocialImage(socialId);
        ApiResponse apiResponse=new ApiResponse("Post deleted ",true);
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("/image/{socialId}")
    public ResponseEntity<NgoSocialImageDto> postImage(@RequestParam("image")MultipartFile image, @PathVariable Integer socialId) throws IOException {
        NgoSocialImageDto ngoSocialImageById = this.ngoSocialImageService.getNgoSocialImageById(socialId);
        String uploadImage = this.fileService.uploadImage(path, image);
        ngoSocialImageById.setImage(uploadImage);
        NgoSocialImageDto ngoSocialImageDto = this.ngoSocialImageService.updateNgoSocialImage(ngoSocialImageById, socialId);
        return ResponseEntity.ok(ngoSocialImageDto);
    }
    @GetMapping(value = "/image/{imagename}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void ShowImage(@PathVariable String imagename, HttpServletResponse response) throws IOException{
        InputStream image = this.fileService.getResource(path, imagename);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(image, response.getOutputStream());

    }
}
