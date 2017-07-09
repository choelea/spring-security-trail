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
/**
 * @author Joe
 *
 * @param <T>
 */
public class CommonData<T> extends BastDTOData{
	private T data;
	
	public CommonData(){
		super();
	}
	
	public CommonData(T data){
		super();
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
