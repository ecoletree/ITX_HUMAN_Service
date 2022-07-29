/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : aileen 
 * Create Date : 2020. 1. 13.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.historyDetail.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author aileen
 *
 */
public interface HistoryDetailService {

	/**
	 * 이전 상담 이력에서 상담내용 저장
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	Map<String, Object> saveCallInfo(Map<String, Object> param, HttpServletRequest request);
	
	/**
	 * 이전 상담 이력 상세보기 팝업에서 상담내용 + 예약콜(예약콜인 경우) 저장
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	Map<String, Object> saveHistoryDetail(Map<String, Object> param, HttpServletRequest request);
}
