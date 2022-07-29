/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.common.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aileen
 *
 */
public class MapUtil {
	
	/**
	 * vo를 map으로 리턴
	 * @param mList
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> createMap(Method[] mList, Object c) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		for (int i = 0; i < mList.length; i++) {
			Method m = mList[i];
			String name = m.getName();
			
			if (0 == name.indexOf("get")) {
				name = name.substring(3);
				name = name.substring(0, 1).toLowerCase() + name.substring(1);
				
				Object invoke = m.invoke(c);
				
				if (m.getReturnType() == String.class) {
					invoke = invoke == null ? "" : invoke.toString();
				}
				map.put(name, invoke);
			}
		}
		
		return map;
	}
	
	/**
	 * vo를 map으로 변환하여 리턴 (null을 변환하지 않는다)
	 * @param c
	 * @param vo 변환할 VO
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> convertVoToMap(Class<?> c, Object vo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Method[] mList = c.getMethods();
		for (Method method : mList) {
			String name = method.getName();
			if (name.indexOf("get") == 0) { // getter
				name = name.substring(3);
				name = name.substring(0, 1).toLowerCase() + name.substring(1);
				map.put(name, method.invoke(vo));
			}
		}
		
		return map;
	}
}
