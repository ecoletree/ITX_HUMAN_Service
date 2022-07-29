/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.history.service;

import java.util.List;
import java.util.Map;

public interface HistoryService {

	
	public Map<String, Object> selectHistoryList(Map<String, Object> params);
	public Map<String, Object> selectNotSaveHistoryList(Map<String, Object> params);
	
	public List<Map<String, Object>> selectHistoryHeader();
}
