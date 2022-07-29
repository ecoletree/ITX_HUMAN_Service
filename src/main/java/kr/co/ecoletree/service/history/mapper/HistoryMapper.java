/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 12.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.history.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface HistoryMapper {

	public List<Map<String, Object>> selectHistoryList(Map<String, Object> params);
	public int selectHistoryCount(Map<String, Object> params);
	public List<Map<String, Object>> selectHistoryHeader();
	public List<Map<String, Object>> selectNotSaveHistoryList(Map<String, Object> params);
	public int selectNotSaveHistoryCount(Map<String, Object> params);
}
