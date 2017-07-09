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

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.security.data.CommonData;
import com.example.security.data.DefaultUserDetails;
import com.example.security.data.GroupBasicData;
import com.example.security.data.MemberData;
import com.example.security.data.GroupMembersData;
import com.example.security.data.PaginatedListData;
import com.example.security.data.Paging;
import com.example.security.entity.Group;
import com.example.security.entity.Role;
import com.example.security.entity.User;
import com.example.security.exception.BadRequestException;
import com.example.security.form.GroupBasicForm;
import com.example.security.repo.GroupRepo;
import com.example.security.repo.RoleRepo;
import com.example.security.repo.UserRepo;




/**
 * group service, which provides model operations for client
 * @author Joe
 *
 */
@Service
public class GroupService {
	
	/**
	 * define logger object for the class
	 */
	static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
	/**
	 * injects Group Repository, which provides model operations related to Group
	 */
	@Autowired
	private GroupRepo groupRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RoleRepo roleRepo;
	/**
	 * injects Staff Repository, which provides model operations related to Group
	 */
	@Autowired
	private UserRepo staffRepo;
	
	/**
	 * injects messageSource object for i18n labels
	 */
	@Autowired
    private MessageSource messageSource;
	/**
	 * 
	 * create group
	 * 
	 * @author Joe
	 * @param inGroup
	 * @param object
	 * @return
	 */
	@Transactional
	public void createGroup(GroupBasicForm inGroup, DefaultUserDetails userdetails) {
		if(groupRepo.findByGroupName(inGroup.getGroupName()) != null){
			String msgCode = "50070101";
			String errorMsg = messageSource.getMessage(msgCode,new String[]{inGroup.getGroupName()},LocaleContextHolder.getLocale());
			throw new BadRequestException(msgCode,errorMsg);
		}
		Group group = new Group();
		group.setGroupName(inGroup.getGroupName());
		if(inGroup.getDescription() != null){
			group.setDescription(inGroup.getDescription());
		}
//		group.setCreatedBy(userdetails.getEmployeeNumber());
		groupRepo.save(group);
	}

	/**
	 * 
	 * get group list
	 * 
	 * @author Joe
	 * @param viewIndex
	 * @param viewSize
	 * @param object
	 * @return
	 */
	public CommonData<PaginatedListData<GroupBasicData>> getGroupList(Integer viewIndex, Integer viewSize) {
		CommonData<PaginatedListData<GroupBasicData>> result = new CommonData<PaginatedListData<GroupBasicData>>();
		final Sort sort = new Sort(Sort.Direction.DESC, "createdStamp");
		final Pageable pageable = new PageRequest(viewIndex - 1 , viewSize, sort);
		Page<Group> pages=groupRepo.findAll(pageable);
		
		List<GroupBasicData> groupList = new ArrayList<>();
		for(Group group : pages){
			GroupBasicData oList = new GroupBasicData();
			oList.setGroupName(group.getGroupName());
			oList.setId(group.getId());
			groupList.add(oList);
		}
		Paging paging=new Paging();
		PaginatedListData<GroupBasicData> outList = new PaginatedListData<>();
		paging.setViewIndex(viewIndex);
		paging.setViewSize(viewSize);
		paging.setTotalSize(pages.getTotalElements());
		outList.setList(groupList);
		outList.setPaging(paging);
		result.setData(outList);
		return result;
	}

	/**
	 * 
	 * get group detail
	 * 
	 * @author Joe
	 * @param id
	 * @return
	 */
	public GroupBasicData getGroupDetail(Long id) {
		Group group = checkGroupExistance(id);
		GroupBasicData outGroup = new GroupBasicData();
		outGroup.setGroupName(group.getGroupName());
		outGroup.setDescription(group.getDescription());
		outGroup.setId(group.getId());
		return outGroup;
	}


