/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.base.vo ETBaseVO.java
 *****************************************************************/
package kr.co.ecoletree.common.base.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author dongsuk
 *
 */
public class ETBaseVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1349199237392777594L;
	
	/** 결과 값 순서 */
	private Long rowNum;
	
	/** 생성자 ID */
	private String createId;
	
	/** 생성일자 */
	private Date createDate;
	
	/** 변경자 ID */
	private String updateId;
	
	/** 변경일자 */
	private Date updateDate;
	
	/**
	 * 아파트 ID setter
	 * @param rowNum as String
	 */
	public void setRowNum(Long rowNum) {
		this.rowNum = rowNum;
	}
	
	/**
	 * 아파트 ID getter
	 * @return Long
	 */
	public Long getRowNum() {
		return rowNum;
	}
	
	/**
	 * 생성자 ID setter
	 * @param createId as String
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	
	/**
	 * 생성자 ID getter
	 * @return String
	 */
	public String getCreateId() {
		return createId;
	}
	
	/**
	 * 생성일자 setter
	 * @param createDate as Date
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	/**
	 * 생성일자 getter
	 * @return Date
	 */
	public Date getCreateDate() {
		return createDate;
	}
	
	/**
	 * 변경자 ID setter
	 * @param updateId as String
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	
	/**
	 * 변경자 ID getter
	 * @return String
	 */
	public String getUpdateId() {
		return updateId;
	}
	
	/**
	 * 변경일자 setter
	 * @param updateDate as Date
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	/**
	 * 변경일자 getter
	 * @return Date
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
