package com.rihem.users_microservice.restControllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.rihem.users_microservice.entities.User;
import com.rihem.users_microservice.service.UserService;
import com.rihem.users_microservice.service.register.RegistationRequest;

@RestController
@CrossOrigin(origins = "*")
public class UserRestController {

    @Autowired
    UserService userService;

    @GetMapping("all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("debug/auth")
    public String getAuthDebug() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "Principal: " + auth.getPrincipal() +
                "\nAuthorities: " + auth.getAuthorities() +
                "\nIs Authenticated: " + auth.isAuthenticated();
    }

    @PostMapping("/register")
    public User register(@RequestBody RegistationRequest request) {
        return userService.registerUser(request);
    }

    @GetMapping("/verifyEmail/{token}")
    public User verifyEmail(@PathVariable("token") String token) {
        return userService.validateToken(token);
    }
}
