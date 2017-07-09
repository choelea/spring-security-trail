/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
 *
 * {"responseMessage":"error","errorMessage":"xxxxxx"} {"responseMessage":"success"}
 * @author gordon.peng
 * @name JsonUtils.java
 * @package com.ly.apigateway.utils
 */
@SuppressWarnings("javadoc")
public class JsonUtils
{
	/**
	 * convert Model To Json
	 * @param object
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String convertModelToJson(final Object object) throws JsonProcessingException
	{
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
    
	
}
