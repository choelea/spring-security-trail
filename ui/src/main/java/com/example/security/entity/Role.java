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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author Joe
 *
 */
@Entity
@Table(name = "t_role")
public class Role extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2718078768790684902L;

	@Id
	@Column(length = 30, unique = true)
	private String code;

	@Column(length = 60)
	private String name;


	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST, mappedBy="roles")
	private Set<User> users;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	private Set<Group> groups;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy="roles")
	private Set<Session> sessions;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "t_role_permission",
	    joinColumns = {@JoinColumn(name = "role", referencedColumnName = "code")},
	    inverseJoinColumns = {@JoinColumn(name = "permission", referencedColumnName ="code")})
	private Set<Permission> permissions;
	
	@Column(length = 255)
	private String description;

	@OneToOne(mappedBy="role", cascade=CascadeType.ALL)
	private RolePolicy rolePolicy;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public RolePolicy getRolePolicy() {
		return rolePolicy;
	}

	public void setRolePolicy(RolePolicy rolePolicy) {
		this.rolePolicy = rolePolicy;
	}

	public Set<Session> getSessions() {
		return sessions;
	}

	public void setSessions(Set<Session> sessions) {
		this.sessions = sessions;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	 
}
