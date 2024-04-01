package com.GreenMindNetwork.service;

import java.io.IOException;
import java.util.List;

import com.GreenMindNetwork.payloads.PostDto;
import com.GreenMindNetwork.payloads.PostResponse;

public interface PostService {

	PostDto createPost(PostDto postDto,Integer userId);
	PostDto updatePost(PostDto postDto,Integer postId) throws IOException;
	void deletePost(Integer postId) throws IOException;
	PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
	PostDto getSinglePost(Integer postId);
	PostResponse getPostByUser(Integer userId,Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
	PostResponse searchPost(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
}
