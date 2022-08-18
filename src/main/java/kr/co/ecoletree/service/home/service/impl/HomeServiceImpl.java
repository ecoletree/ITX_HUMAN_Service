/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.home.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.ecoletree.common.base.service.ETBaseService;
import kr.co.ecoletree.common.util.PropertyUtil;
import kr.co.ecoletree.common.util.StringUtil;
import kr.co.ecoletree.common.util.TreeUtil;
import kr.co.ecoletree.service.callInfo.mapper.BlackListMapper;
import kr.co.ecoletree.service.callback.mapper.CallBackMapper;
import kr.co.ecoletree.service.code.mapper.CodeMapper;
import kr.co.ecoletree.service.common.ServiceCommonConst;
import kr.co.ecoletree.service.home.mapper.HomeMapper;
import kr.co.ecoletree.service.home.service.HomeService;
import kr.co.ecoletree.service.notice.mapper.NoticeMapper;
import kr.co.ecoletree.service.notice.mapper.NoticeReadMapper;
import kr.co.ecoletree.service.ucase.mapper.UCaseMapper;

@Service
public class HomeServiceImpl extends ETBaseService implements HomeService {

	@Autowired
	NoticeMapper noticeMapper;
	
	@Autowired
	NoticeReadMapper noticeReadMapper;
	
	@Autowired
	CodeMapper codeMapper;
	
	@Autowired
	HomeMapper mapper;
	
	@Autowired
	CallBackMapper callBackMapper;
	
	@Autowired
	UCaseMapper ucaseMapper;
	
	@Autowired
	BlackListMapper blackListMapper;
	
