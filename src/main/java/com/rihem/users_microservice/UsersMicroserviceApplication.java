package com.rihem.users_microservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rihem.users_microservice.entities.Role;
import com.rihem.users_microservice.entities.User;*/
import com.rihem.users_microservice.service.UserService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class UsersMicroserviceApplication {
	@Autowired
	UserService userService;

	/*@PostConstruct
	void init_users() {
		// Add roles
		userService.addRole(new Role(null, "ADMIN"));
		userService.addRole(new Role(null, "USER"));

		// Add users
		userService.saveUser(new User(null, "admin", "123", true, null));
		userService.saveUser(new User(null, "rihem", "123", true, null));
		userService.saveUser(new User(null, "chaima", "123", true, null));

		// Assign roles to users
		userService.addRoleToUser("admin", "ADMIN");
		userService.addRoleToUser("admin", "USER");
		userService.addRoleToUser("rihem", "USER");
		userService.addRoleToUser("chaima", "USER");
	}*/

	public static void main(String[] args) {
		SpringApplication.run(UsersMicroserviceApplication.class, args);
	}

}
