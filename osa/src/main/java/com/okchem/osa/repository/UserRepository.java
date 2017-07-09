package com.okchem.osa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okchem.osa.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
