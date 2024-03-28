package com.GreenMindNetwork;

import java.util.List;
import java.util.Optional;

import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.payloads.UserDto;
import com.GreenMindNetwork.repositories.UserRepo;
import com.GreenMindNetwork.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.GreenMindNetwork.config.AppConstants;
import com.GreenMindNetwork.entities.Role;
import com.GreenMindNetwork.repositories.RoleRepo;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GreenMindNetworkApplication implements CommandLineRunner {

	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(GreenMindNetworkApplication.class, args);
	}
	
	@Bean
	 public ModelMapper modelMapper() {
		 return new ModelMapper();
	 }
	

	@Override
	public void run(String... args) throws Exception {
		try {
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
		}catch (Exception e){
			e.fillInStackTrace();
		}


		Optional<User> byEmail = userRepo.findByEmail("admin@gmail.com");
		if (byEmail.isEmpty()){
			User user=new User();
			user.setEmail("admin@gmail.com");
			user.setFname("Koushik");
			user.setLname("Jana");
			user.setPassword(passwordEncoder.encode("admin"));
			User save = this.userRepo.save(user);

			User user1 = userRepo.findById(save.getId()).orElseThrow(null);
			Role role = this.roleRepo.findById(AppConstants.ADMIN_USER).get();
			user1.getRoles().add(role);
			userRepo.save(user1);
		}

	}
}
