/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.example.security.entity.Group;
import com.example.security.entity.Permission;
import com.example.security.entity.Role;
import com.example.security.form.CommonListForm;
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
public class GroupRolesIntegrationTest extends AbstractTest{
	
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RoleService roleService;
	
	private Role productOperatorRole;
	private Role productAdminRole;
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
		prepareBaseData();
	}
	
	/**
	 * Step1: createGroup
	 * Step2: select roles for group
	 * Step3: update group roles
	 * Step4: get group roles
	 * Step5: get group roles which group not exists
	 * @throws Exception 
	 */
	@Test
	@Transactional
	public void integrationTestCase() throws Exception{
		Group group = createGroup("productsuper","Product Super group","Group for Product Super manager.");
		CommonListForm<String> rolesList  = prepareInPojoForUpdate();
		updateGroupRoles(group.getId(), rolesList);
		getGroupRoles(group.getId());
		getGroupRolesFailed(-111L);
	}
	
	/**
	 * Prepare a list of role codes.
	 * @return
	 */
	private CommonListForm<String> prepareInPojoForUpdate(){
		List<String> roles = new ArrayList<String>();
		roles.add(productOperatorRole.getCode());
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
		
		roleService.addOrUpdateRole("srm_operator", "SRM Operator", "description",permissionSupplierOperator);
		roleService.addOrUpdateRole("srm_approver", "SRM Approver","description", permissionSupplierApprover);
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
     * Get groupRoles
     * @param groupId
     * @throws Exception
     */
	private void getGroupRoles(Long groupId) throws Exception{
		this.mvc.perform(get("/uaa/groups/"+groupId+"/roles")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.success",is(true)))
				.andExpect(jsonPath("$.data.groupRoles",hasItem("plm_operator")))
				.andExpect(jsonPath("$.data.groupRoles.length()",is(2)))
				.andExpect(jsonPath("$.data.allRoles.length()",is(4) ));
	}
	
	/**
     * Get groupRoles while group id not exists
     * @param groupId
     * @throws Exception
     */
	private void getGroupRolesFailed(Long groupId) throws Exception{
		this.mvc.perform(get("/uaa/groups/"+groupId+"/roles")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.success",is(false)));
	}
}
