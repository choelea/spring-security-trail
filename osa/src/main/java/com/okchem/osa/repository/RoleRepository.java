package com.okchem.osa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okchem.osa.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	Role getByCode(String code);
}
