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
public abstract class AbstractException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2384442342474305198L;
	private String msgCode;
	private String msg;


	/**
	 * @param msgCode
	 * @param id
	 */
	public AbstractException(String msgCode,String msg) {
		super();
		this.msgCode = msgCode;
		this.msg = msg;
	}


	public String getMsgCode() {
		return msgCode;
	}


	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}	
}
