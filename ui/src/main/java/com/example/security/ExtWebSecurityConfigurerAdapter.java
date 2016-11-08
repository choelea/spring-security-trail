/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Joe
 *
 */
@EnableWebSecurity
public class ExtWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {


 	@Override
 	public void configure(WebSecurity web) throws Exception {
 		web.ignoring()
 		// Spring Security should completely ignore URLs starting with /resources/
 				.antMatchers("/resources/**");
 	}

 	@Override
 	protected void configure(HttpSecurity http) throws Exception {
 		http.authorizeRequests()
 				.antMatchers("/public/**").permitAll()
 				.antMatchers("/admin/**").hasRole("ADMIN")
 				.and()
 				// Possibly more configuration ...
 				.formLogin() // enable form based log in
 				// set permitAll for all URLs associated with Form Login
 				.permitAll();
 	}

 	@Override
 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 		// enable in memory based authentication with a user named "user" and "admin"
 		auth.inMemoryAuthentication().withUser("user").password("password").roles("USER")
 				.and().withUser("admin").password("password").roles("USER", "ADMIN");
 	}

 	// Possibly more overridden methods ...
 
}
