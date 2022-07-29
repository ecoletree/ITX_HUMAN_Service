/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 12.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.logHistory.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface LogHistoryMapper {

	public int insertLogHistory(Map<String, Object> params);
}
