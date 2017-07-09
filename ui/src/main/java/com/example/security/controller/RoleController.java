/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.data.BastDTOData;
import com.example.security.data.CommonData;
import com.example.security.data.PaginatedListData;
import com.example.security.data.RoleData;
import com.example.security.data.RoleDetailData;
import com.example.security.form.CommonListForm;
import com.example.security.form.RoleForm;
import com.example.security.service.RoleService;

/**
 * 
 * @author Joe.Li
 *
 */
@RestController
@RequestMapping("/uaa/roles")
public class RoleController extends BaseController {
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<CommonData<PaginatedListData<RoleData>>> get(
			@RequestParam(value = "viewIndex", required = false) Integer viewIndex,
			@RequestParam(value = "viewSize", required = false) Integer viewSize,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "objectCode", required = false) String objectCode) {
		PaginatedListData<RoleData> list = roleService.getRoles(viewIndex, viewSize, name, objectCode);
		return ResponseEntity.ok(getSuccessResponse(list));
	}
	@RequestMapping(value="/{code}",method=RequestMethod.GET)
	public ResponseEntity<CommonData<RoleData>> get(@PathVariable String code){
		return ResponseEntity.ok(getSuccessResponse(roleService.getBasic(code)));
	}
	@RequestMapping(method=RequestMethod.POST)
	@PreAuthorize("hasAuthority('role_insert')")
	public ResponseEntity<BastDTOData> create(@Valid @RequestBody final RoleForm roleForm){
		roleService.create(roleForm.getCode(),roleForm.getName(),roleForm.getDescription(),null);
		return ResponseEntity.ok(success());
	}	
	/**
	 * Update role's basic info: name, description
	 * @author Joe
	 * @param roleForm
	 * @return
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<BastDTOData> update(@Valid @RequestBody final RoleForm roleForm){
		roleService.update(roleForm.getCode(),roleForm.getName(),roleForm.getDescription());
		return ResponseEntity.ok(success());
	}
	
	@RequestMapping(value="/{code}",method=RequestMethod.DELETE)
	public ResponseEntity<BastDTOData> delete(@PathVariable String code){
		roleService.delete(code);
		return ResponseEntity.ok(success());
	}
	
	@RequestMapping(value="/{code}/permissions",method=RequestMethod.GET)
	public ResponseEntity<CommonData<RoleDetailData>> getPermissions(@PathVariable String code){
		return ResponseEntity.ok(getSuccessResponse(roleService.getDetail(code)));
	}
	@RequestMapping(value="/{code}/permissions",method=RequestMethod.PUT)
	public ResponseEntity<BastDTOData> updatePermissions(@PathVariable String code, @RequestBody final CommonListForm<String> permissions){
		roleService.updatePermissions(code,permissions.getList());
		return ResponseEntity.ok(success());
	}
}
