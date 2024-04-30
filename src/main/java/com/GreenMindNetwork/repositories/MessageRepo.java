package com.GreenMindNetwork.repositories;

import com.GreenMindNetwork.entities.Message;
import com.GreenMindNetwork.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message,Integer> {
    List<Message> findByUser(User user);
}
