/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.exception;

/**
 * Not found exception
 * @author Joe
 *
 * 2016/5/13
 */
public class BadRequestException extends AbstractException {

	


	/**
	 * 
	 */
	private static final long serialVersionUID = -5117299421142773386L;

	/**
	 * @param msgCode
	 * @param id
	 */
	public BadRequestException(String msgCode,String msg) {
		super(msgCode,msg);
	}


}
