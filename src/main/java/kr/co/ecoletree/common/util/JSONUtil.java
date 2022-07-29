/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.common.util;

import java.lang.reflect.Method;

import net.sf.json.JSONObject;

/**
 * @author dongsuk
 *
 */
public class JSONUtil {

	/**
	 * object 값을 jsonObject로 변경해 리턴
	 * @param mList
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static JSONObject createJson(Method[] mList, Object c) throws Exception{
		JSONObject json = new JSONObject();
		for (int i=0; i < mList.length; i++) {
			Method m = mList[i];
			String name = m.getName();
			if (0 == name.indexOf("get")) {
				name = name.substring(3);
				name = name.substring(0, 1).toLowerCase() + name.substring(1);
				json.put(name, m.invoke(c) == null ? "" : m.invoke(c).toString());
			}
		}
		return json;
	}
}
