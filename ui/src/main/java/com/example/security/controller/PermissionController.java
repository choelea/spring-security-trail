/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.data.CommonData;
import com.example.security.data.PermissionDetailData;
import com.example.security.service.PermissionService;

/**
 * 
 * @author Joe.Li
 *
 */
@RestController
@RequestMapping("/uaa/permissions")
public class PermissionController extends BaseController {
	
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<CommonData<List<PermissionDetailData>>> get() {
		List<PermissionDetailData> list = permissionService.getAllPermissions();
		return ResponseEntity.ok(getSuccessResponse(list));
	}

}
