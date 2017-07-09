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
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.security.data.DefaultUserDetails;
import com.example.security.data.PermissionAuthority;
import com.example.security.entity.Role;
import com.example.security.entity.User;
import com.example.security.exception.BadRequestException;
import com.example.security.repo.RoleRepo;
import com.example.security.repo.UserRepo;
/**
 * Customized UserDetailsService, will try to get use from local database. "username" could be fields: email or username.
 * @author Joe
 *
 */
@Service
public class ExtUserDetailService implements UserDetailsService {

	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;
	
	/**
	 * injects messageSource object for i18n labels
	 */
	@Autowired
    private MessageSource messageSource;
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return loadFromLocalDB(username.toLowerCase());
	}

	private UserDetails loadFromLocalDB(String username){
		User user = userRepo.findByEmailOrUsername(username,username);
		if (user == null) {
			DefaultUserDetails userDetail = new DefaultUserDetails();
			userDetail.setEnabled(true);
			return userDetail;
		}
		
		//Get the permession from user roles and user group roles
		Set<String> authorities = new HashSet<String>();
		if(user.getRoles() != null){
			user.getRoles().forEach(r -> {
				if(r.getPermissions() != null) {
					r.getPermissions().forEach(p -> {
						authorities.add(p.getCode());
					});
				}
			});
		}
		if(user.getGroups() != null) {
			user.getGroups().forEach(g -> {
				if(g.getRoles() != null){
					g.getRoles().forEach(r -> {
						if(r.getPermissions() != null) {
							r.getPermissions().forEach(p -> {
								authorities.add(p.getCode());
							});
						}
					});
				}
			});
		}
		List<PermissionAuthority> authlist = new ArrayList<>();
		if(!CollectionUtils.isEmpty(authorities)){
			authorities.forEach(authority -> {
				authlist.add(new PermissionAuthority(authority));
			});
		}
		DefaultUserDetails userDetail = new DefaultUserDetails(user.getUsername(), user.getPassword(),true,user.getEmail(),user.getEmployeeNumber(),authlist);
		userDetail.setProductLines(user.getProductLines());
		userDetail.setFirstName(user.getFirstName());
		userDetail.setLastName(user.getLastName());
		userDetail.setMarkets(user.getBusinessMarkets());
		userDetail.setEnabled(user.isEnable());
		return userDetail;
	}
	

	/**
	 * @param employeeNo
	 * @return
	 */
	public Set<String> getUserRoles(String employeeNo) {
		Set<String> result = new HashSet<>();
		User user = checkUserExistance(employeeNo);		
		Set<Role> roles = user.getRoles();
		if(!CollectionUtils.isEmpty(roles)){
			roles.forEach(role -> {
				result.add(role.getCode());
			});
		}
		return result;
	}

	/**
	 * Update user roles
	 * @param id
	 * @param list
	 */
	@Transactional
	public void updatUserRoles(String employeeNo, List<String> roleList) {
		User user = checkUserExistance(employeeNo);
		Set<Role> roles = new HashSet<Role>();
		if(!CollectionUtils.isEmpty(roleList)){
			roleList.forEach(code -> {
				Role role = roleRepo.getByCode(code.toLowerCase());
				if(role.getUsers()==null){
					role.setUsers(new HashSet<User>());
				}
				role.getUsers().add(user);
				roles.add(role);
			});
		}
		user.setRoles(roles);
		userRepo.save(user);
	}

	/**
	 * Get roles which comes from group
	 * @param employeeNo
	 * @return
	 */
	public Set<String> getGroupRoles(String employeeNo) {
		User user = checkUserExistance(employeeNo);
		Set<String> result = new HashSet<>();
		if(!CollectionUtils.isEmpty(user.getGroups())){
			user.getGroups().forEach(group -> {
				if(!CollectionUtils.isEmpty(group.getRoles())){
					group.getRoles().forEach(role -> {
						result.add(role.getCode());
					});
				}				
			});
		}		
		return result;
	}
	
	/**
	 * @param employeeNo
	 * @return
	 */
	private User checkUserExistance(String employeeNo) {
		User user = userRepo.getByEmployeeNumber(employeeNo);
		if(user == null){
			String msgCode = "50070201";
			throw new BadRequestException(msgCode,messageSource.getMessage(msgCode, new String[]{employeeNo.toString()}, LocaleContextHolder.getLocale()));
		}
		return user;
	}

}
