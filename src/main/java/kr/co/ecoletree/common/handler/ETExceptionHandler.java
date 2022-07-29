/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.handler ETExceptionHandler.java
*****************************************************************/
package kr.co.ecoletree.common.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author dongsuk
 *
 */
@ControllerAdvice
public class ETExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String DEFAULT_EXCEPTION_VIEW = "common/welcome";
	public static final String IO_EXCEPTION_VIEW = "common/noPath";
	
	
	protected void logDebug(String message) {
		logger.debug(message);
	}

	protected void logInfo(String message) {
		logger.info(message);
	}
	
	protected void logError(String meesage) {
		logger.error(meesage);
	}
	
	protected void logError(String meesage, Throwable t) {
		logger.error(meesage, t);
	}
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="IOException occured")
	@ExceptionHandler(IOException.class)
	public ModelAndView iOExceptionHandler(HttpServletRequest req, Exception e) {
//		logError("page not found");
		
//		ModelAndView mav = new ModelAndView(IO_EXCEPTION_VIEW);
//		mav.setViewName(IO_EXCEPTION_VIEW);
//		JSONObject j = new JSONObject();
//		j.accumulate("code", 404);
//		j.accumulate("msg", "page not found");
//		j.accumulate("page", IO_EXCEPTION_VIEW);
//		mav.addObject(j);
		
		ModelAndView mav = new ModelAndView();
		
//		String url = req.getServletPath();
//		if(url.contains("/admin/")) {
//			mav.setViewName(SessionHelper.getUseWebLayoutPath() + ".body.error.error404");
//		} else {
//			mav.setViewName(IO_EXCEPTION_VIEW);
//		}
		logger.error("ETExceptionHandler#defaultExceptionHandler", e);
		return mav;
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView defaultExceptionHandler(HttpServletRequest req, Exception e) {
//		logError(e.getMessage(), e.getCause());
		
//		ModelAndView mav = new ModelAndView();
//		
//		String url = req.getServletPath();
//		if(url.contains("/admin/")) {
//			mav.setViewName(SessionHelper.getUseWebLayoutPath() + ".body.error.error500");
//		}
		
//		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
//            throw e;

		ModelAndView mav = new ModelAndView(DEFAULT_EXCEPTION_VIEW);
//		mav.addObject("500");
//		JSONObject j = new JSONObject();
//		j.accumulate("error", 500);
//		j.accumulate("errorMessage", e.getMessage());
//		mav.addObject(j);
		logger.error("ETExceptionHandler#defaultExceptionHandler", e);
		return mav;
	}
}
