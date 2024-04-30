package com.GreenMindNetwork.service;

import java.io.IOException;
import java.util.List;

import com.GreenMindNetwork.payloads.EmailResponse;
import com.GreenMindNetwork.payloads.UserDto;
import com.GreenMindNetwork.payloads.UserEditDto;
import jakarta.servlet.http.HttpSession;

public interface UserService {
	UserDto registerUser(UserDto userDto);
	UserDto createUser(UserDto userDto);
	UserEditDto updateUser(UserEditDto userDto,Integer userId) throws IOException;
	void deleteUser(Integer userId) throws IOException;
	void changePassword(EmailResponse emailResponse);
	List<UserDto> getAllUsers();
	UserDto getUserById(Integer userId);
	UserDto getUserByEmail(String email);
	void blockUser(Integer userId);
	void unBlockUser(Integer userId);
	boolean isBlocked(String username);
}
