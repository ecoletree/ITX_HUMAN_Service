/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : aileen 
 * Create Date : 2020. 1. 9.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.callInfo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ecoletree.common.ETCommonConst;
import kr.co.ecoletree.common.base.service.ETBaseService;
import kr.co.ecoletree.common.util.ResultUtil;
import kr.co.ecoletree.service.callInfo.mapper.BlackListMapper;
import kr.co.ecoletree.service.callInfo.mapper.CallInfoMapper;
import kr.co.ecoletree.service.callInfo.service.CallInfoSerivce;
import kr.co.ecoletree.service.callback.mapper.CallBackMapper;

@Service
public class CallInfoServiceImpl extends ETBaseService implements CallInfoSerivce {

	@Autowired
	CallInfoMapper mapper;
	
	@Autowired
	CallBackMapper cbMapper;
	
	@Autowired
	BlackListMapper blMapper;
	
	@Override
	public Map<String, Object> selectCustInfo(Map<String, Object> param, HttpServletRequest request) {
		try {
			// 고객 데이터 검색
			Map<String, Object>  custInfo = mapper.selectCustInfo(param);
			return ResultUtil.getResultMap(true, custInfo, ETCommonConst.SUCCESS);
		} catch (Exception e) {
			logError(e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Map<String, Object> saveCustInfo(Map<String, Object> param, HttpServletRequest request) {
		try {
			int count = mapper.updateCustInfo(param);
			return ResultUtil.getResultMap(true, param, 0<count?ETCommonConst.SUCCESS:ETCommonConst.FAILED);
		} catch (Exception e) {
			logError(e);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> saveCallInfo(Map<String, Object> param, HttpServletRequest request) {
		try {
			int count = 0;
	
			String saveType = (String)param.get("saveType");
			if (saveType.equals("both")) {
				// 고객정보 저장
				count += mapper.updateCustInfo(param);
			}
	
			// 콜 정보 저장
			count += mapper.updateCallInfo(param);

			// 콜 아이디로 콜 시퀀스 결과 검색
			Map<String, Object> map = mapper.selectCallResultSeq(param);
			int seq = 0;
			if (map != null && map.get("cr_sno") != null) seq = Integer.parseInt((String)map.get("cr_sno"));
			param.put("cr_sno", seq);
			// 콜 결과값 저장
			count += mapper.insertCallResult(param);
			
			// 아웃바운드 저장
//			if (param.get("io_flag").equals(ServiceCommonConst.BIZ_TYPE_OUTBOUND)) {
//				mapper.updateStateInfo(param);
//			}

			// 예약콜 또는 콜백 후에 해당 정보를 저장
			if (param.get("viewStatus").equals("callback")) {
//				if(param.get("reserveCallbackData") != null) {
					Map<String, Object> tempMap = (HashMap<String, Object>)param.get("reserveCallbackData");
					tempMap.put("exec_call_id", param.get("call_id"));
					tempMap.put("exec_state", param.get("state_cd"));
					tempMap.put("session_tmr_id", param.get("session_tmr_id"));
					cbMapper.updateOutboundCallBack(tempMap);
//				}
			} 
			
			String returnMsg = ETCommonConst.SUCCESS;
			if (saveType.equals("both")) {
				if (count < 2) {
					returnMsg = ETCommonConst.FAILED;
				}
			} else {
				if (count < 1) {
					returnMsg = ETCommonConst.FAILED;
				}
			}
			
			return ResultUtil.getResultMap(true, param, returnMsg);
		
		} catch (Exception e) {
			logError(e);
			throw e;
		}
	}

	@Override
	public Map<String, Object> saveBlackList(Map<String, Object> param, HttpServletRequest request) {
		int i = blMapper.insertBlackList(param);
		return ResultUtil.getResultMap(0<i);
	}

	@Override
	public List<Map<String, Object>> selectBlackCodeList() {
		return blMapper.selectBlackCodeList();
	}

}
