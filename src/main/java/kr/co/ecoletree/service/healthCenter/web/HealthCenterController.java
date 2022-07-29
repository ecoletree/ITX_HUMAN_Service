/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.healthCenter.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ecoletree.common.base.web.ETBaseController;
import kr.co.ecoletree.common.helper.SessionHelper;
import kr.co.ecoletree.service.healthCenter.service.HealthCenterService;

@Controller
@RequestMapping("/healthCenter")
public class HealthCenterController extends ETBaseController {

	
	@Autowired
	HealthCenterService service;
	
	@RequestMapping("/getHealthCenterList")
	public @ResponseBody Map<String, Object> getHealthCenterList(HttpServletRequest request) throws Exception {
		Map<String, Object> params = getParamToMap(request);
		params.put("tmr_id", SessionHelper.getTmrId());
		Map<String, Object> map = service.selectHealthCenterList(params);
		return map;
	}
	
	
}