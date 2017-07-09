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
 * @author Peter.Li
 *
 */
public class GroupMembersData {
	
	private List<Long> groupMembers;
	
	private List<MemberData> allMembers;

	
	/**
	 * 
	 */
	public GroupMembersData() {
		super();
	}

	/**
	 * @return the groupMembers
	 */
	public List<Long> getGroupMembers() {
		return groupMembers;
	}

	/**
	 * @param groupMembers the groupMembers to set
	 */
	public void setGroupMembers(List<Long> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public List<MemberData> getAllMembers() {
		return allMembers;
	}

	public void setAllMembers(List<MemberData> allMembers) {
		this.allMembers = allMembers;
	}

	
}
