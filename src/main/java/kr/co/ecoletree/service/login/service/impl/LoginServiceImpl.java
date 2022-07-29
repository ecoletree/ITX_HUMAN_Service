/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.login.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ecoletree.common.ETCommonConst;
import kr.co.ecoletree.common.auth.ETSessionManager;
import kr.co.ecoletree.common.base.service.ETBaseService;
import kr.co.ecoletree.common.util.CryptoUtil;
import kr.co.ecoletree.common.util.DateUtil;
import kr.co.ecoletree.common.util.ResultUtil;
import kr.co.ecoletree.common.util.Utility;
import kr.co.ecoletree.common.vo.ETSessionVO;
import kr.co.ecoletree.service.code.mapper.CodeMapper;
import kr.co.ecoletree.service.common.ServiceCommonConst;
import kr.co.ecoletree.service.login.mapper.LoginHistoryMapper;
import kr.co.ecoletree.service.login.mapper.LoginMapper;
import kr.co.ecoletree.service.login.mapper.PWHistoryMapper;
import kr.co.ecoletree.service.login.service.LoginService;

/**
 * @author aileen
 *
 */
@Service
public class LoginServiceImpl extends ETBaseService implements LoginService {
	
	@Autowired
	LoginMapper mapper;
	
	@Autowired 
	LoginHistoryMapper loginHistoryMapper;
	
	@Autowired
	PWHistoryMapper pwHistoryMapper;
	
	@Autowired
	CodeMapper codeMapper;
	
	@Value("${INIT_PW}")
	private String init_pw;
	
	@Override
	public Map<String, Object> getCodeListByCode(Map<String, Object> params) {
		List<Map<String, Object>> list = codeMapper.selectCodeList(params);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("codeList", list);
		return returnMap;
	}
	
	@Override
	@Transactional
	public Map<String, Object> login(Map<String, Object> param, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultMsg = ServiceCommonConst.SUCCESS;

		// 유저 데이터 검색
		Map<String, Object> tmrInfo = mapper.selectLoginInfo(param);
		
		if (tmrInfo == null) {
			resultMsg = "no_match_data";

		} else {
			// 접속 ip 추출
			String ip = Utility.getIP();
			tmrInfo.put("login_ip", ip);
			tmrInfo.put("insert_ip", ip);
			tmrInfo.put("session_tmr_id", tmrInfo.get("tmr_id"));

			resultMap.put("tmr_id", tmrInfo.get("tmr_id"));
			resultMap.put("tmr_nm", tmrInfo.get("tmr_nm"));
			resultMap.put("tmr_pw", tmrInfo.get("tmr_pw"));
			resultMap.put("ip", ip);
			
			resultMsg = checkLogin(param, tmrInfo);
			// 로그인 성공
			if (resultMsg.equals(ServiceCommonConst.SUCCESS)) {
				// 로그인 실패 카운트 수정
				updateLoginFailCount(tmrInfo, ServiceCommonConst.HISTORY_TYPE_LOGIN);
				// 로그인 히스토리 저장 _ 성공
				insertLoginHistory(tmrInfo, ServiceCommonConst.HISTORY_TYPE_LOGIN);
				
				// 세션 정보 저장
				ETSessionVO etSessionVO = new ETSessionVO();
				etSessionVO.setTmr_id((String)tmrInfo.get("tmr_id"));
				etSessionVO.setTmr_nm((String)tmrInfo.get("tmr_nm"));
				etSessionVO.setTmr_pw((String)tmrInfo.get("tmr_pw"));
				etSessionVO.setLogin_dt(DateUtil.getCurrentDateByFormat("yyyy.MM.dd HH:mm"));
				etSessionVO.setTmr_info(tmrInfo);
				request.getSession().setAttribute(ETCommonConst.SESSION_VO, etSessionVO);
				
				ETSessionManager.getInstance().setSession(request.getSession(), (String)tmrInfo.get("tmr_id"));
			}
		}

		resultMap.put("type", resultMsg);
		return ResultUtil.getResultMap(true, tmrInfo, resultMsg);
	}
	
