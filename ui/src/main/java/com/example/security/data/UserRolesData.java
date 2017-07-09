/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.data;

import java.util.List;
import java.util.Set;

/**
 * out group members model
 * 
 * @author Joe
 *
 */
public class UserRolesData {

	private Set<String> userRoles;

	private Set<String> groupRoles;

	private List<RoleData> allRoles;

	/**
	 * @param userRoles
	 * @param groupRoles
	 * @param allRoles
	 */
	public UserRolesData(Set<String> userRoles, Set<String> groupRoles, List<RoleData> allRoles) {
		super();
		this.userRoles = userRoles;
		this.groupRoles = groupRoles;
		this.allRoles = allRoles;
	}

	public Set<String> getUserRoles() {
		return userRoles;
	}



	public Set<String> getGroupRoles() {
		return groupRoles;
	}


	public List<RoleData> getAllRoles() {
		return allRoles;
	}




}
