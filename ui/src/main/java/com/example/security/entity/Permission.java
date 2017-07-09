/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 
 * @author Joe
 *
 */
@Entity
@Table(name = "t_permission")
public class Permission extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2718078768790684902L;

	@Id	
	@Column(length=30, unique=true)
	private String code;
	
	@Column(length=60)
	private String name;
	
		
	@Column(length=255)
	private String description;
	

	@ManyToMany(fetch = FetchType.LAZY, mappedBy="permissions")
	private Set<Role> roles;

	@ManyToOne
	@JoinColumn(name="object")
	private AccessObject object;
	
	@ManyToOne
	@JoinColumn(name="operation")
	private Operation operation;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code.toLowerCase();
	}
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public AccessObject getObject() {
		return object;
	}

	public void setObject(AccessObject object) {
		this.object = object;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	
	
}
