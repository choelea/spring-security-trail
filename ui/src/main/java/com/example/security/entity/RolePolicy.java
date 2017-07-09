/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
/**
 * 
 * @author Joe
 *
 */
@Entity
@Table(name = "t_policy")
public class RolePolicy extends BaseModel{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4751077075980116089L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String description;
	
	@OneToOne
	@JoinColumn(name="role")
	private Role role;
	
	@Column(length=255)
	private String ruleSnippet;
	
	private Long activeDate;
	
	private Long expireDate;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Long activeDate) {
		this.activeDate = activeDate;
	}

	public Long getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Long expireDate) {
		this.expireDate = expireDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getRuleSnippet() {
		return ruleSnippet;
	}

	public void setRuleSnippet(String ruleSnippet) {
		this.ruleSnippet = ruleSnippet;
	}
	
	
}
