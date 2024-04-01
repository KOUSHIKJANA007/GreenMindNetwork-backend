package com.GreenMindNetwork.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.GreenMindNetwork.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.GreenMindNetwork.entities.Post;
import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.payloads.PostDto;
import com.GreenMindNetwork.payloads.PostResponse;
import com.GreenMindNetwork.repositories.PostRepo;
import com.GreenMindNetwork.repositories.UserRepo;
import com.GreenMindNetwork.service.PostService;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private FileService fileService;

	@Value("${project.image.post}")
	private String path;
	@Override
	public PostDto createPost(PostDto postDto,Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user", "id", userId));
		Post post = this.modelMapper.map(postDto, Post.class);
		
		post.setTitle(postDto.getTitle());
		post.setSubTitle(postDto.getSubTitle());
		post.setContent(postDto.getContent());
		post.setImageName("default.jpg");
		post.setPostDate(new Date());
		post.setUser(user);
		Post savedPost = this.postRepo.save(post);
		return this.modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) throws IOException {
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));
		post.setTitle(postDto.getTitle());
		post.setSubTitle(postDto.getSubTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) throws IOException {
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));
		boolean f=true;
		while (f){
			try {
				this.fileService.deleteImage(path,post.getImageName());
				f=false;
			} catch (IOException e) {
				continue;
			}
		}
		this.postRepo.delete(post);
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
		Sort sort=null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort=Sort.by(sortBy).ascending();
		}
		else {
			sort=Sort.by(sortBy).descending();
		}
		
		Pageable p =PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> pageposts = this.postRepo.findAll(p);
		List<Post> posts = pageposts.getContent();
		List<PostDto> collectedPost = posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		PostResponse postResponse=new PostResponse();
		postResponse.setContent(collectedPost);
		postResponse.setPageNumber(pageposts.getNumber());
		postResponse.setPageSize(pageposts.getSize());
		postResponse.setTotalElement(pageposts.getTotalElements());
		postResponse.setTotalPage(pageposts.getTotalPages());
		postResponse.setLastPage(pageposts.isLast());
		return postResponse;
	}

	@Override
	public PostDto getSinglePost(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));
		
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostResponse getPostByUser(Integer userId,Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
		Sort sort=null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort=Sort.by(sortBy).ascending();
		}
		else {
			sort=Sort.by(sortBy).descending();
		}
		Pageable p=PageRequest.of(pageNumber, pageSize, sort);
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user", "id", userId));
		Page<Post> pagepost = this.postRepo.findByUser(user,p);
		List<Post> content = pagepost.getContent();
		List<PostDto> collectedPosts = content.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponse postResponse=new PostResponse();
		postResponse.setContent(collectedPosts);
		postResponse.setPageNumber(pagepost.getNumber());
		postResponse.setPageSize(pagepost.getSize());
		postResponse.setTotalElement(pagepost.getTotalElements());
		postResponse.setTotalPage(pagepost.getTotalPages());
		postResponse.setLastPage(pagepost.isLast());
		return postResponse;
	}

	@Override
	public PostResponse searchPost(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
		Sort sort=null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort=Sort.by(sortBy).ascending();
		}
		else {
			sort=Sort.by(sortBy).descending();
		}
		Pageable p=PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> searchPost = this.postRepo.searchPost("%"+keyword+"%",p);
		List<Post> content = searchPost.getContent();
		List<PostDto> posts = content.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponse postResponse=new PostResponse();
		postResponse.setContent(posts);
		postResponse.setPageNumber(searchPost.getNumber());
		postResponse.setPageSize(searchPost.getSize());
		postResponse.setTotalElement(searchPost.getTotalElements());
		postResponse.setTotalPage(searchPost.getTotalPages());
		postResponse.setLastPage(searchPost.isLast());
		return postResponse;
	}

}
