/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2020. 1. 2.
 * DESC : 코드데이터를 가져오기 위해 자동으로 넘길 parameter를 만들어 준다
*****************************************************************/
package kr.co.ecoletree.service.code.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.ecoletree.service.code.vo.CodeVO;

public class CodeUtil {

	/**
	 * code 에 따라서 코드 값을 가져온다
	 * @param codes String[]  형태 001,002,003 으로 넣으면 001,002,003 이 포함되어있는(in 조건) 코드들을 가져온다
	 * @return
	 */
	public static Map<String, Object> createCodeListParam(String...codes) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		List<CodeVO> list = new ArrayList<CodeVO>();
		for (String code : codes) {
			CodeVO vo = new CodeVO();
			vo.setCode(code);
			list.add(vo);
		}
		
		if (0 < list.size()) {
			param.put("code_list", list);
		}
		
		return param;
		
	}
}