	@Override
	public Map<String, Object> selectCallBackNoticeCount(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			int noticeCount = noticeMapper.selectNewNoticeCount(params);
			int callbackCount = callBackMapper.selectNewCallBackCount(params);
			int ucaseCount = 0;
			int blackListCount = 0;
			
			resultMap.put("noticeCount", noticeCount);
			resultMap.put("callbackCount", callbackCount);
//			resultMap.put("callbackCount", 0);
			
			PropertyUtil propertyUtil;
	        String msg = null;
			try {
				propertyUtil = PropertyUtil.getInstance();
				msg = propertyUtil.getProperties("manager.tmr.id");
//				if (-1 < msg.indexOf(SessionHelper.getTmrId())) {
//					ucaseCount = ucaseMapper.selectNewUCaseCount(params);
//					blackListCount = blackListMapper.selectBlackListCount(params);
//				}
			} catch (IOException e) {
				logError(e.getMessage(),e);
			}
			
			resultMap.put("ucaseCount", ucaseCount);
			resultMap.put("blackListCount", blackListCount);
		} catch (Exception e) {
			logError(e);
			throw e;
		}
		
		
		return resultMap;
	}
	
	@Override
	@Transactional
	public Map<String, Object> setCustCallInfo(Map<String, Object> params) {
		try {
			// 걸려온 전화 번호로 고객번호 채번
//			Map<String, Object> custIdMap = mapper.selectCustId(params);
//			
//			if (custIdMap == null || custIdMap.get("cust_id") == null) { // 이전에 등록 한 고객번호가 없을 경우
//				params.put("cust_id", null);
//				setNoneCustID(params);
//			} else { // 이전에 등록 한 고객번호가 있을 경우
//				params.put("cust_id", custIdMap.get("cust_id"));
//				setHaveCustID(params);
//			}
			// 이번에 고객정보 조회가 생기면서 바뀜
			if (params.get("hand_tel") != null) {
				String callId = StringUtil.getUUID(ServiceCommonConst.CODE_PRIFIX_CALL);
				params.put("call_id", callId);
				if (params.get("io_flag").equals(ServiceCommonConst.BIZ_TYPE_INBOUND)) {
					params.put("out_call_gb_cd", null);
				} else {
					if (params.get("out_call_gb_cd") == null) {
						params.put("out_call_gb_cd", null);
					} else if (params.get("out_call_gb_cd").equals(ServiceCommonConst.VIEW_STATE_CALLBACK)) {
						params.put("out_call_gb_cd", ServiceCommonConst.OUT_CALL_GB_CODE.CALL_BACK);
						Map<String, Object> callData =  (Map<String, Object>)params.get("reserveCallbackData");
						callData.put("call_id", callId);
						callBackMapper.updateCallBackCallId(callData);
					} else if (params.get("out_call_gb_cd").equals(ServiceCommonConst.VIEW_STATE_CAMPAIGN)) {
						params.put("out_call_gb_cd", ServiceCommonConst.OUT_CALL_GB_CODE.OUT_BOUND);
					} else if (params.get("out_call_gb_cd").equals(ServiceCommonConst.VIEW_STATE_CALL)) {
						params.put("out_call_gb_cd", ServiceCommonConst.OUT_CALL_GB_CODE.CALL);
					} else if (params.get("out_call_gb_cd").equals(ServiceCommonConst.VIEW_STATE_RESERVATE)) {
						params.put("out_call_gb_cd", null);
					}
				}
				int i = mapper.insertCallInfo(params);
			}
		} catch (Exception e) {
			logError(e);
			throw e;
		}
		
		return params;
	}
	
	/**
	 * 이전에 등록 한 고객번호가 없을 경우
	 * @param params
	 * @return
	 */
	@Transactional
	private Map<String, Object> setNoneCustID (Map<String, Object> params) {
		// 고객번호 채번
		//String custId = mapper.selectNewCustId(params);
		String custId = StringUtil.getUUID(ServiceCommonConst.CODE_PRIFIX_CUST);
		
		params.put("cust_id", custId);
		// 고객정보 저장
		int i = mapper.insertCustomer(params);
		
		// 콜정보 저장
		try {
			String callId = StringUtil.getUUID(ServiceCommonConst.CODE_PRIFIX_CALL);
			params.put("call_id", callId);
			i += mapper.insertCallInfo(params);
//			if (mapper.selectCallInfoCheck(params) < 1) {
//				i += mapper.insertCallInfo(params);
//			}
		} catch (Exception e) {
			logError(e);
		}
		
		
		return params;
	}
	
	/**
	 * 이전에 등록 한 고객번호가 있을 경우
	 * @param params
	 * @return
	 */
	@Transactional
	private Map<String, Object> setHaveCustID (Map<String, Object> params) {
		// 콜정보 저장
		try {
			String callId = StringUtil.getUUID(ServiceCommonConst.CODE_PRIFIX_CALL);
			params.put("call_id", callId);
			int i = mapper.insertCallInfo(params);
//			if (mapper.selectCallInfoCheck(params) < 1) {
//				i += mapper.insertCallInfo(params);
//			}
		} catch (Exception e) {
			logError(e);
		}
		
		
		return params;
	}

	@Override
	@Transactional
	public boolean insertNoticeRead(Map<String, Object> params) {
		int result = 0;
		try {
			result = noticeReadMapper.insertNoticeRead(params);
		} catch (Exception e) {
			logError(e);
			throw e;
		}
		
		return 0 < result ? true : false; 
	}

	@Override
	public Map<String, Object> selectCodeList(Map<String, Object> params) {
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> allList = null;
		List<Map<String, Object>> teamList = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		list = codeMapper.selectCodeList(new HashMap<String, Object>());
		allList = codeMapper.selectCodeAllList(new HashMap<String, Object>());
		teamList = mapper.selectTeamList(new HashMap<String, Object>());
		resultMap.put("list", list);
		resultMap.put("allList", allList);
		resultMap.put("teamList", teamList);
		return resultMap;
	}

	@Override
	public Map<String, Object> selectNoticeList(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int totalCount = noticeMapper.selectNoticeCount(params);
			List<Map<String, Object>> list = noticeMapper.selectNoticeList(params);
			
			resultMap.put("recordsTotal", totalCount);
			resultMap.put("recordsFiltered", totalCount);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
			resultMap.put("update_dttm", sdf.format(Calendar.getInstance().getTime()));
			resultMap.put("data", list);
		} catch (Exception e) {
			logError(e);
			throw e;
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> selectCallBackList(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			resultMap.put("list", callBackMapper.selectNewCallBackList(params));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
			resultMap.put("update_dttm", sdf.format(Calendar.getInstance().getTime()));
		} catch (Exception e) {
			logError(e);
			throw e;
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> selectScriptList(Map<String, Object> params) {
		List<Map<String, Object>> list = mapper.selectScriptList(null);
		list = TreeUtil.groupCategoryCompare(list);
		return list;
	}
	
	@Override
	public Map<String, Object> selectDBDate() {
		return mapper.selectDBDate();
	}
	
	@Override
	public List<Map<String, Object>> selectCTMRList(Map<String, Object> params) {
		return mapper.selectCTMRList(params);
	}
	
	@Override
	@Transactional
	public boolean setTMRStatus(Map<String, Object> params) {
		int i = 0; 
		try {
			i += mapper.insertCTIStatus(params);
			i += mapper.insertCTIStatusHistory(params);
		} catch (Exception e) {
			logError(e);
			throw e;
		}

		return 0 < i ? true : false; 
	}

	@Override
	public Map<String, Object> getCodeAndMyCamp(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("campaignList", mapper.selectMyCampaignList(params));
		resultMap.put("codeList", mapper.selectOutboundCodeList(params));
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getDBLayoutByCompaign(Map<String, Object> params) {
		return mapper.selectDBLayoutByCompaign(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMyDBList(Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> searchList = new ArrayList<>();
		JSONArray arr = new JSONArray((String)params.get("search_list"));
		for (int i=0; i<arr.length(); i++) {
			Map<String, Object> jsonMap = new ObjectMapper().readValue(arr.getString(i), Map.class); 
			String fromStr = null;
			String whereStr = null;
			if(jsonMap.get("is_enc").equals("Y")) {
				fromStr = ", (select FN_ENCRYPT('"+jsonMap.get("search_value")+"','"+params.get("schema_en_key")+"') as enc_"+jsonMap.get("layout_cd")+" from dual) " + jsonMap.get("layout_cd");
				whereStr = "and b."+jsonMap.get("layout_cd")+" = "+jsonMap.get("layout_cd")+".enc_"+jsonMap.get("layout_cd");
			} else {
				if (jsonMap.get("is_like").equals("Y")) {
					whereStr = "and b."+jsonMap.get("layout_cd")+" like concat('%','"+jsonMap.get("search_value")+"','%')";
				} else {
					whereStr = "and b."+jsonMap.get("layout_cd")+" = '"+jsonMap.get("search_value")+"'";
				}
			}

			Map<String, Object> tempMap = new HashMap<>();
			if (fromStr != null) tempMap.put("fromStr", fromStr);
			if (whereStr != null) tempMap.put("whereStr", whereStr);
			searchList.add(tempMap);
		}
		params.put("searchList", searchList);
		
		int totalCount = mapper.selectMyDBListCount(params);
		List<Map<String, Object>> list = mapper.selectMyDBList(params);
		
		resultMap.put("recordsTotal", totalCount);
		resultMap.put("recordsFiltered", totalCount);
		resultMap.put("data", list);
		
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getDBDetailInfo(Map<String, Object> params) {
		List<Map<String, Object>> layoutList = (List<Map<String, Object>>) params.get("dbLayout");

		List<Map<String, Object>> searchList = new ArrayList<>();
		for (Map<String, Object> map : layoutList) {
			String selectStr = null;
			String layout_cd = (String)map.get("layout_cd");
			if (map.get("is_enc").equals("Y")) {
				selectStr = ", FN_DECRYPT(a."+layout_cd+",'"+params.get("schema_en_key")+"') as "+layout_cd;
			} else {
				selectStr = ", a."+layout_cd+" as "+layout_cd;
			}

			Map<String, Object> tempMap = new HashMap<>();
			if (selectStr != null) tempMap.put("selectStr", selectStr);
			searchList.add(tempMap);
		}
		params.put("searchList", searchList);
		
		return mapper.selectDBDetailInfo(params);
	}
}
