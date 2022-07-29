/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.controller ETErrorController.java
*****************************************************************/
package kr.co.ecoletree.common.controller;

import javax.servlet.http.HttpServletRequest;

import kr.co.ecoletree.common.base.web.ETBaseController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author dongsuk
 *
 */
@Controller
public class ETExceptionController extends ETBaseController {

	@RequestMapping("/sessionTimeout")
	public String gotoTimeout() {
		return "/common/timeout";
	}
	
	@RequestMapping("/welcome")
	public String gotoWelcome() {
		return "/common/welcome";
	}
	
	@RequestMapping("/noPath")
	public String gotoNoPath() {
		return "/common/noPath";
	}
	
	@RequestMapping("/open404Error")
	public ModelAndView goto404ErrorPage(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		
//		String url = req.getServletPath();
//		if(url.contains("/admin/")) {
//			mav.setViewName(SessionHelper.getUseWebLayoutPath() + ".body.error.error404");
//		} else {
//			mav.setViewName("noPath");
//		}
		mav.setViewName("common/noPath");
		
		return mav;
	}
	
	@RequestMapping("/open500Error")
	public ModelAndView goto500ErrorPage(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		
//		String url = req.getServletPath();
//		if(url.contains("/admin/")) {
//		mav.setViewName(SessionHelper.getUseWebLayoutPath() + ".body.error.error404");
//		} else {
//			mav.setViewName("noPath");
//		}
		mav.setViewName("common/welcome");
		
		return mav;
	}
}
