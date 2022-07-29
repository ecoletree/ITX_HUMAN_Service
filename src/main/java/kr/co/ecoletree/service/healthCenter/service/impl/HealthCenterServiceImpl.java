/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.healthCenter.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ecoletree.common.base.service.ETBaseService;
import kr.co.ecoletree.service.healthCenter.mapper.HealthCenterMapper;
import kr.co.ecoletree.service.healthCenter.service.HealthCenterService;

/**
 * @author aileen
 *
 */
@Service
public class HealthCenterServiceImpl extends ETBaseService implements HealthCenterService {
	
	@Autowired
	HealthCenterMapper mapper;
	
	@Override
	public Map<String, Object> selectHealthCenterList(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int totalCount = mapper.selectHealthCenterCount(params);
			List<Map<String, Object>> list = mapper.selectHealthCenterList(params);
			
			resultMap.put("recordsTotal", totalCount);
			resultMap.put("recordsFiltered", totalCount);
			resultMap.put("data", list);
		} catch (Exception e) {
			logError(e);
			throw e;
		}
		
		return resultMap;
	}
	
}
