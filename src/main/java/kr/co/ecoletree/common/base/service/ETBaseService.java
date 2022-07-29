/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.base.service ETBaseService.java
*****************************************************************/
package kr.co.ecoletree.common.base.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dongsuk
 *
 */
public class ETBaseService {

private Logger logger;
	
	public ETBaseService() {
		logger = LoggerFactory.getLogger(this.getClass());
	}
	
	protected void logDebug(String message) {
		logger.debug(message);
	}

	protected void logInfo(String message) {
		logger.info(message);
	}
	
	protected void logError(String message) {
		logger.error(message);
	}
	
	protected void logError(String message, Throwable t) {
		logger.error(message, t);
	}
	
	protected void logError(Throwable t) {
		StringWriter sw = new StringWriter(); 
		t.printStackTrace(new PrintWriter(sw)); 
		String exceptionAsString = sw.toString(); 
		logger.error(exceptionAsString);
	}
}
