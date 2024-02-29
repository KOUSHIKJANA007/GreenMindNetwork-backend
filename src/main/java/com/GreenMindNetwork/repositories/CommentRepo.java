package com.GreenMindNetwork.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GreenMindNetwork.entities.Comment;
import com.GreenMindNetwork.entities.Post;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

	List<Comment> findByPost(Post post);
	
}
