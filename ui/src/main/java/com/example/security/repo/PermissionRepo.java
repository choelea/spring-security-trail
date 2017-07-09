/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.repo;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.security.entity.Permission;
/**
 * group repository
 * @author Joe
 *
 */
public interface PermissionRepo extends JpaRepository<Permission, Long>,JpaSpecificationExecutor<Permission>{
	
	Permission getByCode(String code);
	
	Set<Permission> findByCodeIn(Collection<String> codes);
}
