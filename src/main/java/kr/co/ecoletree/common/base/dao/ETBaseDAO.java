/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.base.dao ETBaseDAO.java
*****************************************************************/
package kr.co.ecoletree.common.base.dao;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author dongsuk
 *
 */
@Component
public class ETBaseDAO {

	//@Resource(name="sqlSession")
	protected SqlSession sqlSession;
	
	private Logger logger;
	
	public ETBaseDAO() {
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
}
