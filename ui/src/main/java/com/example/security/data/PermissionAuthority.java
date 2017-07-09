/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.data;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Joe
 *
 */
public class PermissionAuthority implements GrantedAuthority{

	private String permissionCode;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1051760014006812763L;

	
	/**
	 * @param permissionCode
	 */
	public PermissionAuthority(String permissionCode) {
		super();
		this.permissionCode = permissionCode;
	}


	/* (non-Javadoc)
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return permissionCode;
	}

}
