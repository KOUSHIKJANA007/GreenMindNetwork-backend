package com.GreenMindNetwork.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.GreenMindNetwork.entities.Post;
import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.payloads.CommentDto;

public interface PostRepo extends JpaRepository<Post, Integer> {
  Page<Post> findByUser(User user,Pageable pageable); 
  @Query("select p from Post p where p.title like :key")
  Page<Post> searchPost(@Param("key")String title,Pageable pageable);
}
