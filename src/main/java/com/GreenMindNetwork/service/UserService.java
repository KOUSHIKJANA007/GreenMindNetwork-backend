package com.GreenMindNetwork.service;

import java.util.List;

import com.GreenMindNetwork.payloads.UserDto;
import com.GreenMindNetwork.payloads.UserEditDto;

public interface UserService {
	UserDto registerUser(UserDto userDto);
	UserDto createUser(UserDto userDto);
	UserEditDto updateUser(UserEditDto userDto,Integer userId);
	void deleteUser(Integer userId);
	List<UserDto> getAllUsers();
	UserDto getUserById(Integer userId);
}