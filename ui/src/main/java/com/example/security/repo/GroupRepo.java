/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.security.entity.Group;
/**
 * group repository
 * @author Joe
 *
 */
public interface GroupRepo extends JpaRepository<Group, Long>,JpaSpecificationExecutor<Group>{
	
	Group findByGroupName(String groupName);
}
