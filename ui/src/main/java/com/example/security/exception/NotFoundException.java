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
public class NotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3530600518819211178L;
	private String msgCode;
	private String id;


	/**
	 * @param msgCode
	 * @param id
	 */
	public NotFoundException(String id, String msgCode) {
		super();
		this.msgCode = msgCode;
		this.id = id;
	}


	public String getMsgCode() {
		return msgCode;
	}


	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
}
