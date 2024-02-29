package com.GreenMindNetwork.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GreenMindNetwork.entities.Comment;
import com.GreenMindNetwork.entities.Post;
import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.payloads.CommentDto;
import com.GreenMindNetwork.repositories.PostRepo;
import com.GreenMindNetwork.repositories.UserRepo;
import com.GreenMindNetwork.service.CommentService;
import com.GreenMindNetwork.repositories.CommentRepo;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepo commentsRepo;
	
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Override
	public CommentDto createComment(CommentDto commentDto,Integer postId,Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setContent(commentDto.getContent());
		comment.setPost(post);
		comment.setUser(user);
		Comment savedComment = this.commentsRepo.save(comment);
		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public CommentDto updateComment(CommentDto commentDto, Integer commentId) {
		Comment comment = this.commentsRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment", "id", commentId));
		comment.setContent(commentDto.getContent());
		Comment updatedComment = this.commentsRepo.save(comment);
		return this.modelMapper.map(updatedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = this.commentsRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment", "id", commentId));
		System.out.println("This is delete section");
		this.commentsRepo.delete(comment);
		System.out.println("deleted");
	}

	@Override
	public List<CommentDto> getAllComment() {
		List<Comment> comments = this.commentsRepo.findAll();
		List<CommentDto> collectedComments = comments.stream().map((c)->this.modelMapper.map(c, CommentDto.class)).collect(Collectors.toList());
		return collectedComments;
	}

	@Override
	public List<CommentDto> getCommentsByPost(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));
		List<Comment> findByPost = this.commentsRepo.findByPost(post);
		  List<CommentDto> collectedComments = findByPost.stream().map((p)->this.modelMapper.map(p, CommentDto.class)).collect(Collectors.toList());
		return collectedComments;
	}

}
