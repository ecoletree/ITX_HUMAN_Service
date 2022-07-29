/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : aileen 
 * Create Date : 2020. 1. 9.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.callInfo.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author aileen
 *
 */
public interface CallInfoSerivce {

	/**
	 * 고객 데이터를 조회
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	public Map<String, Object> selectCustInfo(Map<String, Object> param, HttpServletRequest request);

	/**
	 * 고객정보 저장
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	public Map<String, Object> saveCustInfo(Map<String, Object> param, HttpServletRequest request);
	
	/**
	 * 고객정보 + 상담정보 + 예약정보(예약콜인 경우) 저장
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	public Map<String, Object> saveCallInfo(Map<String, Object> param, HttpServletRequest request);
	
	/**
	 * 악성 민원인 저장
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	public Map<String, Object> saveBlackList(Map<String, Object> param, HttpServletRequest request);
	
	/**
	 * 악성 민원인 코드가져오기
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> selectBlackCodeList();
	
}
