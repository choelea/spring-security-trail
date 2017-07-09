/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.data;

import java.util.List;

/**
 * @author Joe
 *
 */
public class RoleDetailData {
	private String code;
	private String name;
	private String description;
	private List<PermissionDetailData> allPermissions;
	private List<String> myPermissions;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<PermissionDetailData> getAllPermissions() {
		return allPermissions;
	}
	public void setAllPermissions(List<PermissionDetailData> allPermissions) {
		this.allPermissions = allPermissions;
	}
	public List<String> getMyPermissions() {
		return myPermissions;
	}
	public void setMyPermissions(List<String> myPermissions) {
		this.myPermissions = myPermissions;
	}
	
	
}
