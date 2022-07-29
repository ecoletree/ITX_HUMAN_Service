/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.healthCenter.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface HealthCenterService {


	public Map<String, Object> selectHealthCenterList(Map<String, Object> params);
}
