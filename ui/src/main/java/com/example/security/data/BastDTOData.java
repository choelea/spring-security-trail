/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.data;

/**
 * @author Joe
 *
 */
public class BastDTOData {
	private String msg;
	private String msgCode;
	private boolean success = true;
	
	/**
	 * 
	 */
	public BastDTOData() {
		super();
	}
	/**
	 * @param msg
	 * @param msgCode
	 * @param success
	 */
	public BastDTOData(String msg, String msgCode, boolean success) {
		super();
		this.msg = msg;
		this.msgCode = msgCode;
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
}
