/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.security.entity.Operation;
/**
 * group repository
 * @author Joe
 *
 */
public interface OperationRepo extends JpaRepository<Operation, Long>,JpaSpecificationExecutor<Operation>{
	
	Operation getByCode(String code);
}
