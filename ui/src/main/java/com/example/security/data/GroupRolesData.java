/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.data;

import java.util.List;

/**
 * out group members model
 * @author Joe
 *
 */
public class GroupRolesData {
	
	private List<String> groupRoles;
	
	private List<RoleData> allRoles;

	/**
	 * @param groupRoles
	 * @param allRoles
	 */
	public GroupRolesData(List<String> groupRoles, List<RoleData> allRoles) {
		super();
		this.groupRoles = groupRoles;
		this.allRoles = allRoles;
	}

	public List<String> getGroupRoles() {
		return groupRoles;
	}

	public void setGroupRoles(List<String> groupRoles) {
		this.groupRoles = groupRoles;
	}

	public List<RoleData> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(List<RoleData> allRoles) {
		this.allRoles = allRoles;
	}


	

}
