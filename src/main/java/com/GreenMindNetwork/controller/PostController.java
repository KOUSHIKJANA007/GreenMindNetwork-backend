package com.GreenMindNetwork.controller;

import java.io.IOException;
import java.io.InputStream;

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

import com.GreenMindNetwork.config.AppConstants;
import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.PostDto;
import com.GreenMindNetwork.payloads.PostResponse;
import com.GreenMindNetwork.service.FileService;
import com.GreenMindNetwork.service.PostService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	
	@PostMapping("/post/{userId}")
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto,@PathVariable("userId") Integer userId){
		PostDto post = this.postService.createPost(postDto, userId);
		return new ResponseEntity<>(post,HttpStatus.CREATED);
	}
	@PutMapping("/post/{postId}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable Integer postId){
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		System.out.println(postDto);
		return new ResponseEntity<>(updatePost,HttpStatus.OK);
	}
	@DeleteMapping("/post/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId){
		this.postService.deletePost(postId);
		ApiResponse response=new ApiResponse("post successfully deleted",true);
		return ResponseEntity.ok(response);
	}
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
			@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
			@RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
			@RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
		PostResponse allPosts = this.postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);
		return ResponseEntity.ok(allPosts);
	}
	
	@GetMapping("/post/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId){
		PostDto singlePost = this.postService.getSinglePost(postId);
		return ResponseEntity.ok(singlePost);
	}
	
	@GetMapping("/post/user/{userId}")
	public ResponseEntity<PostResponse> getPostByUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
			@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
			@RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
			@RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
		 PostResponse postByUser = this.postService.getPostByUser(userId,pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<>(postByUser,HttpStatus.OK);
	}
	
	@GetMapping("/post/search/{keyword}")
	public ResponseEntity<PostResponse> searchPost(@PathVariable String keyword,
			@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
			@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
			@RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
			@RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
		PostResponse searchPost = this.postService.searchPost(keyword, pageNumber, pageSize, sortBy, sortDir);
		return ResponseEntity.ok(searchPost);
	}
	
	@PostMapping(value = "/post/image/{postId}")
	public ResponseEntity<PostDto> uploadImage(@PathVariable Integer postId,@RequestParam("image") MultipartFile image) throws IOException{
		PostDto post = this.postService.getSinglePost(postId);
		String uploadImage = this.fileService.uploadImage(path, image);
		post.setImageName(uploadImage);
		PostDto updatePost = this.postService.updatePost(post, postId);
		return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
	}
	
	@GetMapping(value = "/post/image/{imagename}",produces = MediaType.IMAGE_JPEG_VALUE)
	public void ShowImage(@PathVariable String imagename,HttpServletResponse response) throws IOException{
		InputStream image = this.fileService.getResource(path, imagename);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(image, response.getOutputStream());
		
	}
}
