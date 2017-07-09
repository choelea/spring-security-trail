/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.annotation;

/**
 * @author Joe
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.StringUtils;

import com.example.security.data.PermissionAuthority;
import com.example.security.data.DefaultUserDetails;


/**
 * A {@link WithUserDetailsSecurityContextFactory} that works with {@link WithMockUser}.
 *
 * @author Rob Winch
 * @since 4.0
 * @see WithMockUser
 */
final class WithMockDefaultUserDetailFactory implements
		WithSecurityContextFactory<WithMockDefaultUserDetail> {

	public SecurityContext createSecurityContext(WithMockDefaultUserDetail withUser) {
		String username = StringUtils.hasLength(withUser.username()) ? withUser
				.username() : withUser.value();
		if (username == null) {
			throw new IllegalArgumentException(withUser
					+ " cannot have null username on both username and value properites");
		}

		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for (String authority : withUser.authorities()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority));
		}

		if(grantedAuthorities.isEmpty()) {
			for (String role : withUser.roles()) {
				if (role.startsWith("ROLE_")) {
					throw new IllegalArgumentException("roles cannot start with ROLE_ Got "
							+ role);
				}
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
		} else if(!(withUser.roles().length == 1 && "USER".equals(withUser.roles()[0]))) {
			throw new IllegalStateException("You cannot define roles attribute "+ Arrays.asList(withUser.roles())+" with authorities attribute "+ Arrays.asList(withUser.authorities()));
		}

		
		DefaultUserDetails principal = new DefaultUserDetails(username, withUser.password(),true,withUser.email(),withUser.employeeNo(),authorities(withUser.authorities()));
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				principal, principal.getPassword(), principal.getAuthorities());
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		return context;
	}
	
	private List<PermissionAuthority> authorities(String[] authorities){
		List<PermissionAuthority> list = new ArrayList<PermissionAuthority>();
		if(authorities!=null){
			for (String string : authorities) {
				list.add(new PermissionAuthority(string));
			}
		}
		return list;
	}
}
