/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : Jang Yoon Seok
 * Create Date : 2021. 11. 29.
 * File Name : BlackListMapper.java
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.callInfo.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface BlackListMapper {

	public int insertBlackList(Map<String, Object> params);
	
	public List<Map<String, Object>> selectBlackCodeList();
	
	public int selectBlackListCount(Map<String, Object> params);
}

