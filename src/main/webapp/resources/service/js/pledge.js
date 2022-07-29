/*******************************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * @Author : dongsuk
 * @CreateDate : 2020. 03. 16.
 * @DESC : 서약서
 ******************************************************************************/
(function(et, ctrl) {
	if (_.isObject(et) && et.name === ETCONST.PROJECT_NAME) {
		if (!et.vc || et.vc.name !== "pledge") {
			et.vc = ctrl(et);
		}
	} else {
		console.error("ecoletree OR ETCONST is not valid. please check that common.js file was imported.");
	}
}(this.ecoletree, function(et) {
	
	"use strict";
	
	var ctrl = {};
	
	ctrl.name = "pledge";
	
	ctrl.initData = null;
	// ============================== 화면 컨트롤 ==============================
	/**
	 * init VIEW
	 */
	ctrl.init = function(initData) {
		var self = this;
		self.initData = initData;
		// set view
//		$("#pledge_showName1").text(initData.tmr_nm);
//		$("#pledge_showName2").text(initData.tmr_nm);
		$("#pledge_date").text($.format.date(new Date(), 'yyyy.MM.dd'));
		
		// set event
		$("#pledge_btnAgree").on("click", self.btnAgree_clickEventListener);
		$("#pledge_btnComplete").on("click", self.btnComplete_clickEventListener);
		
		et.setEnterKeyDownEvent("#pledge_enterName1", self.btnComplete_clickEventListener);
		et.setEnterKeyDownEvent("#pledge_enterName2", self.btnComplete_clickEventListener);
	};
	
	// ============================== 동작 컨트롤 ==============================
	
	// ============================== 동작 컨트롤 : 외부 등록 ==============================
	
	// ============================== 이벤트 ==============================
	/**
	 * 확인 버튼 클릭
	 */
	ctrl.btnAgree_clickEventListener = function(e) {
		var self = et.vc;

		$("#pledge_notice").hide();
		$("#pledge_content").show();
		$("#pledge_enterName1").focus();
	};

	/**
	 * 확인 버튼 클릭
	 */
	ctrl.btnComplete_clickEventListener = function(e) {
		var self = et.vc;
		var errorCount = 0;
		var name1 = $("#pledge_enterName1").val().trim();
		var name2 = $("#pledge_enterName2").val().trim();
		
		if (name1 === "" || name1 !== self.initData.tmr_nm) {
			$("#pledge_div1").addClass("error");
			$("#pledge_error1").show();
			errorCount++;
		} else {
			$("#pledge_div1").removeClass("error");
			$("#pledge_error1").hide();
		}

		if (name2 === "" || name2 !== self.initData.tmr_nm) {
			$("#pledge_div2").addClass("error");
			$("#pledge_error2").show();
			errorCount++;
		} else {
			$("#pledge_div2").removeClass("error");
			$("#pledge_error2").hide();
		}
		
		if (errorCount === 0) {
			// 로그인 성공
			new ETService().callView("/", null);
		}
	};
	
	
	return ctrl;
}));