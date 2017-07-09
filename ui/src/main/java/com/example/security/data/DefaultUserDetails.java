/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.data;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Joe
 *
 */
public class DefaultUserDetails  implements UserDetails{  
	    /**
		 * 
		 */
		private static final long serialVersionUID = 6821636658047604981L;
		private String username;  
	    private String password;  
	    private boolean enabled;  
	    private String email;
	    private String employeeNumber;
	    private String productLines;
	    private String markets;
	    private String firstName;
		private String lastName;
	    private Collection<? extends GrantedAuthority> authorities;  
	      
	    /**
		 * 
		 */
		public DefaultUserDetails() {
			super();
		}

		public DefaultUserDetails(String username, String password, boolean enabled,String email,String employeeNumber) {  
	        super();  
	        this.username = username;  
	        this.password = password;  
	        this.enabled = enabled; 
	        this.email = email;
	        this.employeeNumber = employeeNumber;
	    }  
	      
	    public DefaultUserDetails( String username, String password, boolean enabled, String email,String employeeNumber, 
	            Collection<? extends GrantedAuthority> authorities) {  
	        super();  
	        this.username = username;  
	        this.password = password;  
	        this.enabled = enabled;  
	        this.email = email;
	        this.employeeNumber = employeeNumber;
	        this.authorities = authorities;  
	    }  
	    
	    public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getProductLines() {
			return productLines;
		}

		public void setProductLines(String productLines) {
			this.productLines = productLines;
		}

		public String getMarkets() {
			return markets;
		}

		public void setMarkets(String markets) {
			this.markets = markets;
		}

		@Override  
	    public Collection<? extends GrantedAuthority> getAuthorities() {  
	        return authorities;  
	    }  
	    @Override  
	    public String getPassword() {  
	        return password;  
	    }  
	    @Override  
	    public String getUsername() {  
	        return username;  
	    }  
	    @Override  
	    public boolean isAccountNonExpired() {  
	        return true;  
	    }  
	    @Override  
	    public boolean isAccountNonLocked() {  
	        return true;  
	    }  
	    @Override  
	    public boolean isCredentialsNonExpired() {  
	        return true;  
	    }  
	    @Override  
	    public boolean isEnabled() {  
	        return enabled;  
	    }  
	  
	    public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getEmployeeNumber() {
			return employeeNumber;
		}

		public void setEmployeeNumber(String employeeNumber) {
			this.employeeNumber = employeeNumber;
		}
		public void setUsername(String username) {
			this.username = username;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
			this.authorities = authorities;
		}

	 
	  
	}
