package com.GreenMindNetwork.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.EmailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.GreenMindNetwork.config.AppConstants;
import com.GreenMindNetwork.entities.Role;
import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.payloads.UserDto;
import com.GreenMindNetwork.payloads.UserEditDto;
import com.GreenMindNetwork.repositories.RoleRepo;
import com.GreenMindNetwork.repositories.UserRepo;
import com.GreenMindNetwork.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		user.setFname(userDto.getFname());
		user.setLname(userDto.getLname());
		user.setEmail(userDto.getEmail());
		user.setMobile(userDto.getMobile());
		user.setDob(userDto.getDob());
		user.setImageName("default.jpg");
		if(userDto.getInstagramLink()!=null){
		user.setInstagramLink(userDto.getInstagramLink().replace("https://",""));
		}else {
			user.setInstagramLink(null);
		}
		if(userDto.getFacebookLink()!=null){
			user.setFacebookLink(userDto.getFacebookLink().replace("https://",""));
		}else {
			user.setFacebookLink(null);
		}
		if(userDto.getYoutubeLink()!=null){
			user.setYoutubeLink(userDto.getYoutubeLink().replace("https://",""));
		}else {
			user.setYoutubeLink(null);
		}
		if(userDto.getTwitterLink()!= null){
		user.setTwitterLink(userDto.getTwitterLink().replace("https://",""));
		}
		else {
			user.setTwitterLink(null);
		}
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		User savedUser = this.userRepo.save(user);
		return this.modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public UserEditDto updateUser(UserEditDto userDto, Integer userId) {

		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
		user.setFname(userDto.getFname());
		user.setLname(userDto.getLname());
		user.setEmail(userDto.getEmail());
		user.setMobile(userDto.getMobile());
		user.setDob(userDto.getDob());
		user.setImageName(userDto.getImageName());
		if(userDto.getInstagramLink()!=null){
			user.setInstagramLink(userDto.getInstagramLink().replace("https://",""));
		}else {
			user.setInstagramLink(null);
		}
		if(userDto.getFacebookLink()!=null){
			user.setFacebookLink(userDto.getFacebookLink().replace("https://",""));
		}else {
			user.setFacebookLink(null);
		}
		if(userDto.getYoutubeLink()!=null){
			user.setYoutubeLink(userDto.getYoutubeLink().replace("https://",""));
		}else {
			user.setYoutubeLink(null);
		}
		if(userDto.getTwitterLink()!= null){
			user.setTwitterLink(userDto.getTwitterLink().replace("https://",""));
		}
		else {
			user.setTwitterLink(null);
		}
		User savedUser = this.userRepo.save(user);
		return this.modelMapper.map(savedUser, UserEditDto.class);
	}

	@Override
	public void deleteUser(Integer userId) {
		Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
		Role role1 = this.roleRepo.findById(AppConstants.NGO_USER).get();

		List<Role> role11 = List.of(role1, role);
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));

		user.getRoles().removeAll(role11);
		this.userRepo.save(user);
		this.userRepo.delete(user);
	}

	@Override
	public void changePassword(EmailResponse emailResponse) {
		User user = this.userRepo.findByEmail(emailResponse.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "id", emailResponse.getEmail()));
		user.setPassword(passwordEncoder.encode(emailResponse.getNewPassword()));
		this.userRepo.save(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = this.userRepo.findAll();
		List<UserDto> collectedUsers = users.stream().map((user)->this.modelMapper.map(user,UserDto.class)).collect(Collectors.toList());
		return collectedUsers;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto registerUser(UserDto userDto) {
		Optional<User> byEmail = this.userRepo.findByEmail(userDto.getEmail());
		if (byEmail.isPresent()) {
			ApiResponse apiResponse = new ApiResponse("Email already exist", false);
			return this.modelMapper.map(apiResponse, UserDto.class);
		}
		User user = this.modelMapper.map(userDto, User.class);
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
		user.getRoles().add(role);
		user.setImageName("default.jpg");
		User savedUser = this.userRepo.save(user);
		return this.modelMapper.map(savedUser, UserDto.class);
	}


	

}
