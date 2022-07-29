/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : aileen 
 * Create Date : 2020. 1. 9.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.callInfo.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface CallInfoMapper {

	public Map<String, Object> selectCustInfo(Map<String, Object> param);
	public List<Map<String,Object>> selectTeamList(Map<String, Object> param);
	public List<Map<String,Object>> selectTmrList(Map<String, Object> param);
	
	public int updateCustInfo(Map<String, Object> param);
	public int updateCallInfo(Map<String, Object> param);
	
	public int selectCallResultId(Map<String, Object> param);
	public Map<String, Object> selectCallResultSeq(Map<String, Object> param);
	public int insertCallResult(Map<String, Object> param);
	
	public int updateStateInfo(Map<String, Object> param);
	
	
}