	/**
	 * 
	 * update group
	 * 
	 * @author Joe
	 * @param id
	 * @param inGroup
	 */
	@Transactional
	public void updateGroup(Long id, GroupBasicForm inGroup) {
		Group group = checkGroupExistance(id);
		
		Group groupTemp = groupRepo.findByGroupName(inGroup.getGroupName());
		if(groupTemp != null && groupTemp.getId() != id){
			throw new ValidationException("50030401");
		}
		
		group.setGroupName(inGroup.getGroupName());
		group.setDescription(inGroup.getDescription());
		groupRepo.save(group);
	}


	/**
	 * 
	 * delete group
	 * 
	 * @author Joe
	 * @param id
	 */
	@Transactional
	public void deleteGroup(Long id) {
		checkGroupExistance(id);
		groupRepo.delete(id);		
	}

	/**
	 * 
	 * get group members
	 * 
	 * @author Joe
	 * @param id
	 * @return
	 */
	public GroupMembersData getGroupMembers(Long id) {
		Group group = checkGroupExistance(id);		
		GroupMembersData out = new GroupMembersData();
		List<Long> groupMembers = new ArrayList<>();
		List<MemberData> outGroupMembers = new ArrayList<>();
		group.getUsers().forEach(member ->{
			groupMembers.add(member.getId());
		});
		Iterable<User> staffList = userRepo.findAll();
		for(User allMembers : staffList){
			MemberData outMember = new MemberData();
			outMember.setEmployeeNo(allMembers.getEmployeeNumber());
			outMember.setFirstName(allMembers.getFirstName());
			outMember.setLastName(allMembers.getLastName());
			outMember.setId(allMembers.getId());
			outGroupMembers.add(outMember);
		}
		out.setGroupMembers(groupMembers);
		out.setAllMembers(outGroupMembers);
		return out;
	}


	/**
	 * 
	 * get group Roles
	 * 
	 * @author Joe
	 * @param id
	 * @return
	 */
	public List<String> getGroupRoles(Long id) {
		List<String> result = new ArrayList<>();
		Group group = checkGroupExistance(id);
		
		Set<Role> roles = group.getRoles();
		if(!CollectionUtils.isEmpty(roles)){
			roles.forEach(role -> {
				result.add(role.getCode());
			});
		}
		return result;
	}


	/**
	 * 
	 * update group member
	 * 
	 * @author Joe
	 * @param id
	 * @param groupMembers
	 */
	@Transactional
	public void updateGroupMember(Long id, List<Long> groupMembers) {		
		Group group = checkGroupExistance(id);
		Set<User> staffs = new HashSet<User>();
		if(groupMembers != null && groupMembers.size()>0){
			groupMembers.forEach(staffId ->{
				User member = staffRepo.findOne(staffId);
				if(member != null){
					staffs.add(member);
					if(member.getGroups() != null) {
						member.getGroups().add(group);
					} else {
						Set<Group> groups = new HashSet<Group>();
						groups.add(group);
						member.setGroups(groups);
					}
				}				
			});
		}
		group.setUsers(staffs);
		groupRepo.save(group);
	}


	/**
	 * 
	 * update group role
	 * 
	 * @author Joe
	 * @param id
	 * @param roleList
	 */
	@Transactional
	public void updateGroupRoles(Long id, List<String> roleList) {
		Group group = checkGroupExistance(id);
		Set<Role> roles = new HashSet<Role>();		
		if(!CollectionUtils.isEmpty(roleList)){
			roleList.forEach(code -> {
				Role role = roleRepo.getByCode(code.toLowerCase());
				roles.add(role);
				if(role.getGroups()==null){
					role.setGroups(new HashSet<Group>());
				}
				role.getGroups().add(group);
			});
		}
		group.setRoles(roles);
		groupRepo.save(group);
	}

	private Group checkGroupExistance(Long id){
		Group group = groupRepo.findOne(id);
		if(group == null){
			String msgCode = "50070000";
			throw new BadRequestException(msgCode,messageSource.getMessage(msgCode, new String[]{id.toString()}, LocaleContextHolder.getLocale()));
		}
		return group;
	}
}
