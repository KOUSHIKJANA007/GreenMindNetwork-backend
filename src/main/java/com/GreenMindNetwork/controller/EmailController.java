package com.GreenMindNetwork.controller;


import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.EmailResponse;
import com.GreenMindNetwork.payloads.UserDto;
import com.GreenMindNetwork.repositories.UserRepo;
import com.GreenMindNetwork.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class EmailController {
    @Autowired
    private UserRepo userRepo;
   @Autowired
   private EmailService emailService;
    @PostMapping("/email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestBody EmailResponse emailResponse){
        Optional<User> byEmail = this.userRepo.findByEmail(emailResponse.getEmail());
        if(byEmail.isPresent()){
            ApiResponse apiResponse=new ApiResponse("Email already exist",false);
            return  new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }
        String otp=this.emailService.generateOtp();
        boolean flag = this.emailService.sendEmailOTP(emailResponse.getEmail(), otp);
        if(flag){
            ApiResponse apiResponse=new ApiResponse("OTP sent successfully",true);
            return new ResponseEntity<>(apiResponse,HttpStatus.OK);
        }
        ApiResponse apiResponse=new ApiResponse("Email is already exist",false);
        return  new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/email/verify")
    public  ResponseEntity<ApiResponse> verifyOtp(@RequestBody EmailResponse emailResponse){
        Boolean flag = this.emailService.verifyOtp(emailResponse);
        if(flag){
            ApiResponse apiResponse=new ApiResponse("email verified",true);
            return new ResponseEntity<>(apiResponse,HttpStatus.OK);
        }
        ApiResponse apiResponse=new ApiResponse("Wrong otp",false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_GATEWAY);
    }
}
