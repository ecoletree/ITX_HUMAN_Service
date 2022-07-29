/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : aileen 
 * Create Date : 2020. 1. 13.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.historyDetail.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface HistoryDetailMapper {

	int updateCallInfo(Map<String, Object> param);
	int updateCallResult(Map<String, Object> param);
	
	public int updateStateInfo(Map<String, Object> param);
}
