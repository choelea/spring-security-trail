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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

import com.example.annotation.WithMockDefaultUserDetail;
import com.example.security.entity.Group;
import com.example.security.form.GroupBasicForm;
import com.example.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
/**
 * add group test
 * @author Joe
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test") 
public class GroupControllerTests extends AbstractTest{
	
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
	}
	
	
	/**
	 * test normal data
	 * 
	 * @author Joe
	 * @throws Exception
	 */
	@Test
	@Transactional
	@WithMockDefaultUserDetail(username="peter",employeeNo="00000",authorities={"group_insert,group_edit"})
    public void addGroupSuccess() throws Exception {
		GroupBasicForm inGroup = new GroupBasicForm();
		inGroup.setGroupName("Admin2");
		inGroup.setDescription("This is description2");
		this.mvc.perform(post("/uaa/groups")
				.content(JsonUtils.convertModelToJson(inGroup))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(content().json("{\"success\":true}"));
    }

	
	/**
	 * 
	 * test same groupName
	 * 
	 * @author Joe
	 * @throws JsonProcessingException
	 */
	@Test
	@Transactional
    public void addGroupFailed() throws Exception {
		createGroup("Admin","group1","Group for test");	
		GroupBasicForm inGroup = new GroupBasicForm();
		inGroup.setGroupName("Admin");
		inGroup.setDescription("This is description");
		this.mvc.perform(post("/uaa/groups")
				.content(JsonUtils.convertModelToJson(inGroup))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user(createUserDetail("peter", "123", "Joe@okchem.com", "0001", "HR_CREATE,HR_V"))).accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(content().json("{\"success\":false}"));
    }
	
	/**
	 * 
	 * test normal data
	 * 
	 * @author JOe
	 * @throws JsonProcessingException
	 */
	@Test
	@Transactional
    public void getGroupDetail() throws Exception{
		Group group = createGroup("Admin","group1","Group for test");	
		this.mvc.perform(get("/uaa/groups/"+group.getId())
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user("admin").password("pass").roles("USER","ADMIN")).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.data.groupName", is("Admin")));
    }
	
	
	/**
	 * 
	 * test normal data
	 * 
	 * @author Joe
	 * @throws JsonProcessingException
	 */
	@Test
	@Transactional
    public void deleteGroupSuccessful() throws Exception{
		Group group = createGroup("group1","group1","Group for test");	
		this.mvc.perform(delete("/uaa/groups/"+group.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user("admin").password("pass").roles("USER","ADMIN")).accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(content().json("{\"success\":true}"));
    }
	
	/**
	 * 
	 * test not exist data
	 * 
	 * @author Joe
	 * @throws JsonProcessingException
	 */
	@Test
	@Transactional
    public void deleteGroupFailed() throws Exception{
		this.mvc.perform(delete("/uaa/groups/0")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user("admin").password("pass").roles("USER","ADMIN")).accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(content().json("{\"success\":false}"));
    }
	
	@Test
	@Transactional
    public void updateGroupSuccess() throws Exception{
		GroupBasicForm inGroup = new GroupBasicForm();
		inGroup.setGroupName("Admin2");
		inGroup.setDescription("This is description2");		
		Group group = createGroup("group1","group1","Group for test");		
		this.mvc.perform(put("/uaa/groups/"+group.getId())
				.content(JsonUtils.convertModelToJson(inGroup))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user("admin").password("pass").roles("USER","ADMIN")).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("{\"success\":true}"));
    }
	
	/**
	 * Verify getGroup list.
	 * Verify result using jsonpath expression:https://github.com/jayway/JsonPath
	 * @throws Exception
	 */
	@Test
	@Transactional
    public void getGroupList() throws Exception{
		createGroup("group1","group1","Group for test");	
		createGroup("group2","group2","Group for test");	
		this.mvc.perform(get("/uaa/groups?viewIndex=1&viewSize=10")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf()).with(user("admin").password("pass").roles("USER","ADMIN")).accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.data.paging.totalSize", is(2)));
    }
		
}
