/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : aileen 
 * Create Date : 2020. 1. 13.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.historyDetail.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ecoletree.common.ETCommonConst;
import kr.co.ecoletree.common.base.service.ETBaseService;
import kr.co.ecoletree.common.util.ResultUtil;
import kr.co.ecoletree.service.callInfo.mapper.CallInfoMapper;
import kr.co.ecoletree.service.common.ServiceCommonConst;
import kr.co.ecoletree.service.historyDetail.mapper.HistoryDetailMapper;
import kr.co.ecoletree.service.historyDetail.service.HistoryDetailService;
import kr.co.ecoletree.service.reserve.mapper.ReserveMapper;

@Service
public class HistoryDetailServiceImpl extends ETBaseService implements HistoryDetailService {

	@Autowired
	HistoryDetailMapper mapper;
	
	@Autowired
	CallInfoMapper ciMapper;
	
	@Autowired
	ReserveMapper crMapper;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> saveCallInfo(Map<String, Object> param, HttpServletRequest request) {
		try {
			int count = 0;
			
			count += ciMapper.updateCustInfo((Map<String, Object>)param.get("detailData"));
			count += mapper.updateCallInfo((Map<String, Object>)param.get("detailData"));
			count += mapper.updateCallResult((Map<String, Object>)param.get("detailData"));
			
			// 아웃바운드 저장
//			if (((Map<String, Object>)param.get("detailData")).get("io_flag").equals(ServiceCommonConst.BIZ_TYPE_OUTBOUND)) {
//				mapper.updateStateInfo((Map<String, Object>)param.get("detailData"));
//			}
			
			String returnMsg = ETCommonConst.SUCCESS;
			if (count < 3) {
				returnMsg = ETCommonConst.FAILED;
			}
			
			return ResultUtil.getResultMap(true, param, returnMsg);
		} catch (Exception e) {
			logError("test",e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Map<String, Object> saveHistoryDetail(Map<String, Object> param, HttpServletRequest request) {
		String returnMsg = ETCommonConst.SUCCESS;
		try {
			int count = 0;
			
			count += ciMapper.updateCustInfo(param);
			count += mapper.updateCallInfo(param);
			count += mapper.updateCallResult(param);
			
			// 아웃바운드 저장
//			if (param.get("io_flag").equals(ServiceCommonConst.BIZ_TYPE_OUTBOUND)) {
//				mapper.updateStateInfo(param);
//			}
			
			if (count < 3) {
				returnMsg = ETCommonConst.FAILED;
			}
			
		} catch (Exception e) {
			returnMsg = ETCommonConst.FAILED;
		}
		return ResultUtil.getResultMap(true, param, returnMsg);
	}
}
