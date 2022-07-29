/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.login.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

	/**
	 * 로그인 화면에서 사용할 코드 리스트 조회
	 * @param code
	 * @return
	 */
	public Map<String, Object> getCodeListByCode(Map<String, Object> params);
	
	/**
	 * 로그인
	 * 
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> login(Map<String, Object> param, HttpServletRequest request) throws Exception;

	/**
	 * 비밀번호 변경
	 * 
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> changePW(Map<String, Object> param, HttpServletRequest request) throws Exception;
	
	/**
	 * 실패 3분 후 로그인 실패 카운트 0으로 변경
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	public Map<String, Object> resetFailCount(Map<String, Object> param, HttpServletRequest request);
	
}
