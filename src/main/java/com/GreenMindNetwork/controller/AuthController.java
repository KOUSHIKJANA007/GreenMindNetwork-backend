package com.GreenMindNetwork.controller;

import com.GreenMindNetwork.payloads.*;
import com.GreenMindNetwork.service.EmailService;
import com.GreenMindNetwork.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.exception.ApiException;
import com.GreenMindNetwork.security.JwtTokenHelper;
import com.GreenMindNetwork.service.UserService;

import jakarta.validation.Valid;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private FileService fileService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private  EmailService emailService;
	@Value("${project.image.user}")
	private String path;
	@Value("${project.image.event}")
	private String eventPath;
	@Value("${project.image.social}")
	private String socialImagePath;
	@Value("${project.image.post}")
	private String postPath;
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto){
		UserDto registerUser = this.userService.registerUser(userDto);
		return new ResponseEntity<UserDto>(registerUser,HttpStatus.CREATED);
	}


	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest authRequest) throws Exception{
		boolean blocked = this.userService.isBlocked(authRequest.getUsername());
		if(blocked){
			throw new ApiException("Your account is blocked by Admin for furthar information contact with Admin");
		}
		this.authenticate(authRequest.getUsername(), authRequest.getPassword());

		UserDetails userDtails = this.userDetailsService.loadUserByUsername(authRequest.getUsername());
		String generatedToken = this.jwtTokenHelper.generateToken(userDtails);
		
		System.out.println(generatedToken+"value");
		JwtAuthResponse authResponse=new JwtAuthResponse();
		authResponse.setToken(generatedToken);
		authResponse.setUser(this.modelMapper.map((User)userDtails, UserDto.class));
		return ResponseEntity.ok(authResponse);
	}

	private void authenticate(String username, String password) throws Exception {
		UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username, password);
		try {
			this.authenticationManager.authenticate(authenticationToken);
		
		} catch (BadCredentialsException e) {
			 throw new ApiException("invalid username and password");
		}
		
	}
	@GetMapping(value = "/logo/image/{imagename}",produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(
			@PathVariable("imagename") String imagename,
			HttpServletResponse response) throws IOException{
		InputStream resource = this.fileService.getResource(path, imagename);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
