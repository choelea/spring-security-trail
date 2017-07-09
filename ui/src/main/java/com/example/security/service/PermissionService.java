/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.example.security.data.OperationData;
import com.example.security.data.PermissionDetailData;
import com.example.security.entity.AccessObject;
import com.example.security.entity.Operation;
import com.example.security.entity.Permission;
import com.example.security.repo.AccessObjectRepo;
import com.example.security.repo.OperationRepo;
import com.example.security.repo.PermissionRepo;

/**
 * @author Joe
 *
 */
@Service
public class PermissionService {
	private static final Logger LOG = LoggerFactory.getLogger(PermissionService.class);
	@Autowired
	private AccessObjectRepo accessObjectRepo;
	
	@Autowired
	private OperationRepo operationRepo;
	@Autowired
	private PermissionRepo permissionRepo;
	
	public AccessObject createAccessObject(String code,String name, String description){
		AccessObject ao = new AccessObject();
		ao.setCode(code);
		ao.setName(name);
		ao.setDescription(description);
		return accessObjectRepo.save(ao);
	}
	public List<PermissionDetailData> getAllPermissions(){
		List<AccessObject> list = accessObjectRepo.findAll();
		List<PermissionDetailData> returnList = new ArrayList<PermissionDetailData>();
		
		list.forEach(accessObject -> {
			PermissionDetailData tempData = new PermissionDetailData();
			tempData.setObjectName(accessObject.getName());
			List<OperationData> operations = new ArrayList<OperationData>();
			tempData.setOperationDatas(operations);
			accessObject.getPermissions().forEach(permission -> {
				tempData.getOperationDatas().add(new OperationData(permission.getCode(), permission.getOperation().getName()));
			});
			returnList.add(tempData);
		});
		return returnList;
	}
	public Operation createOperations(String code,String name, String description){
		Operation o = new Operation();
		o.setCode(code);
		o.setName(name);
		o.setDescription(description);
		return operationRepo.save(o);
	}
	
	/**
	 * Create permission objectCode_operationCode. If cannot find the object/operation by the code, nothing will happen.
	 * @param objectCode
	 * @param operationCode
	 * @param permissionName
	 * @return
	 */
	@Transactional
	public Permission createPermission(String objectCode, String operationCode,String permissionName){
		Assert.notNull(objectCode, "objectCode cannot be null!");
		Assert.notNull(operationCode, "operationCode cannot be null!");
		Assert.notNull(permissionName, "permissionName cannot be null!");
		AccessObject accessObject =accessObjectRepo.getByCode(objectCode.toLowerCase());
		if(accessObject==null){
			LOG.info("Cannot find the access object by given object code!");	
			return null;
		}
		
		Operation operation = operationRepo.getByCode(operationCode.toLowerCase());
		if(operation==null){
			LOG.info("Cannot find the operation by given operation code!");	
			return null;			
		}
		Permission permissoin = createPermission(accessObject,operation,permissionName);
		accessObject.getPermissions().add(permissoin);
		operation.getPermissions().add(permissoin);	
		return permissoin;
	}
	
	/**
	 * Create permission by give object and operation info. It's firstly used in unit test.
	 * @param objectCode  code is not case sensitive, the lowercase will be stored in db
	 * @param objectName
	 * @param operationCode code is not case sensitive, the lowercase will be stored in db
	 * @param operationName
	 * @return
	 */
	@Transactional
	public Permission createPermission(String objectCode,String objectName, String operationCode, String operationName,String permissionName){
		Assert.notNull(objectCode, "objectCode cannot be null!");
		Assert.notNull(operationCode, "operationCode cannot be null!");
		Assert.notNull(permissionName, "permissionName cannot be null!");
		AccessObject accessObject =accessObjectRepo.getByCode(objectCode.toLowerCase());
		if(accessObject==null){
			accessObject =createAccessObject(objectCode,objectName,"");			
		}
		
		Operation operation = operationRepo.getByCode(operationCode.toLowerCase());
		if(operation==null){
			operation = createOperations(operationCode, operationName,"");			
		}
		Permission permissoin = createPermission(accessObject,operation,permissionName);
		return permissoin;		
	}
	
	private Permission createPermission(AccessObject accessObject,Operation operation,String permissionName){
		Permission permission = new Permission();
		permission.setObject(accessObject);
		permission.setOperation(operation);		
		permission.setCode(accessObject.getCode()+"_"+operation.getCode());
		permission.setName(permissionName);
		permission= permissionRepo.save(permission);
		if(accessObject.getPermissions()==null){
			accessObject.setPermissions(new HashSet<Permission>());
		}
		accessObject.getPermissions().add(permission);
		
		if(operation.getPermissions()==null){
			operation.setPermissions(new HashSet<Permission>());
		}
		operation.getPermissions().add(permission);
		return permission;
	}
}
