/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 12.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.ucase.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface UCaseMapper {

	public List<Map<String, Object>> selectUCaseList(Map<String, Object> params);
	public int selectUCaseCount(Map<String, Object> params);
	public int selectNewUCaseCount(Map<String, Object> params);
	
	public int upsertUCase(Map<String, Object> params);
}
