/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

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
import com.example.security.entity.User;
import com.example.security.form.CommonListForm;
import com.example.utils.JsonUtils;
/**
 * Add group test
 * @author Joe
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test") 
public class GroupMembersIntegrationTest extends AbstractTest{
	
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	
	
	
	
	
	private User groupUser1;
	private User groupUser2;
	
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
	 * Step2: select members for group
	 * Step3: update group members
	 * Step4: get group members
	 * @throws Exception 
	 */
	@Test
	@Transactional
	public void integrationTestCase() throws Exception{
		Group group = createGroup("productsuper","Product Super group","Group for Product Super manager.");
		CommonListForm<Long> memberIdList  = prepareInPojoForUpdate();
		updateGroupMembers(group.getId(), memberIdList);
		getGroupMembers(group.getId());
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
	private void prepareBaseData(){
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
     * @throws Exception
     */
	private void getGroupMembers(Long groupId) throws Exception{
		this.mvc.perform(get("/uaa/groups/"+groupId+"/members")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("joe", "asdff", "joe.lea@gmail.com", "11111112222333", "PLM,SRM"))).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.success",is(true)))
				.andExpect(jsonPath("$.data.groupMembers.length()",is(2)))
				.andExpect(jsonPath("$.data.allMembers.length()",is(4) ));
	}	
}
