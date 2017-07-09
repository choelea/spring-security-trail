/*******************************************************************************
 * Licensed to the OKChem
 *
 * http://www.okchem.com
 *
 *******************************************************************************/
package com.example.security.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * base model ,which is general field for all entity
 * @author Joe
 *
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel implements Serializable
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3126910902448787329L;

	@CreatedDate
	private Long createdStamp;

	
	@LastModifiedDate
	private Long lastUpdatedStamp;

	@Column(length=100)
	@CreatedBy
	private String createdBy;
	
	@Column(length=100)	
	@LastModifiedBy
	private String updatedBy;
	
	/**
	 * @return the createdStamp
	 */
	public Long getCreatedStamp()
	{
		return createdStamp;
	}

	/**
	 * @param createdStamp
	 *           the createdStamp to set
	 */
	public void setCreatedStamp(final Long createdStamp)
	{
		this.createdStamp = createdStamp;
	}

	/**
	 * @return the lastUpdatedStamp
	 */
	public Long getLastUpdatedStamp()
	{
		return lastUpdatedStamp;
	}

	/**
	 * @param lastUpdatedStamp
	 *           the lastUpdatedStamp to set
	 */
	public void setLastUpdatedStamp(final Long lastUpdatedStamp)
	{
		this.lastUpdatedStamp = lastUpdatedStamp;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	
}
