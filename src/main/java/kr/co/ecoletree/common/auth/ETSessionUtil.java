/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.auth ETSessionUtil.java
 *****************************************************************/
package kr.co.ecoletree.common.auth;

import kr.co.ecoletree.common.vo.ETSessionVO;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author aileen
 *
 */
@Component("sessionUtil")
public class ETSessionUtil {
	
	/**
	 * 인증된 사용자인지(Session에 login정보가 있는지) 검사
	 * @return true(로그인상태) false(로그아웃상태)
	 */
	public boolean isLoginUser() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return false;
		} else {
			if (RequestContextHolder.getRequestAttributes().getAttribute("sessionVO", RequestAttributes.SCOPE_SESSION) == null) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * 세션의 sessionVO 값을 리턴
	 * @return null(비로그인 상태)
	 */
	public ETSessionVO getSessionVO() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return null;
		} else {
			ETSessionVO sessionVO = (ETSessionVO) RequestContextHolder.getRequestAttributes().getAttribute("sessionVO", RequestAttributes.SCOPE_SESSION);
			return sessionVO;
		}
	}
	
	/**
	 * 현재 로그인된 사용자의 아이디를 리턴
	 * @return null(비로그인 상태)
	 */
	public String getTmrId() {
		if (getSessionVO() == null) {
			return null;
		}
		return getSessionVO().getTmr_id();
	}
	
	/**
	 * 현재 로그인된 사용자의 비밀번호를 리턴
	 * @return null(비로그인 상태)
	 */
	public String getTmrPw() {
		if (getSessionVO() == null) {
			return null;
		}
		return getSessionVO().getTmr_pw();
	}

	/**
	 * 현재 로그인된 사용자의 이름을 리턴
	 * @return null(비로그인 상태)
	 */
	public String getTmrNm() {
		if (getSessionVO() == null) {
			return null;
		}
		return getSessionVO().getTmr_nm();
	}
	
	/**
	 * 현재 로그인된 날짜시분
	 * @return null(비로그인 상태)
	 */
	public String getLoginDt() {
		if (getSessionVO() == null) {
			return null;
		}
		return getSessionVO().getTmr_pw();
	}
	
}
