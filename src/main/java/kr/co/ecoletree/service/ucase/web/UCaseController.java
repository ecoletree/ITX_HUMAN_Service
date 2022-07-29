/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.ucase.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ecoletree.common.base.web.ETBaseController;
import kr.co.ecoletree.common.helper.SessionHelper;
import kr.co.ecoletree.common.util.ResultUtil;
import kr.co.ecoletree.service.ucase.service.UCaseService;

@Controller
@RequestMapping("/ucase")
public class UCaseController extends ETBaseController {

	@Autowired
	UCaseService service;
	
	@Value("${SCHEMA_EN_KEY}")
	private String schema_en_key;
	
	/** 긴급정보 리스트 가져오기
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getUCaseList")
	public @ResponseBody Map<String, Object> getUCaseList(HttpServletRequest request) throws Exception {
		Map<String, Object> params = getParamToMap(request);
		params.put("tmr_id", SessionHelper.getTmrId());
		params.put("schema_en_key", schema_en_key);
		Map<String, Object> map = service.selectUCaseList(params);
		return map;
	}
	
	/**
	 * 긴급정보 수정 및 저장 하기
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveUCase")
	public @ResponseBody Map<String, Object> saveUCase(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("tmr_id", SessionHelper.getTmrId());
		params.put("schema_en_key", schema_en_key);
		int i = service.upsertUCase(params);
		boolean isSuccess = true;
		if (i < 1) {
			isSuccess = false;
		} else {
			isSuccess = true;
		}
		return ResultUtil.getResultMap(isSuccess,params); 
	}
	
	
}