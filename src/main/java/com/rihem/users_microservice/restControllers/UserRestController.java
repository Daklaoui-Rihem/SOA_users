package com.rihem.users_microservice.restControllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rihem.users_microservice.entities.User;
import com.rihem.users_microservice.service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class UserRestController {
    
    @Autowired
    UserService userService;
    
    @GetMapping("all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }
}
