/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.security.entity.AccessObject;
/**
 * group repository
 * @author Joe
 *
 */
public interface AccessObjectRepo extends JpaRepository<AccessObject, Long>,JpaSpecificationExecutor<AccessObject>{
	
	AccessObject getByCode(String code);
}
