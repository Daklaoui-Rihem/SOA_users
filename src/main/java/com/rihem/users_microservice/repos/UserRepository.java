package com.rihem.users_microservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rihem.users_microservice.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
