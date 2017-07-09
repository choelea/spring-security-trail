/**
 * 
 */
package com.example.security.data;

/**
 * out paging model
 * @author Joe
 *
 */
public class Paging {
	
	/**
	 * 
	 */
	public Paging() {
		super();
	}
	/**
	 * @param viewIndex
	 * @param viewSize
	 * @param totalSize
	 */
	public Paging(Integer viewIndex, Integer viewSize, Long totalSize) {
		super();
		this.viewIndex = viewIndex;
		this.viewSize = viewSize;
		this.totalSize = totalSize;
	}
	/**
	 * view index
	 */
	private Integer viewIndex;
	/**
	 * view size
	 */
	private Integer viewSize;
	/**
	 * total size
	 */
	private Long totalSize;
	/**
	 * Get viewIndex
	 * @return the viewIndex
	 */
	public Integer getViewIndex() {
		return viewIndex;
	}
	/**
	 * Set viewIndex to the viewIndex
	 * @param viewIndex the viewIndex to set
	 */
	public void setViewIndex(Integer viewIndex) {
		this.viewIndex = viewIndex;
	}
	/**
	 * Get viewSize
	 * @return the viewSize
	 */
	public Integer getViewSize() {
		return viewSize;
	}
	/**
	 * Set viewSize to the viewSize
	 * @param viewSize the viewSize to set
	 */
	public void setViewSize(Integer viewSize) {
		this.viewSize = viewSize;
	}
	/**
	 * Get totalSize
	 * @return the totalSize
	 */
	public Long getTotalSize() {
		return totalSize;
	}
	/**
	 * Set totalSize to the totalSize
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}
	
	

}
