package com.GreenMindNetwork.controller;

import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.NgoDto;
import com.GreenMindNetwork.service.FileService;
import com.GreenMindNetwork.service.NgoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/api/ngo")
public class NgoController {

    @Autowired
    private NgoService ngoService;
    @Value("${project.image.ngo}")
    private String path;
    @Autowired
    private FileService fileService;
    @PostMapping("/register/{userId}")
    public ResponseEntity<NgoDto> createNgo(@Valid @RequestBody NgoDto ngoDto, @PathVariable("userId") Integer userId){
        NgoDto ngo = this.ngoService.createNgo(ngoDto,userId);
        return new ResponseEntity<>(ngo, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{ngoId}")
    public ResponseEntity<NgoDto> updateNgo(@Valid @RequestBody NgoDto ngoDto,@PathVariable Integer ngoId){
        NgoDto ngo = this.ngoService.updateNgo(ngoDto, ngoId);
        return ResponseEntity.ok(ngo);
    }
    @DeleteMapping("/delete/{ngoId}")
    public ResponseEntity<ApiResponse> deleteNgo(@PathVariable Integer ngoId){
        this.ngoService.deleteNgo(ngoId);
        ApiResponse apiResponse=new ApiResponse("NGO deleted successfully",true);
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/")
    public ResponseEntity<List<NgoDto>> getAllNgos(){
        List<NgoDto> allNgo = this.ngoService.getAllNgo();
        return ResponseEntity.ok(allNgo);
    }
    @GetMapping("/{ngoId}")
    public ResponseEntity<NgoDto> getNgoById(@PathVariable Integer ngoId){
        NgoDto ngoById = this.ngoService.getNgoById(ngoId);
        return ResponseEntity.ok(ngoById);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<NgoDto> getNgoByUser(@PathVariable Integer userId){
        NgoDto ngoByUser = this.ngoService.getNgoByUser(userId);
        return ResponseEntity.ok(ngoByUser);
    }
    @PostMapping("/image/uploadIdentity/{ngoId}")
    public ResponseEntity<NgoDto> uploadIdentityImage(@RequestParam("image")MultipartFile image,@PathVariable Integer ngoId) throws IOException {
        NgoDto ngoById = this.ngoService.getNgoById(ngoId);
        String uploadImage = this.fileService.uploadImage(path, image);
        ngoById.setIdentityOfHead(uploadImage);
        NgoDto ngoDto = this.ngoService.updateNgo(ngoById, ngoId);
        return ResponseEntity.ok(ngoDto);
    }
    @PostMapping("/image/TaxProof/{ngoId}")
    public ResponseEntity<NgoDto> uploadTaxProofImage(@RequestParam("image")MultipartFile image,@PathVariable Integer ngoId) throws IOException {
        NgoDto ngoById = this.ngoService.getNgoById(ngoId);
        String uploadImage = this.fileService.uploadImage(path, image);
        ngoById.setImageOfTax(uploadImage);
        NgoDto ngoDto = this.ngoService.updateNgo(ngoById, ngoId);
        return ResponseEntity.ok(ngoDto);
    }
    @PostMapping("/image/RegistrationProof/{ngoId}")
    public ResponseEntity<NgoDto> uploadRegistrationProofImage(@RequestParam("image")MultipartFile image,@PathVariable Integer ngoId) throws IOException {
        NgoDto ngoById = this.ngoService.getNgoById(ngoId);
        String uploadImage = this.fileService.uploadImage(path, image);
        ngoById.setRegisterImage(uploadImage);
        NgoDto ngoDto = this.ngoService.updateNgo(ngoById, ngoId);
        return ResponseEntity.ok(ngoDto);
    }
    @PostMapping("/image/ngoLogo/{ngoId}")
    public ResponseEntity<NgoDto> uploadNgoLogoImage(@RequestParam("image")MultipartFile image,@PathVariable Integer ngoId) throws IOException {
        NgoDto ngoById = this.ngoService.getNgoById(ngoId);
        String uploadImage = this.fileService.uploadImage(path, image);
        ngoById.setLogo(uploadImage);
        NgoDto ngoDto = this.ngoService.updateNgo(ngoById, ngoId);
        return ResponseEntity.ok(ngoDto);
    }
    @GetMapping(value = "/image/{imagename}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void ShowImage(@PathVariable String imagename, HttpServletResponse response) throws IOException{
        InputStream image = this.fileService.getResource(path, imagename);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(image, response.getOutputStream());

    }
}
