/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.security.service.ExtUserDetailService;
/**
 * @author Joe
 *
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final String loginURL = "/uaa/login";

	@Autowired
	private ExtUserDetailService userService;

	@Autowired
	PasswordEncoder bcryptEncoder;

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/datasync/uaa/**").permitAll()
								.anyRequest().authenticated()
				.and().formLogin().loginPage(loginURL).permitAll()
				/*  this is not recommended way to logout using GET verb. 
				Please refer to http://docs.spring.io/spring-security/site/docs/current/reference/html/csrf.html#csrf-include-csrf-token
				and https://github.com/spring-projects/spring-boot/issues/1536 */
				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/uaa/logout")).permitAll() 
				.and().exceptionHandling().authenticationEntryPoint(new ExtLoginUrlAuthenticationEntryPoint(loginURL))
				// For rest services, do not use CSRF Protection 
				.and().csrf().ignoringAntMatchers("/uaa/permissions/**","/uaa/users/**", "/uaa/groups/**","/uaa/roles/**").csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bcryptEncoder);
	}

	/**
	 * Customized authenticationEntryPoint. For restful services, if it's anonymous will return 401 instead of redirect to login page.
	 * @author Joe
	 *
	 */
	static class ExtLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint{
		
		/**
		 * @param loginFormUrl
		 */
		public ExtLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
			super(loginFormUrl);
		}

		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException, ServletException {
			if(request.getRequestURI().startsWith("/uaa/users") || request.getRequestURI().startsWith("/uaa/groups") || request.getRequestURI().startsWith("/uaa/roles") || request.getRequestURI().startsWith("/uaa/permissions")){
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
						authException.getMessage());
			}else{
				super.commence(request, response, authException);
			}
			
		}
		
		
	}
}