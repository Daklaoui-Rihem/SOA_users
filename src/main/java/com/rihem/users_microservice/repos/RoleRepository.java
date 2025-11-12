package com.rihem.users_microservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rihem.users_microservice.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
