/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.data.BastDTOData;
import com.example.security.data.CommonData;
import com.example.security.data.DefaultUserDetails;
import com.example.security.data.GroupBasicData;
import com.example.security.data.GroupRolesData;
import com.example.security.data.GroupMembersData;
import com.example.security.data.PaginatedListData;
import com.example.security.data.RoleData;
import com.example.security.form.GroupBasicForm;
import com.example.security.form.CommonListForm;
import com.example.security.service.GroupService;
import com.example.security.service.RoleService;

/**
 * 
 * @author Joe.Li
 *
 */
@RestController
@RequestMapping("/uaa/groups")
public class GroupController extends BaseController {
	
	/**
	 * Injects of Group Service
	 */
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private RoleService roleService;

	/**
	 * 
	 * add group
	 * 
	 * @author Joe
	 * @param suit
	 * @param inGroup
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<BastDTOData> create(Authentication authentication,@Valid @RequestBody final GroupBasicForm inGroup){
		DefaultUserDetails userdetails = (DefaultUserDetails)authentication.getPrincipal();
		groupService.createGroup(inGroup, userdetails);
		return ResponseEntity.ok(getSuccessResponse());
	}
	
	/**
	 * 
	 * get group list
	 * 
	 * @author Joe
	 * @param suit
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<CommonData<PaginatedListData<GroupBasicData>>> get(Authentication authentication,
			@RequestParam("viewIndex") Integer viewIndex,@RequestParam("viewSize") Integer viewSize){
		Assert.notNull(viewIndex, "viewIndex cannot be null");
		Assert.notNull(viewSize, "viewSize cannot be null");
		Assert.isTrue(viewIndex > 0, "viewIndex must be greater than zero");
		return ResponseEntity.ok(groupService.getGroupList(viewIndex, viewSize));
	}
	
	/**
	 * 
	 * get group detail
	 * 
	 * @author Joe
	 * @param suit
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<CommonData<GroupBasicData>> get(Authentication authentication,@PathVariable final Long id){
		
		GroupBasicData group = groupService.getGroupDetail(id);
		return ResponseEntity.ok(new CommonData<GroupBasicData>(group));
	}
	
	/**
	 * 
	 * update group
	 * 
	 * @author Joe
	 * @param suit
	 * @param id
	 * @param inGroup
	 * @return
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public ResponseEntity<BastDTOData> update(Authentication authentication,@PathVariable final Long id,
			@Valid @RequestBody final GroupBasicForm inGroup){
		groupService.updateGroup(id, inGroup);
		return ResponseEntity.ok(this.getSuccessResponse(id));
	}
	
	/**
	 * 
	 * delete group
	 * 
	 * @author Joe
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<BastDTOData> delete(@PathVariable final Long id){
		groupService.deleteGroup(id);
		return ResponseEntity.ok(this.getSuccessResponse(id));
	}
	
	/**
	 * 
	 * get group members
	 * 
	 * @author Joe
	 * @param suit
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/members",method=RequestMethod.GET)
	public ResponseEntity<CommonData<GroupMembersData>> getMembers(Authentication authentication,@PathVariable final Long id){
		
		GroupMembersData outGroupMembers = groupService.getGroupMembers(id);
		return ResponseEntity.ok(new CommonData<GroupMembersData>(outGroupMembers));
	}
	
	/**
	 * 
	 * update group member
	 * 
	 * @author Joe
	 * @param suit
	 * @param id
	 * @param groupMembers
	 * @return
	 */
	@RequestMapping(value="/{id}/members",method=RequestMethod.PUT)
	public ResponseEntity<BastDTOData> updateMembers(Authentication authentication,@PathVariable final Long id,
			@Valid @RequestBody final CommonListForm<Long> groupMembers){
		groupService.updateGroupMember(id, groupMembers.getList());
		return ResponseEntity.ok(this.getSuccessResponse(id));
	}
	
	/**
	 * 
	 * get group permissions
	 * 
	 * @author Joe
	 * @param suit
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}/roles",method=RequestMethod.GET)
	public ResponseEntity<CommonData<GroupRolesData>> getRoles(Authentication authentication,@PathVariable final Long id){	
		List<String> groupRoles = groupService.getGroupRoles(id);
		List<RoleData> allRoles = roleService.getAllRoles();
		GroupRolesData groupData = new GroupRolesData(groupRoles,allRoles);
		return ResponseEntity.ok(new CommonData<GroupRolesData>(groupData));
	}
	
	/**
	 * 
	 * update group permission
	 * 
	 * @author Joe
	 * @param suit
	 * @param id
	 * @param permissions
	 * @return
	 */
	@RequestMapping(value="/{id}/roles",method=RequestMethod.PUT)
	public ResponseEntity<BastDTOData> updateRoles(Authentication authentication,@PathVariable final Long id,
			@Valid @RequestBody final CommonListForm<String> roles){
		groupService.updateGroupRoles(id, roles.getList());
		return ResponseEntity.ok(this.getSuccessResponse(id));
	}
}
