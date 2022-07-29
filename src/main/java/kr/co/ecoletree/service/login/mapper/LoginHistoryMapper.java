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
public interface LoginHistoryMapper {

	public int insertLoginHistory(Map<String, Object> param);
	
	public Map<String, Object> selectLastLoginHistory(Map<String, Object> param);
}