	/**
	 * 로그인 가능 체크
	 * @param param
	 * @param tmrInfo
	 * @return
	 * @throws Exception
	 */
	@Transactional
	private String checkLogin(Map<String, Object> param, Map<String, Object> tmrInfo) throws Exception {
		String resultMsg = ServiceCommonConst.SUCCESS;
		
		// 로그인 실패 카운트 체크
		if (Integer.parseInt(tmrInfo.get("login_fail_count").toString()) == ServiceCommonConst.MAX_LOGIN_FAIL_COUNT) {
			resultMsg = "locked_account";
			return resultMsg;
		}
		
		// 비밀번호 체크
		String cryptoPasswd = CryptoUtil.encodePassword((String)param.get("tmr_pw"));
		if (!tmrInfo.get("tmr_pw").equals(cryptoPasswd)) {
			resultMsg = "no_match_data";
			// 로그인 실패 카운트 수정
			updateLoginFailCount(tmrInfo, ServiceCommonConst.HISTORY_TYPE_FAIL);
			// 로그인 히스토리 저장 _ 실패
			insertLoginHistory(tmrInfo, ServiceCommonConst.HISTORY_TYPE_FAIL);
			return resultMsg;
		}
		
		if (ETSessionManager.getInstance().isLogon((String)tmrInfo.get("tmr_id"))) {
			resultMsg = "login_user";
			return resultMsg;
		}
		
		// 최초 접속 탐지 or 비밀번호 초기화된 계정
		cryptoPasswd = CryptoUtil.encodePassword(CryptoUtil.getBase64String(init_pw));
		if (tmrInfo.get("tmr_pw").equals(cryptoPasswd)) {
			resultMsg = "initialized_pw";
			return resultMsg;
		}
		
		// 14일 미접속 계정
		Map<String, Object> historyMap = loginHistoryMapper.selectLastLoginHistory(tmrInfo); 
		if (14 < Integer.parseInt(historyMap.get("workdays").toString()) && Integer.parseInt(historyMap.get("cnt").toString()) == 0) {
			resultMsg = "close_account";
			return resultMsg;
		}

		// 비밀번호 변경 30일 대상 계정
		int count = pwHistoryMapper.selectLastPWHistory(tmrInfo);
		if (count == 0) {
			resultMsg = "change_pw";
			return resultMsg;
		}
		
		return resultMsg;
	}
	
	@Override
	public Map<String, Object> resetFailCount(Map<String, Object> param, HttpServletRequest request) {
		int count = updateLoginFailCount(param, ServiceCommonConst.HISTORY_TYPE_LOGIN);
		return ResultUtil.getResultMap(true, null, 0 < count ? ServiceCommonConst.SUCCESS : ServiceCommonConst.ERROR);
	}
	
	@Override
	@Transactional
	public Map<String, Object> changePW(Map<String, Object> param, HttpServletRequest request) throws Exception {
		String resultMsg = ServiceCommonConst.SUCCESS;
		
		// 기존 비밀번호 체크
		String cryptoPasswd = CryptoUtil.encodePassword((String)param.get("tmr_oldPw"));
		if (!cryptoPasswd.equals(param.get("tmr_pw"))) {
			resultMsg = "no_match_data";
		
		} else {
			// 접속 ip 추출
			String ip = Utility.getIP();
			param.put("login_ip", ip);
			param.put("insert_ip", ip);
			
			param.put("tmr_pw", CryptoUtil.encodePassword((String)param.get("tmr_newPw")));
			param.put("session_tmr_id", param.get("tmr_id"));
			
			String type = (String)param.get("type");
			
			// 비밀번호 업데이트
			mapper.updateLoginPW(param);
			// 비밀번호 변경 히스토리 저장
			pwHistoryMapper.insertPWHistory(param);
			
			// 로그인 히스토리 저장 _ 성공
			insertLoginHistory(param, ServiceCommonConst.HISTORY_TYPE_LOGIN);

			// 14일 미접속 계정인 경우 바로 로그인되어야 함
			if (type.equals("close_account")) {

				// 세션 정보 저장
				ETSessionVO etSessionVO = new ETSessionVO();
				etSessionVO.setTmr_id((String)param.get("tmr_id"));
				etSessionVO.setTmr_nm((String)param.get("tmr_nm"));
				etSessionVO.setTmr_pw((String)param.get("tmr_pw"));
				etSessionVO.setLogin_dt(DateUtil.getCurrentDateByFormat("yyyy.MM.dd HH:mm"));
				etSessionVO.setTmr_info(param);
				request.getSession().setAttribute(ETCommonConst.SESSION_VO, etSessionVO);
				
				ETSessionManager.getInstance().setSession(request.getSession(), (String)param.get("tmr_id"));
			} 
		}
		
		return ResultUtil.getResultMap(true, param, resultMsg);
	}
	
	/**
	 * 로그인 실패 카운트 수정
	 * @param param
	 * @param history_type
	 */
	private int updateLoginFailCount(Map<String, Object> param, String history_type) {
		param.put("status", history_type);
		return mapper.updateLoginFailCount(param);
	}
	
	/**
	 * 로그인 히스토리 저장
	 * @param param
	 * @param history_type
	 */
	private int insertLoginHistory(Map<String, Object> param, String history_type) {
		param.put("login_type", history_type);
		return loginHistoryMapper.insertLoginHistory(param);
	}
}
