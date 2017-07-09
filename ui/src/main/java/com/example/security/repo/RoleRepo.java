/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.security.entity.Role;
/**
 * group repository
 * @author Joe
 *
 */
public interface RoleRepo extends JpaRepository<Role, Long>,JpaSpecificationExecutor<Role>{
	
	Role getByCode(String code);
	Page<Role> findByNameContaining(String name,Pageable pageable);
}
