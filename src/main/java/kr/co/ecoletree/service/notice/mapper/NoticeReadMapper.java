/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 12.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.notice.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface NoticeReadMapper {

	public int insertNoticeRead(Map<String, Object> params);
}
