/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 12.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.code.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.co.ecoletree.service.code.vo.CodeVO;

@Repository
public interface CodeMapper {

	public List<Map<String, Object>> selectCodeList(Map<String, Object> params);
	
	public List<Map<String, Object>> selectCode(CodeVO vo);
	
	public List<Map<String, Object>> selectCodeAllList(Map<String, Object> params);
	
	public List<Map<String, Object>> selectCodeAll(CodeVO vo);
	
	public List<Map<String, Object>> selectNewCodeList();
}
