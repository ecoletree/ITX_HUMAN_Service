/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 12.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.callback.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface CallBackMapper {

	public List<Map<String, Object>> selectNewCallBackList(Map<String, Object> params);
	
	public int selectNewCallBackCount(Map<String, Object> params);
	
	public int updateOutboundCallBack(Map<String, Object> params);
}
