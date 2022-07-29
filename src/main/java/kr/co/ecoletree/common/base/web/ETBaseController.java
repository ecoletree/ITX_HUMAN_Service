/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.base.controller ETBaseController.java
 *****************************************************************/
package kr.co.ecoletree.common.base.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import kr.co.ecoletree.common.exception.ETException;
import kr.co.ecoletree.common.helper.SessionHelper;
import kr.co.ecoletree.common.util.AesUtil;
import kr.co.ecoletree.common.util.CryptoUtil;
import net.sf.json.JSONObject;

/**
 * @author dongsuk
 *
 */
public class ETBaseController implements ServletContextAware {
	
	public ServletContext servletContext;
	
	private Logger logger;
	
	/**
	 * 
	 */
	public ETBaseController() {
		logger = LoggerFactory.getLogger(this.getClass());
	}
	
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
	
	/**
	 * @return
	 */
	public static String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
		return sdf.format(date);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext arg0) {
		servletContext = arg0;
	}
	
	/**
	 * HttpServletRequest에서 파라미터들을 JSONObject로 반환
	 * @param request
	 * @return
	 */
	protected JSONObject getParamToJson(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String name = (String) params.nextElement();
			map.put(name, request.getParameter(name));
		}
		return JSONObject.fromObject(map);
	}
	
	/**
	 * AES Description
	 * @param decryptedPassword
	 * @return
	 */
	protected String descriptAES(String decryptedPassword) {
		AesUtil aesUtil = new AesUtil(256, 1000);
        String plaintext = "";
        if (decryptedPassword != null && decryptedPassword.split("§").length == 4) {
        	try {
        		String secret = decryptedPassword.split("§")[0];
        		String iv = decryptedPassword.split("§")[1];
        		String salt = decryptedPassword.split("§")[2];
        		String data = decryptedPassword.split("§")[3];
	        	plaintext = aesUtil.decrypt(salt, iv, secret, data);
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        return plaintext;
	}
	
	/**
	 * HttpServletRequest에서 파라미터들을 Map으로 반환
	 * @param request
	 * @return
	 */
	protected Map<String, Object> getParamToMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String name = (String) params.nextElement();
			map.put(name, request.getParameter(name));
		}
		return map;
	}
	
	/**
	 * DefaultMultipartHttpServletRequest에서 파라미터들을 Map으로 반환
	 * @param request
	 * @return
	 */
	protected Map<String, Object> getParamToMap(DefaultMultipartHttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String name = (String) params.nextElement();
			map.put(name, request.getParameter(name));
		}
		return map;
	}
	
	/**
	 * 비밀 번호 체크
	 * @param passwd
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws ETException
	 */
	protected boolean checkPasswd(String passwd) {
		boolean result = false;
		String pass = null;
		try {
			pass = CryptoUtil.encodePassword(passwd);
			if (pass.equals(SessionHelper.getSessionVO().getTmr_pw())) {
				result = true;
			}
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | ETException e) {
			logError(e.getMessage());
		}
		return result;
	}
}
