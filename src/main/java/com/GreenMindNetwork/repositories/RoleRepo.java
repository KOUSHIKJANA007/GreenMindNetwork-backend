package com.GreenMindNetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GreenMindNetwork.entities.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {

}
