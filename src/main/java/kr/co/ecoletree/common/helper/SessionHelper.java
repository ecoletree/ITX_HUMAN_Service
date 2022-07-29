/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : aileen 
 * Create Date : 2017. 11. 1.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.common.helper;

import java.util.Enumeration;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.ecoletree.common.auth.ETSessionUtil;
import kr.co.ecoletree.common.vo.ETSessionVO;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author aileen
 *
 */
@Component
public class SessionHelper implements ApplicationContextAware {
	
	public static ETSessionUtil etSessionUtil;
	
	private ApplicationContext applicationContext;
	
	@PostConstruct
	private void init() {
		etSessionUtil = applicationContext.getBean("sessionUtil", ETSessionUtil.class);
	}
	
	/**
	 * 인증된 사용자인지(Session에 login정보가 있는지) 검사
	 * @return true(로그인상태) false(로그아웃상태)
	 */
	public static boolean isLoginUser() {
		return etSessionUtil.isLoginUser();
	}
	
	/**
	 * 세션의 sessionVO 값을 리턴
	 * @return null(비로그인 상태)
	 */
	public static ETSessionVO getSessionVO() {
		return etSessionUtil.getSessionVO();
	}
	
	/**
	 * 현재 로그인된 사용자의 아이디를 리턴
	 * @return null(비로그인 상태)
	 */
	public static String getTmrId() {
		return etSessionUtil.getTmrId();
	}

	/**
	 * 현재 로그인된 사용자의 비밀번호를 리턴
	 * @return null(비로그인 상태)
	 */
	public static String getTmrPw() {
		return etSessionUtil.getTmrPw();
	}

	/**
	 * 현재 로그인된 사용자의 이름을 리턴
	 * @return null(비로그인 상태)
	 */
	public static String getTmrNm() {
		return etSessionUtil.getTmrNm();
	}

	/**
	 * 현재 로그인된 날짜시분
	 * @return null(비로그인 상태)
	 */
	public static String getLoginDt() {
		return etSessionUtil.getLoginDt();
	}
	
	/**
	 * 로그아웃
	 * @param request
	 */
	public static void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		Enumeration<String> sessionEnum = session.getAttributeNames();
		while (sessionEnum.hasMoreElements()) {
			String name = sessionEnum.nextElement();
			session.removeAttribute(name);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
	}
	
}
