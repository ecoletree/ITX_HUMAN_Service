/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 12.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.healthCenter.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface HealthCenterMapper {

	public List<Map<String,Object>> selectHealthCenterList(Map<String,Object> params);
	public int selectHealthCenterCount(Map<String,Object> params);
}
