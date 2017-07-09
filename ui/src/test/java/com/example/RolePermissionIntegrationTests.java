/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
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

import com.example.security.entity.Permission;
import com.example.security.form.CommonListForm;
import com.example.security.form.RoleForm;
import com.example.security.repo.RoleRepo;
import com.example.security.service.PermissionService;
import com.example.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * add group test
 * 
 * @author Joe
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder()
public class RolePermissionIntegrationTests extends AbstractTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private PermissionService permissionService;

	private List<String> supplierApproverPermissions = new ArrayList<String>();//Prepare with 3 permissions
	private List<String> supplierOperatorPermissions = new ArrayList<String>(); //Prepare with 2 permissions
	private Permission srmApprove;
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	/**
	 * This is an integration that all
	 * @author Joe
	 * @throws Exception
	 */
	@Test
	@Transactional	
	public void intTest() throws Exception {
		String roleCode = "product_admin";
		String roleName = "Product Admin";
		createRole(roleCode, roleName); 							// step1: Create role with base info but no permissions
		prepareBaseData();											// step2: Prepare permissions
		updatePermissions(roleCode,supplierApproverPermissions); 	// step3: Set permissions to role
		checkPermissions(roleCode,supplierApproverPermissions); 	// step4: Check if the permission is set to role
		updatePermissions(roleCode,supplierOperatorPermissions); 	// step5: Set permissions to role
		checkPermissions(roleCode,supplierOperatorPermissions); 	// step6: Check if the permission is set to role
		updateRole(roleCode, "Product Admin2", "test if permission is impacted"); // step6: Check update basic info
		checkPermissions(roleCode,supplierOperatorPermissions); 	// step7: Check that the update didn't wipe permissions
	}

	/**
	 * @author Joe
	 * @param roleCode
	 * @param permissions 
	 * @throws Exception 
	 */
	private void checkPermissions(String roleCode, List<String> permissions) throws Exception {
		this.mvc.perform(get("/uaa/roles/"+roleCode+"/permissions")
		.contentType(MediaType.APPLICATION_JSON_UTF8).with(csrf())
		.with(user(createUserDetail("peter", "123", "Joe@okchem.com", "0001", "HR_CREATE,HR_V")))
		.accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.data.myPermissions.length()",is(permissions.size())))
		.andExpect(jsonPath("$.data.myPermissions",hasItem(permissions.get(0))));
	}

	/**
	 * @author Joe
	 * @param roleCode
	 * @param supplierApproverPermissions2
	 * @throws Exception 
	 * @throws JsonProcessingException 
	 */
	private void updatePermissions(String roleCode, List<String> supplierApproverPermissions) throws JsonProcessingException, Exception {
		CommonListForm<String> inListPojo = new CommonListForm<>();
		inListPojo.setList(supplierApproverPermissions);
		this.mvc.perform(put("/uaa/roles/"+roleCode+"/permissions").content(JsonUtils.convertModelToJson(inListPojo))
				.contentType(MediaType.APPLICATION_JSON_UTF8).with(csrf())
				.with(user(createUserDetail("peter", "123", "Joe@okchem.com", "0001", "HR_CREATE,HR_V")))
				.accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json("{\"success\":true}"));
		
	}

	/**
	 * Prepare accessObject, operation, permission. Create 6 permissions
	 */
	private void prepareBaseData() {
		Permission productR = permissionService.createPermission("PRODUCT", "Product", "R", "Read", "Product Read");
		Permission productU = permissionService.createPermission("PRODUCT", "Product", "U", "Update", "Product Update");
		Permission productApprove = permissionService.createPermission("PRODUCT", "Product", "P", "Approve",
				"Product Update");

		Permission srmR = permissionService.createPermission("supplier", "supplier", "R", "Read", "Supplier Read");
		Permission srmU = permissionService.createPermission("supplier", "supplier", "U", "Update", "Supplier Update");
		srmApprove = permissionService.createPermission("supplier", "supplier", "P", "Approve",
				"Supplier Update");

		Set<String> permissionsProductOperator = new HashSet<String>();
		permissionsProductOperator.add(productU.getCode());
		permissionsProductOperator.add(productR.getCode());

		Set<String> permissionsProductAdmin = new HashSet<String>();
		permissionsProductAdmin.addAll(permissionsProductOperator);
		permissionsProductAdmin.add(productApprove.getCode());

		supplierOperatorPermissions.add(srmR.getCode());
		supplierOperatorPermissions.add(srmU.getCode());

		
		supplierApproverPermissions.add(srmApprove.getCode());
		supplierApproverPermissions.addAll(supplierOperatorPermissions);
	}

	public void createRole(String roleCode,String roleName) throws Exception {
		RoleForm roleForm = new RoleForm();
		roleForm.setCode(roleCode);
		roleForm.setName(roleName);
		roleForm.setDescription("Prouct Admin who can do: xx");
		this.mvc.perform(post("/uaa/roles").content(JsonUtils.convertModelToJson(roleForm))
				.contentType(MediaType.APPLICATION_JSON_UTF8).with(csrf())
				.with(user(createUserDetail("peter", "123", "Joe@okchem.com", "0001", "role_insert")))
				.accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json("{\"success\":true}"));
		Assert.assertEquals(roleName, roleRepo.getByCode(roleCode).getName());
	}

	/**
	 * If permissions were already attached to role. Then if user update role's
	 * basic info, permissions should remain the same.
	 * 
	 * @author Joe
	 * @throws Exception
	 */

	public void updateRole(String code, String name, String description) throws Exception {
		RoleForm roleForm = new RoleForm();
		roleForm.setCode(code);
		roleForm.setName(name);
		roleForm.setDescription(description);
		this.mvc.perform(put("/uaa/roles").content(JsonUtils.convertModelToJson(roleForm))
				.contentType(MediaType.APPLICATION_JSON_UTF8).with(csrf())
				.with(user(createUserDetail("peter", "123", "Joe@okchem.com", "0001", "HR_CREATE,HR_V")))
				.accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json("{\"success\":true}"));
		Assert.assertEquals(name, roleRepo.getByCode(code).getName());
	}

}
