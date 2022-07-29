/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.ucase.service;

import java.util.Map;

public interface UCaseService {


	public Map<String, Object> selectUCaseList(Map<String, Object> params);
	public int upsertUCase(Map<String, Object> params);
}
