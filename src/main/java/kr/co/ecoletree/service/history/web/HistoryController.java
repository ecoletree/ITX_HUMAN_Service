/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.history.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ecoletree.common.ETCommonConst;
import kr.co.ecoletree.common.auth.Auth;
import kr.co.ecoletree.common.base.web.ETBaseController;
import kr.co.ecoletree.common.helper.SessionHelper;
import kr.co.ecoletree.common.util.ResultUtil;
import kr.co.ecoletree.service.history.service.HistoryService;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/history")
public class HistoryController extends ETBaseController{
	
	@Autowired
	HistoryService service;
	
	@Value("${SCHEMA_EN_KEY}")
	private String schema_en_key;
	
	/**
	 * 히스토리 정보 가져오기
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/getHeaderList")
	public @ResponseBody Map<String, Object> getHeaderList(HttpServletRequest request) throws Exception {
		List<Map<String, Object>> map = service.selectHistoryHeader();
		return ResultUtil.getResultMap(true, map, ETCommonConst.SUCCESS);
	}
	
	/**
	 * 히스토리 정보 가져오기
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/getCallHistoryList")
	public @ResponseBody Map<String, Object> getCallHistoryList(HttpServletRequest request) throws Exception {
		Map<String, Object> params = getParamToMap(request);
		params.put("tmr_id", SessionHelper.getTmrId());
		params.put("log_tmr_nm", SessionHelper.getSessionVO().getTmr_nm());
		params.put("schema_en_key", schema_en_key);
		Map<String, Object> map = service.selectHistoryList(params);
		return map;
	}
	
	/**
	 * 히스토리 미저장 정보 가져오기
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/getCallNotSaveHistoryList")
	public @ResponseBody Map<String, Object> getCallNotSaveHistoryList(HttpServletRequest request) throws Exception {
		Map<String, Object> params = getParamToMap(request);
		params.put("tmr_id", SessionHelper.getTmrId());
		params.put("log_tmr_nm", SessionHelper.getSessionVO().getTmr_nm());
		params.put("schema_en_key", schema_en_key);
		Map<String, Object> map = service.selectNotSaveHistoryList(params);
		return map;
	}
}
