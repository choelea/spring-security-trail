/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 
 * @author Joe
 *
 */
@Entity
@Table(name = "t_group")
public class Group extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 554825658077688779L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 30, unique = true)
	private String code;

	@Column(length = 60)
	private String groupName;

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "t_group_user", 
		joinColumns = {@JoinColumn(name = "groupId", referencedColumnName = "id") }, 
		inverseJoinColumns = {@JoinColumn(name = "user", referencedColumnName = "id") })
	private Set<User> users;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "t_group_role",
	    joinColumns = {@JoinColumn(name = "groupId", referencedColumnName = "id")},
	    inverseJoinColumns = {@JoinColumn(name = "role", referencedColumnName ="code")})	
	private Set<Role> roles;

	@Column(length = 255)
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<User> getUsers() {
		return users;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code.toLowerCase();
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
