/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.auth ETSessionListener.java
*****************************************************************/
package kr.co.ecoletree.common.auth;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author dongsuk
 *
 */
public class ETSessionListener implements HttpSessionListener {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 * 세션 생성 시 호출
	 */
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		session.setMaxInactiveInterval(60*30);//세션 생명유지 시간설정
		long time = session.getCreationTime();
		String id = session.getId();
		System.out.println(time + "에 생성된 세션 : " + id);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 * 세션 만료 시 호출
	 * 브라우저 닫힌 후 처리할 때
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
//		long time = session.getCreationTime();
		long ltime = session.getLastAccessedTime(); // 마지막 접속시간
		long ctime = System.currentTimeMillis(); // 현재시간
		String id = session.getId();
		System.out.println((ctime-ltime) + "ms만에 세션이 죽음 : " + id);
		
		// TODO
	}

}
