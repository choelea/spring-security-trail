/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.example.security.data.DefaultUserDetails;
import com.example.security.entity.Group;
import com.example.security.entity.User;
import com.example.security.repo.GroupRepo;
import com.example.security.repo.UserRepo;

/**
 * @author Joe
 *
 */
public abstract class AbstractTest {
	@Autowired
	protected GroupRepo groupRepo;
	
	@Autowired
	private  UserRepo userRepo;
	
	protected DefaultUserDetails createUserDetail(String username, String password,String email,String employeeNo,String auths){
		List<GrantedAuthority> auth = AuthorityUtils
				.commaSeparatedStringToAuthorityList(auths);
		return new DefaultUserDetails(username, password,true,email,employeeNo,auth);
	}
	protected Group createGroup(String name,String code,String description){
		Group group = new Group();
		group.setGroupName(name);
//		group.setCode(code);
		group.setDescription(description);
		return groupRepo.save(group);
	}	
	protected User createUser(String username, String email, String employeeNumber,String firstName,String lastName){
		User user = new User();
		user.setEmail(email);
		user.setUsername(username);
		user.setEmployeeNumber(employeeNumber);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		return userRepo.save(user);
	}
}
