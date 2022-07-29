/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.common.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dongsuk
 *
 */
public class StringUtil {
	
	
	/**
	 * 테이블 고유코드 값 생성
	 * @param
	 * @return
	 */
	public static String getUUID(String type) {
		String uid = type.toUpperCase() + DateUtil.getCurrentDate() + UUID.randomUUID().toString().replace("-", "");
		return uid.length() > 50 ? uid.substring(0, 50) : uid;
	}
	
	/**
	 * null을 "" 으로 변환
	 * @param value
	 * @return
	 */
	public static String nullToEmpty(String value) {
		if (value == null) {
			value = "";
		}
		return value;
	}
	
	/**
	 * null, ""을 replace 으로 변환
	 * @param value
	 * @return
	 */
	public static String nullToReplace(String value, String replace) {
		if (nullToEmpty(value).equals("")) {
			value = replace;
		}
		return value;
	}
	
	/**
	 * null을 int 로 변환
	 * @param value
	 * @return
	 */
	public static int nullToInt(String value) {
		int i = 0;
		if (value == null) {
			i = 0;
		} else {
			try {
				i = Integer.parseInt(value);
			} catch (Exception e) {
				
			}
		}
		return i;
	}
	
	/**
	 * @param yn
	 * @return
	 */
	public static boolean convertYN(String yn) {
		boolean b = false;
		if (yn != null && yn.toLowerCase().equals("y")) {
			b = true;
		} else {
			b = false;
		}
		return b;
		
	}
	
	/**
	 * 문자열에서 패턴과 일치하는 부분의 index를 반환.없으면 -1 반환.
	 * @param target 탐색할 문자열
	 * @param pattern 정규식 패턴으로 포함될 문자열
	 * @return
	 */
	public static int indexOf(String target, String pattern) {
		Matcher m = Pattern.compile(pattern).matcher(target);
		int idx = -1;
		while (m.find()) {
			idx = m.start();
			break;
		}
		return idx;
	}
	
	/**
	 * null 또는 ""일 경우 true
	 * @author sgKim
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Object value) {
		return value == null || value.toString().isEmpty();
	}
	
	/**
	 * 문자열을 정수로 변환. 변환 실패시 지정한 기본값을 반환한다.
	 * 
	 * @author sgKim
	 * @param strNum 변환할 문자열
	 * @param defaultValue 변환 실패시 반환될 기본값
	 * @return
	 */
	public static int parseInt(String strNum, int defaultValue) {
		Pattern p = Pattern.compile("^\\d+.?\\d*$");
		int num = defaultValue;
		
		if (strNum != null && p.matcher(strNum).find()) {
			num = Integer.parseInt(strNum);
		}
		
		return num;
	}
	
	/**
	 * 문자열을 정수로 변환. 변환 실패시 반환되는 값은 0.
	 * 
	 * @author sgKim
	 * @param strNum 변환할 문자열
	 * @return
	 */
	public static int parseInt(String strNum) {
		return parseInt(strNum, 0);
	}
}
