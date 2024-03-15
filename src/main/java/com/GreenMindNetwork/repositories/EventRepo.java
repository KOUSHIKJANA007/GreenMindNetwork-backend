package com.GreenMindNetwork.repositories;

import com.GreenMindNetwork.entities.Event;
import com.GreenMindNetwork.entities.Ngo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EventRepo extends JpaRepository<Event,Integer> {
    List<Event> findByNgo(Ngo ngo);
}
