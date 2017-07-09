/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.security.data.BastDTOData;
import com.example.security.data.CommonData;
import com.example.security.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**Base Controller is for each controller to extend in Sourcing,
 * provides base success and error response
 *
 * MessageLabel provides methods for getting i18n label
 * @author Joe
 * 2016/5/5
 */
@ControllerAdvice
public abstract class BaseController {
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	
	protected BastDTOData getSuccessResponse()
	{
		return new BastDTOData();
	}
	
	protected <T> CommonData<T> getSuccessResponse(T data)
	{
		return new CommonData<T>(data);
	}
	
	protected BastDTOData success()
	{
		return new BastDTOData();
	}
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    @ResponseBody
    public BastDTOData notFoundError(BadRequestException e,HttpSession session) {
    	BastDTOData baseData = new BastDTOData();
    	baseData.setMsgCode(e.getMsgCode());
    	baseData.setMsg(e.getMsg());
    	baseData.setSuccess(false);
    	return baseData;
    }
}
