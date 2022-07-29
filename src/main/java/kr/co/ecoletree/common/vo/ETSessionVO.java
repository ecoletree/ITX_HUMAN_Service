/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.common.vo;

import kr.co.ecoletree.common.base.vo.ETBaseVO;

/**
 * @author aileen
 *
 */
public class ETSessionVO extends ETBaseVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4875281347561437785L;
	
	private String tmr_id;
	private String tmr_nm;
	private String tmr_pw;
	private String login_dt;
	private Object tmr_info;
	
	private String bp_url;
	private String biz_type;
	
	public String getTmr_id() {
		return tmr_id;
	}
	public void setTmr_id(String tmr_id) {
		this.tmr_id = tmr_id;
	}
	public String getTmr_nm() {
		return tmr_nm;
	}
	public void setTmr_nm(String tmr_nm) {
		this.tmr_nm = tmr_nm;
	}
	public String getTmr_pw() {
		return tmr_pw;
	}
	public void setTmr_pw(String tmr_pw) {
		this.tmr_pw = tmr_pw;
	}
	public String getLogin_dt() {
		return login_dt;
	}
	public void setLogin_dt(String login_dt) {
		this.login_dt = login_dt;
	}
	public Object getTmr_info() {
		return tmr_info;
	}
	public void setTmr_info(Object tmr_info) {
		this.tmr_info = tmr_info;
	}
	public String getBp_url() {
		return bp_url;
	}
	public void setBp_url(String bp_url) {
		this.bp_url = bp_url;
	}
	public String getBiz_type() {
		return biz_type;
	}
	public void setBiz_type(String biz_type) {
		this.biz_type = biz_type;
	}
	
}
