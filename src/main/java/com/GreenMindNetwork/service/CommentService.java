package com.GreenMindNetwork.service;

import java.util.List;

import com.GreenMindNetwork.payloads.CommentDto;

public interface CommentService {

	CommentDto createComment(CommentDto commentDto,Integer postId,Integer userId);
	CommentDto updateComment(CommentDto commentDto,Integer commentId);
	void deleteComment(Integer commentId);
	List<CommentDto> getAllComment();
	List<CommentDto> getCommentsByPost(Integer postId);
}
