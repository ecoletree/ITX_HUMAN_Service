/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : aileen 
 * Create Date : 2020. 1. 9.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.callInfo.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ecoletree.common.ETCommonConst;
import kr.co.ecoletree.common.auth.Auth;
import kr.co.ecoletree.common.base.web.ETBaseController;
import kr.co.ecoletree.common.helper.SessionHelper;
import kr.co.ecoletree.common.util.ResultUtil;
import kr.co.ecoletree.service.callInfo.service.CallInfoSerivce;

@Controller
@RequestMapping("/callInfo")
public class CallInfoController extends ETBaseController {

	@Autowired
	CallInfoSerivce service;
	
	@Value("${SCHEMA_EN_KEY}")
	private String schema_en_key;
	
	/**
	 * 고객 정보 검색
	 * @param param
	 * @param request
	 * @return
	 */
	@Auth
	@RequestMapping("/getCustInfo")
	public @ResponseBody Map<String, Object> getCustInfo(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
		param.put("schema_en_key", schema_en_key);
		return service.selectCustInfo(param, request);
	}

	/**
	 * 고객 정보 저장
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/saveCustInfo")
	public @ResponseBody Map<String, Object> saveCustInfo(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
		param.put("session_tmr_id", SessionHelper.getTmrId());
		param.put("schema_en_key", schema_en_key);
		Map<String, Object> map = service.saveCustInfo(param, request);
		return ResultUtil.getResultMap(true, param, 0 < (int)map.get("count") ? ETCommonConst.SUCCESS:ETCommonConst.FAILED);
	}

	/**
	 * 상담 정보 저장
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/saveCallInfo")
	public @ResponseBody Map<String, Object> saveCallInfo(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
		param.put("session_tmr_id", SessionHelper.getTmrId());
		param.put("schema_en_key", schema_en_key);
		return service.saveCallInfo(param, request);
	}
	
	/**
	 * 악성 민원인 정보 저장
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/saveBlackList")
	public @ResponseBody Map<String, Object> saveBlackList(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
		param.put("session_tmr_id", SessionHelper.getTmrId());
		param.put("session_tmr_nm", SessionHelper.getTmrNm());
		param.put("schema_en_key", schema_en_key);
		return service.saveBlackList(param, request);
	}
	
	/**
	 * 악성 민원인 코드 가져오기
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/getBlackCodeList")
	public @ResponseBody Map<String, Object> saveBlackList(@RequestBody Map<String, Object> param) throws Exception {
		return ResultUtil.getResultMap(true,service.selectBlackCodeList());
	}
}
