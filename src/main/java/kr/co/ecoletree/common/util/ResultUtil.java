/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2020. 1. 7.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.common.util;

import java.util.HashMap;
import java.util.Map;

import kr.co.ecoletree.common.ETCommonConst;

public class ResultUtil {

	public static Map<String, Object> getResultMap(boolean isSuccess, Object data) {
		return getResultMap(isSuccess,data,null);
	}
	
	/**
	 * 클라이언트로 리턴 하는 값을 정의 하는 유틸
	 * 
	 * @param isSuccess 성공여부
	 * @param data      리턴하는 data값
	 * @param resultMsg   리턴 메시지
	 * @return
	 */
	public static Map<String, Object> getResultMap(boolean isSuccess) {
		Map<String, Object> resultMap = getResultMap(isSuccess, null, null);
		return resultMap;
	} 
	
	/**
	 * 클라이언트로 리턴 하는 값을 정의 하는 유틸
	 * 
	 * @param isSuccess 성공여부
	 * @param data      리턴하는 data값
	 * @param resultMsg   리턴 메시지
	 * @return
	 */
	public static Map<String, Object> getResultMap(boolean isSuccess, Object data, String resultMsg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (isSuccess) {
			resultMap.put(ETCommonConst.MESSAGE, ETCommonConst.SUCCESS);
		} else {
			resultMap.put(ETCommonConst.MESSAGE, ETCommonConst.FAILED);
		}
		
		resultMap.put(ETCommonConst.DATA, data);
		resultMap.put(ETCommonConst.RESULT_MSG, resultMsg);
		
		return resultMap;
	} 
}
