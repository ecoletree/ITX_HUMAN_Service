/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.login.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.ecoletree.common.ETCommonConst;
import kr.co.ecoletree.common.auth.ETSessionManager;
import kr.co.ecoletree.common.base.web.ETBaseController;
import kr.co.ecoletree.common.exception.ETException;
import kr.co.ecoletree.common.helper.SessionHelper;
import kr.co.ecoletree.common.util.DateUtil;
import kr.co.ecoletree.common.util.ResultUtil;
import kr.co.ecoletree.common.vo.ETSessionVO;
import kr.co.ecoletree.service.common.ServiceCommonConst;
import kr.co.ecoletree.service.login.service.LoginService;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/login")
public class LoginController extends ETBaseController {

	private static final String JSP_PATH = ".login";
	
	@Autowired
	LoginService service;
	
	/**
	 * 로그인 화면 열기
	 * @return
	 */
	@RequestMapping(value= {"","/"})
	public ModelAndView openLoginMain() {
		List<Map<String, Object>> paramList = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("code", ServiceCommonConst.CODE_VALUE_BPURL);
		paramList.add(paramMap);
		
		Map<String, Object> tempMap = new HashMap<>(); 
		tempMap.put("code_list", paramList);
		Map<String, Object> returnMap = service.getCodeListByCode(tempMap);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject(ServiceCommonConst.INIT_DATA, JSONObject.fromObject(returnMap));
		mv.setViewName(JSP_PATH + ".login" );
		return mv;
	}
	
	/**
	 * 로그인 
	 * @param params
	 * @param request
	 * @return
	 * @throws ETException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/doLogin")
	public @ResponseBody Map<String, Object> login(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		String cryptoStr = (String)param.get("cryptoInfo");
		
		String descriptStr = descriptAES(cryptoStr);
		Map<String, Object> descriptData =  new ObjectMapper().readValue(descriptStr, Map.class);
		result = service.login(descriptData, request);
		
		return result;
	}
	
	/**
	 * 다중 로그인 방지
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("/closeMultiLogin")
	public @ResponseBody Map<String, Object> closeMultiLogin(@RequestBody Map<String, Object> param, HttpServletRequest request) {
		ETSessionManager.getInstance().removeSession((String)param.get("tmr_id"));
		SessionHelper.logout(request);
		
		// 세션 정보 저장
		ETSessionVO etSessionVO = new ETSessionVO();
		etSessionVO.setTmr_id((String)param.get("tmr_id"));
		etSessionVO.setTmr_nm((String)param.get("tmr_nm"));
		etSessionVO.setTmr_pw((String)param.get("tmr_pw"));
		etSessionVO.setLogin_dt(DateUtil.getCurrentDateByFormat("yyyy.MM.dd HH:mm"));
		etSessionVO.setTmr_info(param);
		request.getSession().setAttribute(ETCommonConst.SESSION_VO, etSessionVO);
		
		ETSessionManager.getInstance().setSession(request.getSession(), (String)param.get("tmr_id"));
		
		return ResultUtil.getResultMap(true, param, ServiceCommonConst.SUCCESS);
	}

	/**
	 * 로그인 실패 3분 후 실패 카운트 초기화
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("/resetFailCount")
	public @ResponseBody Map<String, Object> resetFailCount(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
		return service.resetFailCount(param, request);
	}
	
	

	/**
	 * 비밀번호 변경, 계정 잠금 해제
	 * @param params
	 * @param request
	 * @return
	 * @throws ETException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/changePW")
	public @ResponseBody Map<String, Object> changePW(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		String cryptoStr = (String)param.get("cryptoInfo");
		
		String descriptStr = descriptAES(cryptoStr);
		Map<String, Object> descriptData =  new ObjectMapper().readValue(descriptStr, Map.class);
		result = service.changePW(descriptData, request);
			
		return result;
	}
	
}