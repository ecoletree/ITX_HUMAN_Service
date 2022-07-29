/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.auth ETSessionManager.java
*****************************************************************/
package kr.co.ecoletree.common.auth;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * 로그인 중복체크
 * 
 * @author dongsuk
 *
 */
public class ETSessionManager implements HttpSessionBindingListener {
	
	private static ETSessionManager etSessionManager;
	
	/**
	 * 세션을 키값으로 아이디가 밸류값으로 저장
	 */
	private static Hashtable<HttpSession, String> users = new Hashtable<HttpSession, String>(); 
	
	public static synchronized ETSessionManager getInstance() {
		if (etSessionManager == null) {
			etSessionManager = new ETSessionManager();
		}
		return etSessionManager;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
	 * 세션이 연결되었을 때 호출
	 */
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		users.put(session, event.getName());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 * 세션이 끊겼을 때 호출
	 */
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		users.remove(session); // 사용자 목록에서 삭제
	}
	
	/**
	 * 로그인한 사용자의 아이디를 세션에 저장
	 * @param session
	 * @param userId
	 */
	public void setSession(HttpSession session, String userId) {
		session.setAttribute(userId, this); // valueBound 실행
	}
	
	/**
	 * 로그인한 아이디를 강제로 로그아웃
	 * @param userId
	 */
	public void removeSession(String userId) {
		Enumeration<HttpSession> e = users.keys();
		HttpSession session = null;
		while(e.hasMoreElements()) {
			session = e.nextElement();
			String value = users.get(session);
			if(userId != null && userId.equals(value)) {
				session.invalidate(); // valueUnbound실행
			}
		}
	}
	
	/**
	 * 사용자가 입력한 id, pw가 맞는지 확인
	 * @param userId
	 * @param userPw
	 * @return
	 */
	public boolean isValid(String userId, String userPw) {
		// TODO
		return true;
	}
	
	/**
	 * 로그인한 사용자인지 여부 판단, 중복 로그인 방지
	 * @param userid 검사할 유저아이디
	 * @return 존재하면 true, 새 유저면 false 
	 */
	public boolean isLogon(String userId) {
		return users.containsValue(userId);
	}
	
	/**
	 * 로그인한 사용자 수를 알아내는 메소드
	 * @return int 사용자 수
	 */
	public int getSize() {
		return users.size();
	}
	
	/**
	 * 로그인한 사용자 아이디 컬렉션을 가져오는 메소드
	 * @return Collection 유저아이디 값이 포함
	 */
	public Collection<String> getUserList() {
		return users.values();
	}
}
