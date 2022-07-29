/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.auth ETLoginCheckInterceptor.java
*****************************************************************/
package kr.co.ecoletree.common.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ecoletree.common.handler.ETExceptionHandler;
import kr.co.ecoletree.common.helper.SessionHelper;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author dongsuk
 *
 */
public class ETLoginCheckInterceptor implements HandlerInterceptor {

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 * 클라이언트의 요청을 컨트롤러에 전달하기 전 호출
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) {
		
		HandlerMethod handlerMethod = null;
		
		try {
			handlerMethod = (HandlerMethod) handler;
			Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
			if(auth != null) {
				if(!SessionHelper.isLoginUser()) {
					response.sendRedirect(request.getContextPath() + "/sessionTimeout");
					return false;
				} else {
					if (!ETSessionManager.getInstance().isLogon(SessionHelper.getTmrId())) {
						response.sendRedirect(request.getContextPath() + "/sessionTimeout");
						return false;
					}
				}
			}
		} catch (ClassCastException e) {
			new ETExceptionHandler().iOExceptionHandler(request, e);
		} catch (IOException e) {
			new ETExceptionHandler().iOExceptionHandler(request, e);
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 * 컨트롤러 로직이 실행된 후 호출
	 * 컨트롤러 실행 도중 에러가 발생된 경우 실행되지 않음
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 * 컨트롤러 로직이 실행된 후 호출
	 * 컨트롤러 실행 도중이나 뷰페이지 실행 도중 에러발생 해도 실행됨
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
