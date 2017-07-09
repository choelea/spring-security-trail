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
public class UniqueConstraintException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8810148662663818262L;
	private String msgCode;
	private String arg;


	/**
	 * @param msgCode
	 * @param id
	 */
	public UniqueConstraintException(String arg, String msgCode) {
		super();
		this.msgCode = msgCode;
		this.arg = arg;
	}


	public String getMsgCode() {
		return msgCode;
	}


	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}


	public String getArg() {
		return arg;
	}


	public void setArg(String arg) {
		this.arg = arg;
	}
	
}
