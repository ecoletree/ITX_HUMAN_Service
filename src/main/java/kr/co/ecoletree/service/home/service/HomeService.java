/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.home.service;

import java.util.List;
import java.util.Map;

public interface HomeService {

	
	public Map<String, Object> selectCodeList(Map<String, Object> params);
	
	public Map<String, Object> selectCallBackNoticeCount(Map<String, Object> params);
	
	public Map<String, Object> selectNoticeList(Map<String, Object> params);
	
	public Map<String, Object> selectCallBackList(Map<String, Object> params);
	
	public Map<String, Object> setCustCallInfo(Map<String, Object> params);
	
	public boolean insertNoticeRead(Map<String, Object> params);
	
	public List<Map<String, Object>> selectScriptList(Map<String, Object> params);
	
	public Map<String, Object> selectDBDate();
	
	public List<Map<String, Object>> selectCTMRList(Map<String, Object> params);
	
	public boolean setTMRStatus(Map<String, Object> params);
	
	//////////////아웃바운드 관련 /////////////////
	/**
	* 자신이 속한 캠페인 목록을 가져오도록 함
	* @param params
	* @return
	*/
	public Map<String, Object> getCodeAndMyCamp(Map<String, Object> params);

	/**
	 * 선택한 캠페인의 DB 레이아웃 정보를 가져옴
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getDBLayoutByCompaign(Map<String, Object> params);
	
	/**
	 * 자신이 속한 DB 목록을 가져오도록 함
	 * @param params
	 * @return
	 */
	public Map<String, Object> getMyDBList(Map<String, Object> params) throws Exception;
	
	/**
	 * 선택한 DB의 상세 정보를 검색
	 * @param params
	 * @return
	 */
	public Map<String, Object> getDBDetailInfo(Map<String, Object> params);
}
