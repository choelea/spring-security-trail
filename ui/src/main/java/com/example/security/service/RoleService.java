/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.security.data.PaginatedListData;
import com.example.security.data.Paging;
import com.example.security.data.RoleData;
import com.example.security.data.RoleDetailData;
import com.example.security.entity.Group;
import com.example.security.entity.Permission;
import com.example.security.entity.Role;
import com.example.security.entity.User;
import com.example.security.exception.BadRequestException;
import com.example.security.repo.GroupRepo;
import com.example.security.repo.PermissionRepo;
import com.example.security.repo.RoleRepo;
import com.example.security.repo.UserRepo;

/**
 * @author Joe
 *
 */
@Service
public class RoleService {
	
	
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private PermissionRepo permissionRepo;	
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private GroupRepo groupRepo;
	@Autowired
	private UserRepo userRepo;
	/**
	 * injects messageSource object for i18n labels
	 */
	@Autowired
    private MessageSource messageSource;
	public List<RoleData> getAllRoles(){
		List<Role> roles = roleRepo.findAll();
		List<RoleData> returnList = new ArrayList<RoleData>();
		roles.forEach(role ->{
			returnList.add(new RoleData(role.getCode(),role.getName(),role.getDescription()));
		});
		return returnList;
	}
	
	public Role create(String roleCode,String roleName,String description, Set<Permission> permissions){
		Assert.notNull(roleCode, "roleCode cannot be null!");
		Assert.notNull(roleName, "roleName cannot be null!");
		
		Role role = new Role();
		role.setCode(roleCode);
		role.setName(roleName);
		role.setDescription(description);
		role.setPermissions(permissions);
		return roleRepo.save(role);
	}
	
	/**
	 * Update basic info
	 * @author Joe
	 * @param roleCode
	 * @param roleName
	 * @param description
	 */
	@Transactional
	public void update(String roleCode,String roleName,String description){
		Assert.notNull(roleCode, "roleCode cannot be null!");
		Assert.notNull(roleName, "roleName cannot be null!");
		Role role = roleRepo.getByCode(roleCode.toLowerCase());
		if(role!=null){
			role.setCode(roleCode);
			role.setName(roleName);
			role.setDescription(description);
			roleRepo.save(role);
		}		
	}
		
	/**
	 * Add or update Role. If there isn't role with the give roleCode, create a new Role
	 * @author Joe
	 * @param roleCode
	 * @param roleName
	 * @param description
	 * @param permissionCodes
	 * @return
	 */
	@Transactional
	public Role addOrUpdateRole(String roleCode,String roleName,String description, Set<String> permissionCodes){
		Assert.notNull(roleCode, "roleCode cannot be null!");
		Assert.notEmpty(permissionCodes);			
		Set<Permission> permissions = permissionRepo.findByCodeIn(permissionCodes);
		Role role = roleRepo.getByCode(roleCode.toLowerCase());
		if(role==null){
			role = create(roleCode, roleName,description, permissions);
		}else{
			role.getPermissions().clear();
			role.getPermissions().addAll(permissions);
			if(!StringUtils.isEmpty(roleName)){
				role.setName(roleName);
			}
			role = roleRepo.save(role);
		}
		return role;
	}

	/**
	 * @author Joe
	 * @param code
	 * @return
	 */
	public RoleDetailData getDetail(String code) {
		RoleDetailData roleDetailData = new RoleDetailData();
		Role role = roleRepo.getByCode(code);
		if(role!=null){
			roleDetailData.setCode(role.getCode());
			roleDetailData.setName(role.getName());
			roleDetailData.setDescription(role.getDescription());
			roleDetailData.setAllPermissions(permissionService.getAllPermissions());
			roleDetailData.setMyPermissions(new ArrayList<String>());
			role.getPermissions().forEach(permission -> {
				roleDetailData.getMyPermissions().add(permission.getCode());
			});
		}
		return roleDetailData;
	}

	/**
	 * @author Joe
	 * @param code
	 * @return
	 */
	public RoleData getBasic(String code) {
		Role role = checkIfExist(code);
		return new RoleData(role.getCode(),role.getName(),role.getDescription());
	}
	
	/**
	 * Throw exception if role with the given code not exist.
	 * @author Joe
	 * @param code
	 * @return
	 */
	private Role checkIfExist(String code){
		Role role = roleRepo.getByCode(code);
		if(role==null){
			String msgCode = "50070301";
			throw new BadRequestException(msgCode,messageSource.getMessage(msgCode, new String[]{code}, LocaleContextHolder.getLocale()));
		}
		return role;
	}

	/**
	 * @author Joe
	 * @param list
	 * @return
	 */
	@Transactional
	public void updatePermissions(String roleCode,List<String> permissionCodes) {
		Role role = checkIfExist(roleCode);
		if(CollectionUtils.isEmpty(permissionCodes)){
			role.setPermissions(null);
		}else{
			Set<Permission> permissions = permissionRepo.findByCodeIn(permissionCodes);
			role.setPermissions(permissions);
		}
		roleRepo.save(role);
	}

	/**
	 * Remove role at the same time remove from groups and users.
	 * @author Joe
	 * @param code
	 */
	@Transactional
	public void delete(String code) {
		Role role = checkIfExist(code);
		Set<Group> groups = role.getGroups();
		if(!CollectionUtils.isEmpty(groups)){
			groups.forEach(group -> {
				group.getRoles().remove(role);
			});
		}
		Set<User> users = role.getUsers();
		if(!CollectionUtils.isEmpty(users)){
			users.forEach(user -> {
				user.getRoles().remove(role);
			});
		}
		groupRepo.save(groups);
		userRepo.save(users);
		roleRepo.delete(role);
		
	}

	/**
	 * @author Joe
	 * @param viewIndex
	 * @param viewSize
	 * @param roleName
	 * @param objectCode
	 * @return
	 */
	public PaginatedListData<RoleData> getRoles(Integer viewIndex, Integer viewSize, String roleName, String objectCode) {
		viewIndex = viewIndex==null?1:viewIndex;
		viewSize = viewSize==null?10:viewSize;
		final Sort sort = new Sort(Sort.Direction.DESC, "name");
		final Pageable pageable = new PageRequest(viewIndex - 1 , viewSize,sort);
		Page<Role> roles;
		if(StringUtils.isEmpty(roleName)){
			roles = roleRepo.findAll(pageable);
		}else{
			roles = roleRepo.findByNameContaining(roleName, pageable);			
		}
		List<RoleData> returnList = new ArrayList<RoleData>();
		if(roles!=null){
			roles.forEach( role -> {
				returnList.add(new RoleData(role.getCode(), role.getName(), role.getDescription()));
			});
		}
		return new PaginatedListData<RoleData>(new Paging(viewIndex, viewSize, roles.getTotalElements()), returnList);
	}
}
