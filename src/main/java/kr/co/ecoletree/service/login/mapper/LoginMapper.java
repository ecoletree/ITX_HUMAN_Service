/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 12.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.login.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface LoginMapper {

	public Map<String, Object> selectLoginInfo(Map<String, Object> param);
	
	public int updateLoginFailCount(Map<String, Object> param);
	public int updateLoginPW(Map<String, Object> param);
}
