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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Joe
 *
 */
@Entity
@Table(name = "t_operation")
public class Operation extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7792705688424777707L;

	@Id	
	@Column(length=15, unique=true)
	private String code;
	
	@Column(length=50)
	private String name;
	
	@Column(length=255)
	private String description;
	
	@OneToMany(mappedBy = "operation", cascade = CascadeType.ALL)
	private Set<Permission> permissions;
	
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

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
}
