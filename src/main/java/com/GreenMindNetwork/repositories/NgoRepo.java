package com.GreenMindNetwork.repositories;

import com.GreenMindNetwork.entities.Ngo;
import com.GreenMindNetwork.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NgoRepo extends JpaRepository<Ngo,Integer> {
    Ngo findByUser(User user);
}
