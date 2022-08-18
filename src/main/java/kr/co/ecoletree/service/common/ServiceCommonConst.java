/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 13.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.common;

public class ServiceCommonConst {

	/**
	 * 컨트롤러 결과값
	 */
	public static final String RESULT = "result";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	
	/**
	 * 컨트롤러 ModelAndView 공용 네임
	 */
	public static final String INIT_DATA = "initData";
	
	/**
	 * 코드프리픽스
	 */
	public static final String CODE_PRIFIX_NOTICE = "NOTICE";
	public static final String CODE_PRIFIX_TEAM = "TEAM";
	public static final String CODE_PRIFIX_USER = "USER";
	public static final String CODE_PRIFIX_UCASE = "UCASE";
	public static final String CODE_PRIFIX_CUST = "CUST";
	public static final String CODE_PRIFIX_CALL = "CALL";
	
	/**
	 * 로그 히스토리 타입정의
	 */
	public static final String HISTORY_TYPE_FAIL = "FAIL";
	public static final String HISTORY_TYPE_LOGIN = "LOGIN";
	public static final String HISTORY_TYPE_LOGOUT = "LOGOUT";
	public static final String HISTORY_TYPE_SEARCH = "SEARCH";
	public static final String HISTORY_TYPE_SAVE = "SAVE";
	public static final String HISTORY_TYPE_DELETE = "DELETE";
	
	/**
	 * 로그인 실패 한계 수
	 */
	public static final int MAX_LOGIN_FAIL_COUNT = 5;
	
	/**
	 * 코드 타입
	 */
	public static final String CODE_VALUE_BPURL = "900";
	
	/**
	 * 업무 타입
	 */
	public static final String BIZ_TYPE_INBOUND = "I";
	public static final String BIZ_TYPE_OUTBOUND = "O";
	
	/**
	 * VIEW_STATE 타입
	 */
	public static final String VIEW_STATE_CALLBACK = "callback";
	public static final String VIEW_STATE_CAMPAIGN = "campaign";
	public static final String VIEW_STATE_CALL = "call";
	public static final String VIEW_STATE_RESERVATE = "reservate";
	
	/**
	 * 아웃콜 유형
	 * @author boadl
	 *
	 */
	public final class OUT_CALL_GB_CODE {
		public static final String OUT_BOUND = "001";
		public static final String CALL_BACK = "002";
		public static final String CALL = "009";
	}
}
