/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.data.BastDTOData;
import com.example.security.data.CommonData;
import com.example.security.data.RoleData;
import com.example.security.data.UserRolesData;
import com.example.security.form.CommonListForm;
import com.example.security.service.ExtUserDetailService;
import com.example.security.service.RoleService;

/**
 * 
 * @author Joe.Li
 *
 */
@RestController
@RequestMapping("/uaa/users")
public class UserController extends BaseController {
	
	/**
	 * Injects of Group Service
	 */
	@Autowired
	private ExtUserDetailService userService;
	
	@Autowired
	private RoleService roleService;

	
	
	/**
	 * 
	 * get user roles
	 * 
	 * @author Joe
	 * @param suit
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{employeeNo}/roles",method=RequestMethod.GET)
	public ResponseEntity<CommonData<UserRolesData>> getRoles(Authentication authentication,@PathVariable final String employeeNo){	
		Set<String> userRoles = userService.getUserRoles(employeeNo);
		Set<String> groupRoles = userService.getGroupRoles(employeeNo);
		List<RoleData> allRoles = roleService.getAllRoles();
		UserRolesData uesrRolesData = new UserRolesData(userRoles,groupRoles,allRoles);
		return ResponseEntity.ok(new CommonData<UserRolesData>(uesrRolesData));
	}
	
	
	/**
	 * 
	 * update user roles
	 * 
	 * @author Joe
	 * @param suit
	 * @param id
	 * @param permissions
	 * @return
	 */
	@RequestMapping(value="/{employeeNo}/roles",method=RequestMethod.PUT)
	public ResponseEntity<BastDTOData> updateRoles(Authentication authentication,@PathVariable final String employeeNo,
			@Valid @RequestBody final CommonListForm<String> roles){
		userService.updatUserRoles(employeeNo, roles.getList());
		return ResponseEntity.ok(success());
	}
}
