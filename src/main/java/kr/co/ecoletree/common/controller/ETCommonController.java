/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.base.controller ETCommonController.java
*****************************************************************/
package kr.co.ecoletree.common.controller;

import kr.co.ecoletree.common.base.web.ETBaseController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author dongsuk
 *
 */
@Controller
public class ETCommonController extends ETBaseController {

	/**
	 * @param pathParam
	 * @param reAttr
	 * @return
	 */
	@RequestMapping("/{pathParam}.sp")
	public String includeJspMethod(@PathVariable String pathParam, RedirectAttributes reAttr) {
		pathParam = pathParam.replace(".", "/");
		return "forward:/WEB-INF/views/" + pathParam + ".jsp";
	}
	
	/**
	 * @param pathParam
	 * @param reAttr
	 * @return
	 */
	@RequestMapping("/home/body/*/{pathParam}")
	public ModelAndView includeJspMethod1(@PathVariable String pathParam, RedirectAttributes reAttr, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		//pathParam = pathParam.replace(".", "/");
		String viewName = req.getRequestURI().replaceAll(req.getContextPath(), "").replaceAll("/", ".");
		mv.setViewName(viewName);
		return mv;
	}
	
	/**
	 * @param pathParam
	 * @param reAttr
	 * @return
	 */
	@RequestMapping("/*/body/*/{pathParam}")
	public ModelAndView includeJspMethod2(@PathVariable String pathParam, RedirectAttributes reAttr, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		//pathParam = pathParam.replace(".", "/");
		String viewName = req.getRequestURI().replaceAll(req.getContextPath(), "").replaceAll("/", ".");
		mv.setViewName(viewName);
		return mv;
	}
}
