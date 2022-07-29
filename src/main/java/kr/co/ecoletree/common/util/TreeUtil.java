/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2020. 2. 11.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeUtil {

	public static List<Map<String, Object>> groupCategoryCompare(List<Map<String, Object>> categoryList){
		return groupCategoryCompare(categoryList, "code_cd", "p_code_cd");
	}
	
	/**
	 * 그룹 카테고리 정보를 트리형으로 변경
	 * @param categoryList
	 * @return
	 */
	public static List<Map<String, Object>> groupCategoryCompare(List<Map<String, Object>> categoryList, String codeCdName, String pCodeCdName){
		List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
    	for (Map<String, Object> vo1 : categoryList) {
    		boolean isChildren = false;
    		String nodeId1 = (String)vo1.get(codeCdName);
    		String pNodeId1 = (String)vo1.get(pCodeCdName);
    		List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
    		
    		for (Map<String, Object> vo2 : categoryList) {
    			String nodeId2 = (String)vo2.get(codeCdName);
        		String pNodeId2 = (String)vo2.get(pCodeCdName);
    			if (pNodeId2 != null) {
        			if (nodeId1.equals(nodeId2)) {
        				continue;
        			} else {
    					if (nodeId1.equals(pNodeId2)) {
        					children.add(generateGroupCategoryHierarchy(categoryList, vo2, codeCdName, pCodeCdName));
        					vo1.put("children", children);
        					isChildren = true;
        				}
        			}
    			}
    		}
    		if (pNodeId1 == null) {
    			groupList.add(vo1);
    		}
    	}
    	return groupList;
	}
	
	/**
	 * XML를 만들기전에 계층 구조로 변경
	 * @param list
	 * @param pVo
	 * @return
	 */
	private static Map<String, Object> generateGroupCategoryHierarchy(List list, Map<String, Object> pVo, String codeCdName, String pCodeCdName) {
		String pNodeId = (String)pVo.get(pCodeCdName);
		String nodeId = (String)pVo.get(codeCdName);
		List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
		boolean b = false;
		for (int i=0; i < list.size(); i++) {
			Map<String, Object> gcVo = (Map<String, Object>)list.get(i);
			String gcPNodeId = (String)gcVo.get(pCodeCdName);
			if (gcPNodeId != null) {
				if (nodeId.equals(gcPNodeId)) {
					Map<String , Object> childMap = generateGroupCategoryHierarchy(list , gcVo, codeCdName, pCodeCdName);
					if (childMap != null) {
						children.add(childMap);
						pVo.put("children", children);
					}
				}
			}
		}
		return pVo;
	}
}
