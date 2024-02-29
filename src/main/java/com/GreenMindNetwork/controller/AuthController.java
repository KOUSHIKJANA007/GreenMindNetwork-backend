package com.GreenMindNetwork.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.exception.ApiException;
import com.GreenMindNetwork.payloads.JwtAuthRequest;
import com.GreenMindNetwork.payloads.JwtAuthResponse;
import com.GreenMindNetwork.payloads.UserDto;
import com.GreenMindNetwork.security.JwtTokenHelper;
import com.GreenMindNetwork.service.UserService;

import jakarta.validation.Valid;

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
	private ModelMapper modelMapper;
	
 
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto){
		UserDto registerUser = this.userService.registerUser(userDto);
		return new ResponseEntity<UserDto>(registerUser,HttpStatus.CREATED);
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest authRequest) throws Exception{
		System.out.println("hello");
		this.authenticate(authRequest.getUsername(), authRequest.getPassword());
		System.out.println("hi");
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
}
