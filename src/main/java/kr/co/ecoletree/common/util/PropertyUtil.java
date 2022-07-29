/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2020. 1. 21.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.common.util;

import java.io.IOException;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUtil {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static PropertyUtil instance;
	
	String resource = "message/message.properties";
	
	Properties properties = new Properties();
	
	
	/**
	 * 프로퍼티의 인스턴스 생성
	 * @return Properties
	 * @throws IOException
	 */
	public static PropertyUtil getInstance() throws IOException {
		if (instance == null) {
			instance = new PropertyUtil();
		}
		instance.setProperties();
		return instance;
	}
	
	
	/**
	 * 프로퍼티 파일을 셋팅
	 * @throws IOException
	 */
	private void setProperties () throws IOException {
		try {
			Reader reader = Resources.getResourceAsReader(resource);
			properties.load(reader);
		}catch(IOException e) {
			logger.error("Properties File Not READ", e);
			throw e;
		}
	}
	
	/**
	 * 프로퍼티의 Message를 가져온다
	 * {0},{1} 은 파라미터를 넣어서 해결
	 * @param key 프로퍼티 키
	 * @param strings 프로퍼티에 들어갈 파라미터
	 * @return
	 */
	public String getProperties (String key, Object...strings) {
		String message = properties.getProperty(key);
		if (strings != null && 0 < strings.length) {
			String format = MessageFormat.format(message, strings);
			message = format;
		}
		return message;
	}
}
