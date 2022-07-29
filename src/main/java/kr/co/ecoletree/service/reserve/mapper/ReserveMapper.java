/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 12.
 * DESC : 예약 건수
*****************************************************************/
package kr.co.ecoletree.service.reserve.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ReserveMapper {

	public List<Map<String, Object>> selectNewReserveList(Map<String, Object> params);
	public int selectNewReserveCount(Map<String, Object> params);
	
	
	public Map<String,Object> selectCallReserved(Map<String, Object> param);
	public int insertCallReserved(Map<String, Object> param);
	public int updateCallReserved(Map<String, Object> param);
	
	public int updateOutboundCallReserved(Map<String, Object> param);
}
