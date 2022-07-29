/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.ucase.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ecoletree.common.base.service.ETBaseService;
import kr.co.ecoletree.common.helper.SessionHelper;
import kr.co.ecoletree.common.util.StringUtil;
import kr.co.ecoletree.service.common.ServiceCommonConst;
import kr.co.ecoletree.service.ucase.mapper.UCaseMapper;
import kr.co.ecoletree.service.ucase.service.UCaseService;

/**
 * @author aileen
 *
 */
@Service
public class UCaseServiceImpl extends ETBaseService implements UCaseService {
	
	@Autowired
	UCaseMapper mapper;
	
	@Override
	public Map<String, Object> selectUCaseList(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int totalCount = mapper.selectUCaseCount(params);
			List<Map<String, Object>> list = mapper.selectUCaseList(params);
			
			resultMap.put("recordsTotal", totalCount);
			resultMap.put("recordsFiltered", totalCount);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
			resultMap.put("update_dttm", sdf.format(Calendar.getInstance().getTime()));
			resultMap.put("data", list);
		} catch (Exception e) {
			logError(e);
			throw e;
		}
		return resultMap;
	}

	@Override
	public int upsertUCase(Map<String, Object> params) {
		params.put("tmr_id", SessionHelper.getTmrId());
		
		if (!params.containsKey("uc_id") || params.get("uc_id") == null || params.get("uc_id").equals("")) {
			params.put("uc_id", StringUtil.getUUID(ServiceCommonConst.CODE_PRIFIX_UCASE));
		}
		return mapper.upsertUCase(params);
	}
	
}
