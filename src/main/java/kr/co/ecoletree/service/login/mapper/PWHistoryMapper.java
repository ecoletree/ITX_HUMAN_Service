/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : aileen 
 * Create Date : 2019. 12. 31.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.login.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface PWHistoryMapper {

	public int insertPWHistory(Map<String, Object> param);
	
	public int selectLastPWHistory(Map<String, Object> param);
}
