/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 12.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.home.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface HomeMapper {

	public Map<String, Object> selectCustId (Map<String, Object> params);
	
	public String selectNewCustId (Map<String, Object> params);
	
	public int selectCallInfoCheck (Map<String, Object> params);
	
	public int insertCustomer (Map<String, Object> params);
	
	public int insertCallInfo (Map<String, Object> params);
	
	public List<Map<String, Object>> selectTeamList(Map<String, Object> params);

	public List<Map<String, Object>> selectScriptList(Map<String, Object> params);
	
	public Map<String, Object> selectDBDate();
	
	public List<Map<String, Object>> selectCTMRList(Map<String, Object> params);
	
	public int insertCTIStatus (Map<String, Object> params);
	
	public int insertCTIStatusHistory (Map<String, Object> params);
	
	////////////// 아웃바운드 관련 /////////////////
	
	/**
	 * DB 상태에 표시할 code 정보 가져오기
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectOutboundCodeList(Map<String, Object> params);
	/**
	 * 자신이 속한 캠페인 목록을 가져오도록 함
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectMyCampaignList(Map<String, Object> params);
	
	/**
	 * 선택한 캠페인의 DB 레이아웃 정보를 가져옴
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectDBLayoutByCompaign(Map<String, Object> params);

	/**
	 * 자신이 속한 DB 목록의 전체 카운트 가져오도록 함
	 * @param params
	 * @return
	 */
	public int selectMyDBListCount(Map<String, Object> params);

	/**
	 * 자신이 속한 DB 목록을 가져오도록 함
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectMyDBList(Map<String, Object> params);
	
	/**
	 * 선택한 DB의 상세 정보를 가져오도록 함
	 * @param params
	 * @return
	 */
	public Map<String, Object> selectDBDetailInfo(Map<String, Object> params);
}
