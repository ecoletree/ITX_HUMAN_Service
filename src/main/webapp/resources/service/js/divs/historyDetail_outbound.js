/*******************************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * @Author : aileen
 * @CreateDate : 2018. 08. 06.
 * @DESC : 홈 _ 메인
 ******************************************************************************/
(function(et, ctrl) {
	if (_.isObject(et) && et.name === ETCONST.PROJECT_NAME) {
		if (!et.historyDetail || et.vc.name !== "historyDetail") {
			et.historyDetail = ctrl(et);
		}
	} else {
		console.error("ecoletree OR ETCONST is not valid. please check that common.js file was imported.");
	}
}(this.ecoletree, function(et) {
	
	"use strict";
	
	var ctrl = {};
	
	ctrl.name = "historyDetail";
	ctrl.path = "/historyDetail";
	
	ctrl.detailData = null; // 넘겨받은 데이터 저장용
	ctrl.status = null; // 넘겨받은 현재 상태
	
	ctrl.popupObj = {}; // 상세화면 열린 팝업 리스트
	
	// ============================== 화면 컨트롤 ==============================
	/**
	 * init VIEW
	 */
	ctrl.init = function(initData) {
		var self = et.historyDetail;
		self.data = initData;
		
		// set event
		$("#"+self.name+"_selCrCd1").on("change", self.selCrCd_changeEventListener);
		$("#"+self.name+"_selCrCd2").on("change", self.selCrCd_changeEventListener);
		$("#"+self.name+"_selCrCd3").on("change", self.selCrCd_changeEventListener);
		$("#"+self.name+"_selCrCd4").on("change", self.selCrCd_changeEventListener);
		
		$("#"+self.name+"_btnShowDetail").on("click", self.btnShowDetail_clickEventListener);
		$("#"+self.name+"_btnUpdate").on("click", self.btnUpdate_clickEventListener);
		$("#"+self.name+"_btnCancel").on("click", self.btnCancel_clickEventListener);
		$("#"+self.name+"_btnSave").on("click", self.btnSave_clickEventListener);
	};
	
	/**
	 * home 에서 코드 데이터가 왔을때 데이터를 넘겨 받음
	 */
	ctrl.setCodeData = function () {
		var self = et.historyDetail;
	};
	
	/**
	 * history 에서 로우 클릭시 넘겨주는 데이터
	 */
	ctrl.sendData = function(data) {
		var self = et.historyDetail;
		self.detailData = data;
		// 뷰 영역 초기화
		self.setView(false);

		// F5 눌렀을 때는 setStatus 시간차로 버튼 상태를 변경할 수 없기 때문
		if (!!self.detailData) {
			if (self.status !== BP_STATE.BUSY) $("#"+self.name+"_btnUpdate").removeAttr("disabled");
			$("#"+self.name+"_btnShowDetail").removeAttr("disabled");
		} else {
			$("#"+self.name+"_btnUpdate").attr("disabled", "disabled");
			$("#"+self.name+"_btnShowDetail").attr("disabled", "disabled");
		}

		// 버튼영역제어
		$("#"+self.name+"_divBtn1").show();
		$("#"+self.name+"_divBtn2").hide();

		if (!!self.detailData) {
			$("#"+self.name+"_iptCust_id").val(self.detailData.cust_id);
			$("#"+self.name+"_iptCust_nm").val(self.detailData.cust_nm);
			$("#"+self.name+"_iptHand_tel").val(self.detailData.view_hand_tel);
			$("#"+self.name+"_iptClass_1").val(self.detailData.class_1);
			$("#"+self.name+"_iptClass_2").val(self.detailData.class_2);
			$("#"+self.name+"_iptClass_3").val(self.detailData.class_3);
			
			var memo = self.detailData.cr_mm === null ? "" : (self.detailData.cr_mm).replace(/<br\s*\/?>/mg,"\n");
			$("#"+self.name+"_taCr_mm").val(memo);
			
			$("#"+self.name+"_selCrCd1").empty();
			$("#"+self.name+"_selCrCd2").empty();
			$("#"+self.name+"_selCrCd3").empty();
			$("#"+self.name+"_selCrCd4").empty();
			$("#"+self.name+"_selExec_state").empty();

			if (!!self.detailData.cr_cd1) {
				$("#"+self.name+"_selCrCd1").append("<option value='"+self.detailData.cr_cd1+"'>"+ self.detailData.view_cr_cd1 +"</option>");
			} else {
				$("#"+self.name+"_selCrCd1").append("<option value=''>선택해주세요</option>");
			}
			if (!!self.detailData.cr_cd2) {
				$("#"+self.name+"_selCrCd2").append("<option value='"+self.detailData.cr_cd2+"'>"+ self.detailData.view_cr_cd2 +"</option>");
			} else {
				$("#"+self.name+"_selCrCd2").append("<option value=''>선택해주세요</option>");
			}
			if (!!self.detailData.cr_cd3) {
				$("#"+self.name+"_selCrCd3").append("<option value='"+self.detailData.cr_cd3+"'>"+ self.detailData.view_cr_cd3 +"</option>");
			} else {
				$("#"+self.name+"_selCrCd3").append("<option value=''>선택해주세요</option>");
			}
			if (!!self.detailData.cr_cd4) {
				$("#"+self.name+"_selCrCd4").append("<option value='"+self.detailData.cr_cd4+"'>"+ self.detailData.view_cr_cd4 +"</option>");
			} else {
				$("#"+self.name+"_selCrCd4").append("<option value=''>선택해주세요</option>");
			}
			if (!!self.detailData.state_cd) {
				$("#"+self.name+"_selExec_state").append("<option value='"+self.detailData.state_cd+"'>"+ self.detailData.state_nm +"</option>");
			} else {
				$("#"+self.name+"_selExec_state").append("<option value=''>선택해주세요</option>");
			}
		}
	};
	
	/**
	 * home에서 상태가 변경될 때 데이터를 받음
	 */
	ctrl.setStatus = function(status) {
		var self = et.historyDetail;
		self.status = status;
		
		if (self.status === BP_STATE.BUSY) { // 상담중
			self.sendData(self.detailData);
			
			// 버튼영역제어
			$("#"+self.name+"_divBtn1").show();
			$("#"+self.name+"_divBtn2").hide();
			
			$("#"+self.name+"_btnUpdate").attr("disabled", "disabled");
			if (!!self.detailData) {
				$("#"+self.name+"_btnShowDetail").removeAttr("disabled");
			} else {
				$("#"+self.name+"_btnShowDetail").attr("disabled", "disabled");
			}
		} else {
			if (!!self.detailData) {
				$("#"+self.name+"_btnUpdate").removeAttr("disabled");
				$("#"+self.name+"_btnShowDetail").removeAttr("disabled");
			} else {
				$("#"+self.name+"_btnUpdate").attr("disabled", "disabled");
				$("#"+self.name+"_btnShowDetail").attr("disabled", "disabled");
			}
		}
	}
	
	/**
	 * 화면 리셋 및 비활성 제어
	 */
	ctrl.setView = function(isEditable) {
		var self = et.historyDetail;
		
		if (isEditable) {
			$("#"+self.name+"_iptCust_nm").removeAttr("disabled");
			$("#"+self.name+"_iptClass_1").removeAttr("disabled");
			$("#"+self.name+"_iptClass_2").removeAttr("disabled");
			$("#"+self.name+"_iptClass_3").removeAttr("disabled");
//			$("#"+self.name+"_dfCalss_4").removeAttr("disabled");
//			$("#"+self.name+"_dfCalss_4").datepicker({
//				format: "yyyy.mm.dd",
//				language: "kr",
//				todayHighlight : true
//			});
//			$("#"+self.name+"_btnCalss_4").on("click", function(e) {
//				$("#"+self.name+"_dfCalss_4").datepicker("show",{
//					format: "yyyy.mm.dd",
//					language: "kr",
//					todayHighlight : true
//	            }).on("changeDate", function (e) {
//	                $(this).datepicker("hide")
//
//	            });
//			});
			
			$("#"+self.name+"_selCrCd1").removeAttr("disabled");
			$("#"+self.name+"_selCrCd2").removeAttr("disabled");
			$("#"+self.name+"_selCrCd3").removeAttr("disabled");
			$("#"+self.name+"_selCrCd4").removeAttr("disabled");
			$("#"+self.name+"_selExec_state").removeAttr("disabled");
			$("#"+self.name+"_taCr_mm").removeAttr("disabled");
		} else {
			$("#"+self.name+"_iptCust_id").val("");
			$("#"+self.name+"_iptCust_nm").val("");
			$("#"+self.name+"_iptHand_tel").val("");
			$("#"+self.name+"_iptClass_1").val("");
			$("#"+self.name+"_iptClass_2").val("");
			$("#"+self.name+"_iptClass_3").val("");
//			$("#"+self.name+"_dfCalss_4").val("");
			
			$("#"+self.name+"_selCrCd1").empty();
			$("#"+self.name+"_selCrCd1").append("<option>선택해주세요</option>");
			$("#"+self.name+"_selCrCd2").empty();
			$("#"+self.name+"_selCrCd2").append("<option>선택해주세요</option>");
			$("#"+self.name+"_selCrCd3").empty();
			$("#"+self.name+"_selCrCd3").append("<option>선택해주세요</option>");
			$("#"+self.name+"_selCrCd4").empty();
			$("#"+self.name+"_selCrCd4").append("<option>선택해주세요</option>");
			$("#"+self.name+"_selExec_state").empty();
			$("#"+self.name+"_selExec_state").append("<option>선택해주세요</option>");
			$("#"+self.name+"_taCr_mm").val("");
			
			$("#"+self.name+"_iptCust_nm").attr("disabled", "disabled");
			$("#"+self.name+"_iptClass_1").attr("disabled", "disabled");
			$("#"+self.name+"_iptClass_2").attr("disabled", "disabled");
			$("#"+self.name+"_iptClass_3").attr("disabled", "disabled");
//			$("#"+self.name+"_dfCalss_4").attr("disabled", "disabled");
			
			$("#"+self.name+"_selCrCd1").attr("disabled", "disabled");
			$("#"+self.name+"_selCrCd2").attr("disabled", "disabled");
			$("#"+self.name+"_selCrCd3").attr("disabled", "disabled");
			$("#"+self.name+"_selCrCd4").attr("disabled", "disabled");
			$("#"+self.name+"_selExec_state").attr("disabled", "disabled");
			$("#"+self.name+"_taCr_mm").attr("disabled", "disabled");
		}
	};
	
	// ============================== 동작 컨트롤 ==============================
	
	// ============================== 동작 컨트롤 : 외부 등록 ==============================
	/**
	 * 저장 서비스 호출 결과를 핸들링.
	 */
	ctrl.saveCallSucceed = function(returnData) {
		var self = et.historyDetail;
		var msg = returnData.resultMsg;
		var data = returnData.data;
		
		$("#"+self.name+"_btnSave").removeAttr("disabled");
		
		if (msg === ETCONST.SUCCESS) {
			self.isSaved = true;
			et.home.showMessageAlert("이전 상담이력을 수정하였습니다.");
			
			// 상담 목록 재검색
			et.history.btnSearchHandler();
		}
	};
	
	// ============================== 이벤트 리스너 ==============================
	/**
	 * 대분류, 중분류, 소분류 변경
	 */
	ctrl.selCrCd_changeEventListener = function(e) {
		var self = et.historyDetail;
		
		var id = e.currentTarget.id;
		var num = id.replace(self.name+"_selCrCd","");
		var selCd1, selCd2, selCd3, codeList;
		
		selCd1 = $("#"+self.name+"_selCrCd1").val();
		selCd2 = $("#"+self.name+"_selCrCd2").val();
		selCd3 = $("#"+self.name+"_selCrCd3").val();
		
		if (num === "1") {
			codeList = et.getCodeAllList("010", selCd1);
			et.makeSelectOption(null, {value:"item3",text:"item_nm"},"#"+self.name+"_selCrCd3","선택해주세요");
			$("#"+self.name+"_selCrCd3").find("option:eq(0)").prop("selected", true);
			et.makeSelectOption(null, {value:"item4",text:"item_nm"},"#"+self.name+"_selCrCd4","선택해주세요");
			$("#"+self.name+"_selCrCd4").find("option:eq(0)").prop("selected", true);
			if (!!codeList) {
				et.makeSelectOption(codeList, {value:"item2",text:"item_nm"},"#"+self.name+"_selCrCd2","선택해주세요");
				$("#"+self.name+"_selCrCd2").find("option:eq(0)").prop("selected", true).trigger("change");
			} else {
				et.makeSelectOption(null, {value:"item2",text:"item_nm"},"#"+self.name+"_selCrCd2","선택해주세요");
				$("#"+self.name+"_selCrCd2").find("option:eq(0)").prop("selected", true);
			}
		} else if (num === "2") {
			codeList = et.getCodeAllList("010", selCd1, selCd2);
			et.makeSelectOption(null, {value:"item4",text:"item_nm"},"#"+self.name+"_selCrCd4","선택해주세요");
			$("#"+self.name+"_selCrCd4").find("option:eq(0)").prop("selected", true);
			if (!!codeList) {
				et.makeSelectOption(codeList, {value:"item3",text:"item_nm"},"#"+self.name+"_selCrCd3","선택해주세요");
				$("#"+self.name+"_selCrCd3").find("option:eq(0)").prop("selected", true).trigger("change");
			} else {
				et.makeSelectOption(null, {value:"item3",text:"item_nm"},"#"+self.name+"_selCrCd3","선택해주세요");
				$("#"+self.name+"_selCrCd3").find("option:eq(0)").prop("selected", true);
			}
		} else if (num === "3") {
			codeList = et.getCodeAllList("010", selCd1, selCd2, selCd3);
			if (!!codeList) {
				et.makeSelectOption(codeList, {value:"item4",text:"item_nm"},"#"+self.name+"_selCrCd4","선택해주세요");
			} else {
				et.makeSelectOption(null, {value:"item4",text:"item_nm"},"#"+self.name+"_selCrCd4","선택해주세요");
			}
			$("#"+self.name+"_selCrCd4").find("option:eq(0)").prop("selected", true);
		}
	};
	
	/**
	 * 상세보기 버튼 클릭
	 */
	ctrl.btnShowDetail_clickEventListener = function() {
		var self = et.historyDetail;
		
		if ($("#"+self.name+"_btnShowDetail").is("[disabled=disabled]")) {
			return;
		}
		
		var popupId = "pop"+Date.now();
		// 팝업 띄우기
		var tempPop = window.open(
							getContextPath()+"/pop.historyPopup.sp", 
							popupId,
							"width=970px, height=660px, location=no, toolbars=no, status=no");
		
		self.popupObj[popupId] = tempPop;
	};
	
	/**
	 * 수정하기 버튼 클릭
	 */
	ctrl.btnUpdate_clickEventListener = function() {
		var self = et.historyDetail;
		var codeList;
		
		if ($("#"+self.name+"_btnUpdate").is("[disabled=disabled]")) {
			return;
		}
		
		self.setView(true);

		// 대분류 코드
		codeList = et.getCodeAllList("010");
		et.makeSelectOption(codeList, {value:"item1",text:"item_nm"},"#"+self.name+"_selCrCd1","선택해주세요");
		$("#"+self.name+"_selCrCd1").val(self.detailData.cr_cd1);
		// 중분류 코드
		codeList = et.getCodeAllList("010", self.detailData.cr_cd1);
		et.makeSelectOption(codeList, {value:"item2",text:"item_nm"},"#"+self.name+"_selCrCd2","선택해주세요");
		$("#"+self.name+"_selCrCd2").val(self.detailData.cr_cd2);
		// 소분류 코드
		codeList = et.getCodeAllList("010", self.detailData.cr_cd1, self.detailData.cr_cd2);
		et.makeSelectOption(codeList, {value:"item3",text:"item_nm"},"#"+self.name+"_selCrCd3","선택해주세요");
		$("#"+self.name+"_selCrCd3").val(self.detailData.cr_cd3);
		// 상세 코드
		codeList = et.getCodeAllList("010", self.detailData.cr_cd1, self.detailData.cr_cd2, self.detailData.cr_cd3);
		et.makeSelectOption(codeList, {value:"item4",text:"item_nm"},"#"+self.name+"_selCrCd4","선택해주세요");
		$("#"+self.name+"_selCrCd4").val(self.detailData.cr_cd4);
		
		// 처리코드
		codeList = et.getCodeAllList("011");
		et.makeSelectOption(codeList, {value:"item1",text:"item_nm"},"#"+self.name+"_selExec_state","선택해주세요");
		$("#"+self.name+"_selExec_state").val(self.detailData.state_cd);

		// 버튼영역제어
		$("#"+self.name+"_divBtn1").hide();
		$("#"+self.name+"_divBtn2").show();
	};
	
	/**
	 * 취소 버튼 클릭
	 */
	ctrl.btnCancel_clickEventListener = function() {
		var self = et.historyDetail;
		
		if ($("#"+self.name+"_btnCancel").is("[disabled=disabled]")) {
			return;
		}
		
		self.sendData(self.detailData);
		
		// 버튼영역제어
		$("#"+self.name+"_divBtn1").show();
		$("#"+self.name+"_divBtn2").hide();
	};
	
	/**
	 * 저장하기 버튼 클릭
	 */
	ctrl.btnSave_clickEventListener = _.debounce(function() {
		var self = et.historyDetail;
		
		if ($("#"+self.name+"_btnSave").is("[disabled=disabled]")) return;
				
		self.detailData.cust_nm = $("#"+self.name+"_iptCust_nm").val();
		self.detailData.class_1 = $("#"+self.name+"_iptClass_1").val();
		self.detailData.class_2 = $("#"+self.name+"_iptClass_2").val();
		self.detailData.class_3 = $("#"+self.name+"_iptClass_3").val();
		
		self.detailData.cr_cd1 = $("#"+self.name+"_selCrCd1").val();
		self.detailData.cr_cd2 = $("#"+self.name+"_selCrCd2").val();
		self.detailData.cr_cd3 = $("#"+self.name+"_selCrCd3").val();
		self.detailData.cr_cd4 = $("#"+self.name+"_selCrCd4").val();
		self.detailData.state_cd = $("#"+self.name+"_selExec_state").val();
		self.detailData.cr_mm = $("#"+self.name+"_taCr_mm").val().replace(/\n/g, '<br/>');

		var optionData = $("#"+self.name+"_selCrCd4").find("option:selected").data();
		self.detailData.o_state_cd = optionData.value1;
		self.detailData.o_state_scd = optionData.value2;
		
		var postData = {
			detailData : self.detailData,
		};
		
		$("#"+self.name+"_btnSave").attr("disabled", "disabled");
		
		new ETService().setSuccessFunction(self.saveCallSucceed).callService(self.path + "/saveCallInfo", postData);
	},500);
	
	/**
	 * 팝업에서 보내주는 메시지 받는 이벤트
	 */
	ctrl.receiveMessage = function(e) {
		var self = et.historyDetail;
		
		if (e.origin !== window.location.protocol+"//"+window.location.host) {
			return;
		}
		if (e.data.action === "init") {
			var data = {};
			data.contextPath = getContextPath();
			data.detailData = self.detailData;
			data.code = ITX_ALL_CODE;
			data.session_tmr_id = $("#home_spTmrId").text();
			e.source.postMessage(data, e.origin);
		} else if (e.data.action === "saveComplete") {
			// 상담 목록 재검색
			et.history.btnSearchHandler();
		}
	};
	
	return ctrl;
}));