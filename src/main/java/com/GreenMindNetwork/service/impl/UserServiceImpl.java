package com.GreenMindNetwork.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.GreenMindNetwork.entities.BlockStatus;
import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.EmailResponse;
import com.GreenMindNetwork.repositories.BlockStatusRepo;
import com.GreenMindNetwork.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	private BlockStatusRepo blockStatusRepo;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private FileService fileService;
	@Value("${project.image.user}")
	private String path;

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		user.setFname(userDto.getFname());
		user.setLname(userDto.getLname());
		user.setEmail(userDto.getEmail());
		user.setMobile(userDto.getMobile());
		user.setDob(userDto.getDob());
		user.setImageName("default.png");
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
	public UserEditDto updateUser(UserEditDto userDto, Integer userId) throws IOException {
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
	public void deleteUser(Integer userId) throws IOException {
		Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
		Role role1 = this.roleRepo.findById(AppConstants.NGO_USER).get();
		BlockStatus blockStatus = this.blockStatusRepo.findById(AppConstants.UNBLOCKED).get();
		List<Role> role11 = List.of(role1, role);
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
		user.getRoles().removeAll(role11);
		user.getStatus().remove(blockStatus);
		this.userRepo.save(user);
		boolean f=true;
		if(!user.getImageName().equals("default.png")){
			while (f){
				try {
					this.fileService.deleteImage(path,user.getImageName());
					f=false;
				} catch (IOException e) {
					continue;
				}
			}
		}
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
	public void blockUser(Integer userId) {
		BlockStatus blockStatus = this.blockStatusRepo.findById(AppConstants.UNBLOCKED).get();
		BlockStatus blockStatus1 = this.blockStatusRepo.findById(AppConstants.BLOCKED).get();
		User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		user.getStatus().remove(blockStatus);
		user.getStatus().add(blockStatus1);
		this.userRepo.save(user);
	}

	@Override
	public void unBlockUser(Integer userId) {
		BlockStatus blockStatus1 = this.blockStatusRepo.findById(AppConstants.BLOCKED).get();
		BlockStatus blockStatus = this.blockStatusRepo.findById(AppConstants.UNBLOCKED).get();
		User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		user.getStatus().remove(blockStatus1);
		user.getStatus().add(blockStatus);
		this.userRepo.save(user);
	}

	@Override
	public boolean isBlocked(String username) {
		BlockStatus blockStatus = this.blockStatusRepo.findById(AppConstants.BLOCKED).get();
		User user = this.userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "id", username));
		if (user.getStatus().contains(blockStatus)){
			return true;
		}
		return false;
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
		BlockStatus blockStatus = this.blockStatusRepo.findById(AppConstants.UNBLOCKED).get();
		user.getStatus().add(blockStatus);
		user.setImageName("default.png");
		User savedUser = this.userRepo.save(user);
		return this.modelMapper.map(savedUser, UserDto.class);
	}


	

}
