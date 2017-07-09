/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.example.security.data.DefaultUserDetails;
import com.example.security.entity.Group;
import com.example.security.entity.Permission;
import com.example.security.entity.Role;
import com.example.security.entity.User;
import com.example.security.form.CommonListForm;
import com.example.security.service.ExtUserDetailService;
import com.example.security.service.PermissionService;
import com.example.security.service.RoleService;
import com.example.utils.JsonUtils;
/**
 * Add group test
 * @author Joe
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test") 
public class UserGroupRolesIntegrationTest extends AbstractTest{
	
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	
	@Autowired
	private ExtUserDetailService useService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RoleService roleService;
	
	private Role productOperatorRole;
	private Role productAdminRole;
	
	
	private User groupUser1;
	private User groupUser2;
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
		prepareBaseData();
		prepareUserData();
	}
	
	/**
	 * Step0: check Roles which are created when setup
	 * Step1: createGroup
	 * Step2: select members for group:  groupUser1 groupUser2
	 * Step3: update group members
	 * Step4: get group members and check if step3 success
	 * Step5: updateGroupRoles: Roles: productOperatorRole  having permission "Product_R", "Product_U", productAdminRole having permission: 
	 * Step6: updateUserRoles Role: productAdminRole having permission: "Product_R", "Product_U", Product_P"
	 * Step7: checkUserRoles check if user has the 2 roles
	 * Step8: checkUserAuthorities get User detail by username. User should have 3 permissions:"Product_R", "Product_U", Product_P"
	 * step9 delete role: productAdminRole
	 * step10 check if role productAdmin has been removed from group and user
	 * @throws Exception 
	 */
	@Test
	@Transactional
	public void integrationTestCase() throws Exception{
		//step0
		checkRoles();
		
		//step1
		Group group = createGroup("productsuper","Product Super group","Group for Product Super manager.");
		
		//step2
		CommonListForm<Long> memberIdList  = prepareInPojoForUpdate();
		
		//step3
		updateGroupMembers(group.getId(), memberIdList);
		
		//step4
		checkGroupMembers(group.getId(),memberIdList);
		
		//step5
		CommonListForm<String> groupRolesList = operatorAndAdminRoleCodes();
		updateGroupRoles(group.getId(),groupRolesList);
		checkGroupRoles(group.getId(),productOperatorRole,2,4);
		
		//step6
		CommonListForm<String> adminRoleListPojo = productAdminRolePojo();
		updateUserRoles(groupUser1.getEmployeeNumber(),adminRoleListPojo);
		
		//step7
		checkUserRoles(groupUser1.getEmployeeNumber());
		
		//step8 "Product_R", "Product_U", Product_P"
		checkUserAuthorities(groupUser1,3);
		
		//step9 delete role: productAdminRole
		deleteRole(productAdminRole);
		
		//step10 check if role productAdmin has been removed from group and user
		checkGroupRoles(group.getId(),productOperatorRole,1,3);
		
		//step11 check if User authorities
		checkUserAuthorities(groupUser1,2);
	}
	
	 /**
	 * @author Joe
	 * @throws Exception 
	 */
	private void checkRoles() throws Exception {
		this.mvc.perform(get("/uaa/roles")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.success",is(true)))
				.andExpect(jsonPath("$.data.list.length()",is(4)))
				.andExpect(jsonPath("$.data.paging.totalSize",is(4)));
		
		
		this.mvc.perform(get("/uaa/roles").param("viewSize", "2").param("viewIndex", "1")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.success",is(true)))
				.andExpect(jsonPath("$.data.list.length()",is(2)))
				.andExpect(jsonPath("$.data.paging.totalSize",is(4)));
		
		
		this.mvc.perform(get("/uaa/roles").param("name", "PLM")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.success",is(true)))
				.andExpect(jsonPath("$.data.list.length()",is(2)))
				.andExpect(jsonPath("$.data.paging.totalSize",is(2)));
	}

	/**
	 * @author Joe
	 * @param productOperatorRole2
	 * @throws Exception 
	 */
	private void checkGroupRoles(final Long id,Role productOperatorRole, int expectedGroupRoleCount, int expectedAllRoleCount) throws Exception {
		this.mvc.perform(get("/uaa/groups/"+id+"/roles")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.success",is(true)))
				.andExpect(jsonPath("$.data.groupRoles",hasItem(productOperatorRole.getCode())))
				.andExpect(jsonPath("$.data.groupRoles.length()",is(expectedGroupRoleCount)))
				.andExpect(jsonPath("$.data.allRoles.length()",is(expectedAllRoleCount)));
		
	}

	
	/**
	 * @author Joe
	 * @param productAdminRole2
	 * @throws Exception 
	 */
	private void deleteRole(Role productAdminRole) throws Exception {
		this.mvc.perform(delete("/uaa/roles/"+productAdminRole.getCode())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.success",is(true)));
	}

	/**
     * Get groupRoles
     * @param groupId
     * @throws Exception
     */
	private void checkUserRoles(String employeeNo) throws Exception{
		this.mvc.perform(get("/uaa/users/"+employeeNo+"/roles")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.success",is(true)))
				.andExpect(jsonPath("$.data.userRoles",hasItem("plm_approver")))
				.andExpect(jsonPath("$.data.groupRoles",hasItem("plm_operator")))
				.andExpect(jsonPath("$.data.userRoles.length()",is(1)))
				.andExpect(jsonPath("$.data.groupRoles.length()",is(2)))
				.andExpect(jsonPath("$.data.allRoles.length()",is(4) ));
	}
	
	/**
     * check if user has the right permissions
	 * @param groupUser 
     * @throws Exception
     */
	private void checkUserAuthorities(User groupUser, int expectedCount){
		DefaultUserDetails userDetail = (DefaultUserDetails)useService.loadUserByUsername(groupUser.getUsername());
		assertEquals(groupUser1.getEmployeeNumber(), userDetail.getEmployeeNumber());
		assertEquals(expectedCount, userDetail.getAuthorities().size());
	}

	/**
	 * Prepare a list of role codes.
	 * @return
	 */
	private CommonListForm<String> operatorAndAdminRoleCodes(){
		List<String> roles = new ArrayList<String>();
		roles.add(productOperatorRole.getCode());
		roles.add(productAdminRole.getCode());
		CommonListForm<String> rolesList = new CommonListForm<>();
		rolesList.setList(roles);
		return rolesList;
	}
	
	/**
	 * Prepare a list of role codes.
	 * @return
	 */
	private CommonListForm<String> productAdminRolePojo(){
		List<String> roles = new ArrayList<String>();
		roles.add(productAdminRole.getCode());
		
		CommonListForm<String> rolesList = new CommonListForm<>();
		rolesList.setList(roles);
		return rolesList;
	}
	
	/**
	 * Prepare accessObject, operation, permission roles. 
	 * Create 4 roles
	 */
	private void prepareBaseData(){
		Permission productR = permissionService.createPermission("PRODUCT", "Product", "R", "Read","Product Read");
		Permission productU = permissionService.createPermission("PRODUCT", "Product", "U", "Update","Product Update");		
		Permission productApprove = permissionService.createPermission("PRODUCT", "Product", "P", "Approve","Product Update");
		
		Permission srmR = permissionService.createPermission("supplier", "supplier", "R", "Read","Supplier Read");
		Permission srmU = permissionService.createPermission("supplier", "supplier", "U", "Update","Supplier Update");		
		Permission srmApprove = permissionService.createPermission("supplier", "supplier", "P", "Approve","Supplier Update");
		
		Set<String> permissionsProductOperator = new HashSet<String>();
		permissionsProductOperator.add(productU.getCode());
		permissionsProductOperator.add(productR.getCode());		
		
		Set<String> permissionsProductAdmin = new HashSet<String>();
		permissionsProductAdmin.addAll(permissionsProductOperator);
		permissionsProductAdmin.add(productApprove.getCode());
		
		Set<String> permissionSupplierOperator = new HashSet<String>();
		permissionSupplierOperator.add(srmR.getCode());
		permissionSupplierOperator.add(srmU.getCode());
		
		Set<String> permissionSupplierApprover = new HashSet<String>();
		permissionSupplierApprover.add(srmApprove.getCode());
		permissionSupplierApprover.addAll(permissionSupplierApprover);
		
		productOperatorRole = roleService.addOrUpdateRole("plm_operator", "PLM Operator","description", permissionsProductOperator);
		productAdminRole = roleService.addOrUpdateRole("plm_approver", "PLM Approver","description", permissionsProductAdmin);
		
		roleService.addOrUpdateRole("srm_operator", "SRM Operator","description", permissionSupplierOperator);
		roleService.addOrUpdateRole("srm_approver", "SRM Approver", "description",permissionSupplierApprover);
	}
	
	/**
	 * Prepare a list of employeenumber.
	 * @return
	 */
	private CommonListForm<Long> prepareInPojoForUpdate(){
		List<Long> users = new ArrayList<Long>();
		users.add(groupUser1.getId());
		users.add(groupUser2.getId());
		
		CommonListForm<Long> usersList = new CommonListForm<Long>();
		usersList.setList(users);
		return usersList;
	}
	/**
	 * Prepare Users
	 * Create 4 Users
	 */
	private void prepareUserData(){
		groupUser1 = createUser("test1", "test1@okchem.com", "00001","firstName","lastName");
		groupUser2 = createUser("test2", "test2@okchem.com", "00002","firstName","lastName");
		createUser("test3", "test3@okchem.com", "00003","firstName","lastName");
		createUser("test4", "test4@okchem.com", "00004","firstName","lastName");
	}
	
	/**
	 * Update groupMembers
	 * @param groupId
	 * @throws Exception
	 */
    private void updateGroupMembers(Long groupId,CommonListForm<Long> memberList) throws Exception{		
		this.mvc.perform(put("/uaa/groups/"+groupId+"/members")
				.content(JsonUtils.convertModelToJson(memberList))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("{\"success\":true}"));
    }
	
    /**
     * Get groupMembers
     * @param groupId
     * @param memberIdList 
     * @throws Exception
     */
	private void checkGroupMembers(Long groupId, CommonListForm<Long> memberIdList) throws Exception{
		this.mvc.perform(get("/uaa/groups/"+groupId+"/members")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.success",is(true)))
				.andExpect(jsonPath("$.data.groupMembers.length()",is(memberIdList.getList().size())))
				.andExpect(jsonPath("$.data.allMembers.length()",is(4) ));
	}	
	
	/**
	 * Update groupRoles
	 * @param groupId
	 * @throws Exception
	 */
    private void updateGroupRoles(Long groupId,CommonListForm<String> rolesList) throws Exception{		
		this.mvc.perform(put("/uaa/groups/"+groupId+"/roles")
				.content(JsonUtils.convertModelToJson(rolesList))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("{\"success\":true}"));
    }
    
    /**
	 * Update userRoles
	 * @param groupId
	 * @throws Exception
	 */
    private void updateUserRoles(String emplyeeNo,CommonListForm<String> rolesList) throws Exception{		
		this.mvc.perform(put("/uaa/users/"+emplyeeNo+"/roles")
				.content(JsonUtils.convertModelToJson(rolesList))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("{\"success\":true}"));
    }
}
