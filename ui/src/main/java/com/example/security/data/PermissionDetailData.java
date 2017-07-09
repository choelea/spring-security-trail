/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.data;

import java.util.List;

/**
 * @author Joe
 *
 */
public class PermissionDetailData {
	private String objectName;
	private List<OperationData> operationDatas;
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public List<OperationData> getOperationDatas() {
		return operationDatas;
	}
	public void setOperationDatas(List<OperationData> operationDatas) {
		this.operationDatas = operationDatas;
	}
	
	
}
