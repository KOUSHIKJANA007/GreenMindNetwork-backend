package com.GreenMindNetwork.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import com.GreenMindNetwork.config.AppConstants;
import com.GreenMindNetwork.entities.Role;
import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.repositories.RoleRepo;
import com.GreenMindNetwork.repositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.UserDto;
import com.GreenMindNetwork.payloads.UserEditDto;
import com.GreenMindNetwork.service.FileService;
import com.GreenMindNetwork.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private FileService fileService;
	
    @Autowired
	private ModelMapper modelMapper;
	
	@Value("${project.image.user}")
	private String path;

	@GetMapping("/test")
	public String test(){
		return "all done koushik";
	}
	
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		UserDto createUser = this.userService.createUser(userDto);
		return new ResponseEntity<>(createUser,HttpStatus.CREATED);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserEditDto> updateUser(@Valid @RequestBody UserEditDto userDto,@PathVariable Integer userId) throws IOException {
		UserEditDto updateUser = this.userService.updateUser(userDto, userId);
		return ResponseEntity.ok(updateUser);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId) throws IOException {
		Role role = this.roleRepo.findById(AppConstants.ADMIN_USER).get();
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));

		if(user.getRoles().contains(role)){
			ApiResponse apiResponse=new ApiResponse("Admin can't delete",false);
            return ResponseEntity.ok(apiResponse);
		}
		this.userService.deleteUser(userId);
		ApiResponse apiResponse=new ApiResponse("user deleted successfully",true);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		List<UserDto> allUsers = this.userService.getAllUsers();
		return new ResponseEntity<>(allUsers,HttpStatus.OK);
	}
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId){
		UserDto userById = this.userService.getUserById(userId);
		return new ResponseEntity<>(userById,HttpStatus.OK);
	}
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
		UserDto userById = this.userService.getUserByEmail(email);
		return new ResponseEntity<>(userById,HttpStatus.OK);
	}
	@GetMapping("/block/{userId}")
	public ResponseEntity<ApiResponse> blockUser(@PathVariable Integer userId){
		this.userService.blockUser(userId);
		ApiResponse apiResponse=new ApiResponse("user blocked",true);
		return ResponseEntity.ok(apiResponse);
	}
	@GetMapping("/unblock/{userId}")
	public ResponseEntity<ApiResponse> unBlockUser(@PathVariable Integer userId){
		this.userService.unBlockUser(userId);
		ApiResponse apiResponse=new ApiResponse("user unblocked",true);
		return ResponseEntity.ok(apiResponse);
	}
	@GetMapping("/isBlocked/{username}")
	public ResponseEntity<ApiResponse> isBlocked(@PathVariable String username){
		boolean blocked = this.userService.isBlocked(username);
		if (blocked){
			ApiResponse apiResponse=new ApiResponse("user blocked",false);
			return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(new ApiResponse("",true));
	}


	@PostMapping(value = "/image/upload/{userId}")
	public ResponseEntity<UserEditDto> uploadImage(
			@PathVariable("userId") Integer userId,
			@RequestParam("image") MultipartFile image) throws IOException, InterruptedException {

		UserDto userDto = this.userService.getUserById(userId);
		String oldImage = userDto.getImageName();
		UserEditDto userEdit = this.modelMapper.map(userDto, UserEditDto.class);
		String uploadImage=this.fileService.uploadImage(path, image);
		userEdit.setImageName(uploadImage);
		UserEditDto updatePost = this.userService.updateUser(userEdit, userId);
		if(!oldImage.equals("default.png")){
			if(!updatePost.getImageName().equals(oldImage)){
				Thread deleteOldImage=new Thread(()->{
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                   boolean f=true;
					while (f){
						try {
							this.fileService.deleteImage(path,oldImage);
							f=false;
						} catch (IOException e) {
							continue;
						}
					}
                });
				deleteOldImage.start();
			}
		}
		return new ResponseEntity<>(updatePost,HttpStatus.OK);
	}
	
	@GetMapping(value = "/image/{imagename}",produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(
			@PathVariable("imagename") String imagename,
			HttpServletResponse response) throws IOException{
		InputStream resource = this.fileService.getResource(path, imagename);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
