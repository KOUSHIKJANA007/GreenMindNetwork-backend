package com.GreenMindNetwork.repositories;

import com.GreenMindNetwork.entities.BlockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockStatusRepo extends JpaRepository<BlockStatus,Integer> {
}
