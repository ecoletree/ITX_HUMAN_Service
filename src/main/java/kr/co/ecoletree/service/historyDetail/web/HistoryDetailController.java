/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : aileen 
 * Create Date : 2020. 1. 13.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.historyDetail.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ecoletree.common.auth.Auth;
import kr.co.ecoletree.common.base.web.ETBaseController;
import kr.co.ecoletree.service.historyDetail.service.HistoryDetailService;

@Controller
@RequestMapping("/historyDetail")
public class HistoryDetailController extends ETBaseController {

	@Autowired
	HistoryDetailService service;
	
	@Value("${SCHEMA_EN_KEY}")
	private String schema_en_key;
	
	/**
	 * 콜 정보 저장
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	@Auth
	@RequestMapping("/saveCallInfo")
	public @ResponseBody Map<String, Object> saveCallInfo(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
		param.put("schema_en_key", schema_en_key);
		return service.saveCallInfo(param, request);
	}
	
	/**
	 * 이전 상담 이력 상세보기 팝업에서 상담내용 + 예약콜(예약콜인 경우) 저장
	 * 
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveHistoryDetail")
	public @ResponseBody Map<String, Object> saveHistoryDetail(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
		param.put("schema_en_key", schema_en_key);
		return service.saveHistoryDetail(param, request);
	}
}
