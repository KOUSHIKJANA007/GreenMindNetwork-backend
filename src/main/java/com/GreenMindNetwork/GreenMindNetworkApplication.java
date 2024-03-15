package com.GreenMindNetwork;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.GreenMindNetwork.config.AppConstants;
import com.GreenMindNetwork.entities.Role;
import com.GreenMindNetwork.repositories.RoleRepo;

@SpringBootApplication
public class GreenMindNetworkApplication implements CommandLineRunner {

	@Autowired
	private RoleRepo roleRepo;
	
	
	public static void main(String[] args) {
		SpringApplication.run(GreenMindNetworkApplication.class, args);
	}
	
	@Bean
	 public ModelMapper modelMapper() {
		 return new ModelMapper();
	 }
	

	@Override
	public void run(String... args) throws Exception {
		Role role1=new Role();
		role1.setId(AppConstants.ADMIN_USER);
		role1.setRoleName("ADMIN_USER");
		
		Role role2=new Role();
		role2.setId(AppConstants.NORMAL_USER);
		role2.setRoleName("NORMAL_USER");

		Role role3=new Role();
		role3.setId(AppConstants.NGO_USER);
		role3.setRoleName("NGO_USER");
		 List<Role> roles = List.of(role1,role2,role3);
		 this.roleRepo.saveAll(roles);
		
	}

}
