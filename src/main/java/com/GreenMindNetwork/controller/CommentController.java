package com.GreenMindNetwork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.CommentDto;
import com.GreenMindNetwork.service.CommentService;

@RestController
@RequestMapping("/api")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@PostMapping("/comment/user/{userId}/post/{postId}")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,
		@PathVariable Integer postId, 
		@PathVariable Integer userId){
		CommentDto comment = this.commentService.createComment(commentDto, postId, userId);
		return new ResponseEntity<>(comment,HttpStatus.CREATED);
	}
	
	@PutMapping("/comment/{commentId}")
	public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto,@PathVariable Integer commentId){
		CommentDto updateComment = this.commentService.updateComment(commentDto, commentId);
		return new ResponseEntity<CommentDto>(updateComment,HttpStatus.OK);
	}
	
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){
		this.commentService.deleteComment(commentId);
		ApiResponse apiResponse=new ApiResponse("comment deleted successfully",true);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}
	
	@GetMapping("/comment/")
	public ResponseEntity<List<CommentDto>> getAllComment(){
		List<CommentDto> allComment = this.commentService.getAllComment();
		return ResponseEntity.ok(allComment);
	}
	
	@GetMapping("/comment/{postId}")
	public ResponseEntity<List<CommentDto>> getCommentByPost(@PathVariable Integer postId){
		List<CommentDto> commentsByPost = this.commentService.getCommentsByPost(postId);
		return ResponseEntity.ok(commentsByPost);
	}
	
}
