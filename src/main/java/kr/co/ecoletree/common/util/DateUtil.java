/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dongsuk
 *
 */
public class DateUtil {
	
	/**
	 * 해당 포멧으로 현재 날짜를 리턴
	 * @param format
	 * @return
	 */
	public static String getCurrentDateByFormat(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 날짜 포멧 변경
	 * @param data
	 * @param fromFormat
	 * @param toFormat
	 * @return
	 * @throws Exception
	 */
	public static String changeDateFormat(String data, String fromFormat, String toFormat) throws Exception {
		SimpleDateFormat sff = new SimpleDateFormat(fromFormat);
		SimpleDateFormat sft = new SimpleDateFormat(toFormat);
		Date date = sff.parse(data);
		
		return sft.format(date);
		
	}
	
	/**
	 * 지정된 형태로 오늘 날짜를 리턴
	 * @return
	 */
	public static String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}
}
