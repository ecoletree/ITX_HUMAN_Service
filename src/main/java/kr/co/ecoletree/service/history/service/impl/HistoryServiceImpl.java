/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.history.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ecoletree.common.base.service.ETBaseService;
import kr.co.ecoletree.common.util.DateUtil;
import kr.co.ecoletree.common.util.PropertyUtil;
import kr.co.ecoletree.common.util.Utility;
import kr.co.ecoletree.service.history.mapper.HistoryMapper;
import kr.co.ecoletree.service.history.service.HistoryService;
import kr.co.ecoletree.service.logHistory.mapper.LogHistoryMapper;

@Service
public class HistoryServiceImpl extends ETBaseService implements HistoryService {

	@Autowired
	HistoryMapper mapper;
	
	@Autowired
	LogHistoryMapper logMapper;
	
	@Override
	public List<Map<String, Object>> selectHistoryHeader() {
		List<Map<String, Object>> list = mapper.selectHistoryHeader();
		return list;
	} 
	
	@Override
	public Map<String, Object> selectHistoryList(Map<String, Object> params) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			int totalCount = mapper.selectHistoryCount(params);
			List<Map<String, Object>> list = mapper.selectHistoryList(params);
			
			String message = getLogParams(params, totalCount);
			params.put("connect_ip", Utility.getIP());
			params.put("log_message", message);
			logMapper.insertLogHistory(params);
			
			resultMap.put("recordsTotal", totalCount);
			resultMap.put("recordsFiltered", totalCount);
			resultMap.put("data", list);
		} catch (Exception e) {
			logError("test",e);
			throw e;
		}
		return resultMap;
	}
	
	/**
	 * 로그 메시지를 만들어서 가져온다
	 * @param params
	 * @param totalCount
	 * @return
	 */
	private String getLogParams(Map<String, Object> params , int totalCount) {
		
        PropertyUtil propertyUtil;
        String msg = null;
		try {
			propertyUtil = PropertyUtil.getInstance();
			msg = propertyUtil.getProperties("log.msg.history_search", "상담내역",params.get("log_tmr_nm"),DateUtil.getCurrentDateByFormat("yyyy.MM.dd HH:mm:ss"),totalCount);
		} catch (IOException e) {
			logError(e);
		}
        
        return msg;
	}
	
	@Override
	public Map<String, Object> selectNotSaveHistoryList(Map<String, Object> params) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			int totalCount = mapper.selectNotSaveHistoryCount(params);
			List<Map<String, Object>> list = mapper.selectNotSaveHistoryList(params);
			
			String message = getLogParams(params, totalCount);
			params.put("connect_ip", Utility.getIP());
			params.put("log_message", message);
			logMapper.insertLogHistory(params);
			
			resultMap.put("recordsTotal", totalCount);
			resultMap.put("recordsFiltered", totalCount);
			resultMap.put("data", list);
		} catch (Exception e) {
			logError("test",e);
			throw e;
		}
		return resultMap;
	}

}
