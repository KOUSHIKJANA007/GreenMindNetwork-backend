package com.GreenMindNetwork.repositories;

import com.GreenMindNetwork.entities.Ngo;
import com.GreenMindNetwork.entities.NgoSocialImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NgoSocialImageRepo extends JpaRepository<NgoSocialImage,Integer> {
    List<NgoSocialImage> findByNgo(Ngo ngo);
}
