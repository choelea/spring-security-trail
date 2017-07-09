/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.security.data.DefaultUserDetails;

/**
 * @author Joe
 * Add jpa audit support. http://docs.spring.io/spring-data/jpa/docs/1.10.4.RELEASE/reference/html/#jpa.auditing
 * to transparently keep track of who created or changed an entity and the point in time this happened. 
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class JpaAuditConfig {
	@Bean
	public AuditorAware<String> auditorProvider() {
		return new SpringSecurityAuditorAware();
	}

	static class SpringSecurityAuditorAware implements AuditorAware<String> {

		public String getCurrentAuditor() {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof DefaultUserDetails)) {
				return null;
			}
			
			return ((DefaultUserDetails) authentication.getPrincipal()).getEmployeeNumber();
		}
	}
}
