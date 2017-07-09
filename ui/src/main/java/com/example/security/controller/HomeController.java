/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.security.data.DefaultUserDetails;
/**
 * 
 * @author Joe
 *
 */
@Controller
@RequestMapping("/")
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/uaa/me", method = RequestMethod.GET)
	public ResponseEntity<DefaultUserDetails> user(Authentication authentication,HttpSession session)
	{
    	DefaultUserDetails userdetails = (DefaultUserDetails)authentication.getPrincipal();
    	LOGGER.info("principal class is:"+ userdetails.getClass());
		return ResponseEntity.ok(userdetails);
	}
}
