/*******************************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * @Author : aileen
 * @CreateDate : 2018. 08. 06.
 * @DESC : 홈 _ 메인
 ******************************************************************************/
(function(et, ctrl) {
	if (_.isObject(et) && et.name === ETCONST.PROJECT_NAME) {
		if (!et.callInfo || et.callInfo.name !== "callInfo") {
			et.callInfo = ctrl(et);
		}
	} else {
		console.error("ecoletree OR ETCONST is not valid. please check that common.js file was imported.");
	}
}(this.ecoletree, function(et) {
	
	"use strict";
	
	var ctrl = {};
	
	ctrl.name = "callInfo";
	ctrl.path = "/callInfo";
	
	ctrl.callData = null; // 넘겨받은 데이터 저장용
	ctrl.status = null; // 넘겨받은 현재 상태

	ctrl.callDataBefore = null; // 이전 데이터 저장용
	ctrl.statusBefore = null; // 이전 상태
	
	ctrl.afterCallServiceStartTime = null; // 후처리 시작 플래그
	ctrl.isSaved = false; // 저장여부 판단 플래그
	ctrl.isAutoSave = false; // 자동저장여부 판단 플래그
	
	// ============================== 화면 컨트롤 ==============================
	/**
	 * init VIEW
	 */
	ctrl.init = function(initData) {
		var self = et.callInfo;
		
		// set Event
		$("#"+self.name+"_btnSaveCustInfo").on("click", self.btnSaveCustInfo_clickEventListener);
		$("#"+self.name+"_btnClearCallInfo").on("click", self.btnClearCallInfo_clickEventListener);
		$("#"+self.name+"_btnSaveCallInfo").on("click", self.btnSaveCallInfo_clickEventListener);
		
		$("#"+self.name+"_btnBlackList").on("click", self.btnBlackList_clickHandler);
		$("#"+self.name+"_btnBlackListSave").on("click", self.btnBlackListSave_clickHandler);
		$("#"+self.name+"_btnDial").on("click", self.btnDial_clickEventListener);
		
		// 악성민원인 코드를 가져온다
//		new ETService().setSuccessFunction(function (result){
//			if (result.message === ETCONST.SUCCESS) {
//				$("#"+self.name+"_selBlackCode").empty();
//				for (var i = 0; i < result.data.length; i++) {
//					var option = "";
//					if (i === 0) {
//						option = $("<label class='ecoleRadio'><input type='radio' name='callInfo_raBlack' checked='checked' value='"+result.data[i].blk_cd+"'><i></i>"+result.data[i].blk_name+"</label>");
//					} else {
//						option = $("<label class='ecoleRadio'><input type='radio' name='callInfo_raBlack' value='"+result.data[i].blk_cd+"'><i></i>"+result.data[i].blk_name+"</label>");
//					}
//					$("#"+self.name+"_selBlackCode").append(option)
//				}
//			}
//		}).callService(self.path + "/getBlackCodeList", {});
	};
	
	/**
	 * home 에서 코드 데이터가 왔을때 데이터를 넘겨 받음
	 */
	ctrl.setCodeData = function () {
		var self = et.callInfo;
		var codeList;
		
		// 대분류 코드
		codeList = et.getCodeList("010");
		self.makeCodeList("#"+self.name+"_ulCrCd1", codeList, "item1");
		$("#"+self.name+"_ulCrCd1 > li:eq(0)").addClass("sel").trigger("click");
		
		// 처리결과
		codeList = et.getCodeList("011");
		$("#"+self.name+"_selExec_state").empty();
		if (!!codeList) {
			var li;
			for (var i = 0; i < codeList.length; i++) {
				li = $('<li>', {
					id : self.name+"_selExec_state"+"_"+(i+1),
					text : codeList[i]["item_nm"]
				});
				li.data("cd", codeList[i]["item1"]);
				$("#"+self.name+"_selExec_state").append(li);
				li.on("click",self.state_clickEventListener);
			}
		}
		$("#"+self.name+"_selExec_state > li:eq(0)").addClass("sel");
	};
	
	/**
	 * home 에서 customer 콜이 왔을때 데이터를 넘겨 받음
	 * result.data = {item_id, global_id, hand_tel, call_id, io_flag, cust_id} 넘기는 object의 주요 형태
	 */
	ctrl.sendData = function(data) {
		var self = et.callInfo;
		self.callData = _.cloneDeep(data);
		if (!!self.callData) {
			if (!self.callData.tel && !!self.callData.hand_tel) { // 콜백, 인바운드 전화번호가 넘어올 때 다른 필드 사용
				self.callData.tel = self.callData.hand_tel;
			}
			if (!!self.callData.tel) {
				$("#"+self.name+"_iptHand_tel").val(self.callData.tel);
				self.clearDivUserInfo();
				
				if (self.callData.viewStatus === "callback") { // 상태가 콜백인 경우
					if (!self.callData.reserveCallbackData) {
						$("#home_user_iptPhoneNum").val(self.callData.tel);
						self.isAutoOpen = true;
					}
				} else { // 콜백 이외 상태
					$("#home_user_iptPhoneNum").val(self.callData.tel);
					self.isAutoOpen = true;
				}
				
				
			}
		}
		
		if (!!self.callData) {
			new ETService().setSuccessFunction(self.getCustInfoCallSucceed).callService(self.path + "/getCustInfo", self.callData);
		}
	};
	
	/**
	 * home에서 상태가 변경될 때 데이터를 받음
	 */
	ctrl.setStatus = function(status) {
		var self = et.callInfo;
		
		self.statusBefore = self.status;
		self.status = status;
		
		// 상태에 따른 활성여부 제어
		self.setView();
	}
	
	/**
	 * home에서 상태가 후처리로 바뀔 때 DB 시간을 리턴
	 */
	ctrl.setCallEndTime = function(time) {
		var self = et.callInfo;
		
		self.afterCallServiceStartTime = time;
	}
	
	/**
	 * 화면 리셋 및 비활성 제어
	 */
	ctrl.setView = function () {
		var self = et.callInfo;
		// 인풋, 버튼들 비활성 제어
		var isInputDisabled = true;
		var isCustSaveBtnDisabled = true;
		var isCallClearBtnDisabled = true;
		var isCallSaveBtnDisabled = true;
		
		// 고객정보, 상담정보 리셋제어
		var isCustInfoReset = true;
		var isCallInfoReset = true;
		
		if (self.status === BP_STATE.BUSY) { // 상담중
			isInputDisabled = false;
			isCustSaveBtnDisabled = true;
			isCallClearBtnDisabled = false;
			isCallSaveBtnDisabled = true;
			
			isCustInfoReset = false;
			isCallInfoReset = true;
			
		} else if (self.status === BP_STATE.AFTER_CALL_WORK) { // 후처리
			// 통화가 끝나는 시간, 후처리 시작시간
//			self.afterCallServiceStartTime = $.format.date(new Date(), 'yyyy-MM-dd HH:mm:ss'); 
			self.isSaved = false;
			
			isInputDisabled = false;
			isCustSaveBtnDisabled = false;
			isCallClearBtnDisabled = false;
			isCallSaveBtnDisabled = false;
			
			isCustInfoReset = false;
			isCallInfoReset = false;
			
		} else {
			isInputDisabled = true;
			isCustSaveBtnDisabled = true;
			isCallClearBtnDisabled = true;
			isCallSaveBtnDisabled = true;

			isCustInfoReset = true;
			isCallInfoReset = true;
			
			self.checkSaveStatus();
		}
		
		if (isInputDisabled) {
//			$("#"+self.name+"_iptCust_nm").attr("disabled", "disabled");
//			$("#"+self.name+"_iptClass_1").attr("disabled", "disabled");
//			$("#"+self.name+"_iptClass_2").attr("disabled", "disabled");
//			$("#"+self.name+"_iptClass_3").attr("disabled", "disabled");

			$("#"+self.name+"_taCr_mm").attr("disabled", "disabled");
//			$("#"+self.name+"_taCr_mm_add").attr("disabled", "disabled");
		} else {
//			$("#"+self.name+"_iptCust_nm").removeAttr("disabled");
//			$("#"+self.name+"_iptClass_1").removeAttr("disabled");
//			$("#"+self.name+"_iptClass_2").removeAttr("disabled");
//			$("#"+self.name+"_iptClass_3").removeAttr("disabled");

			$("#"+self.name+"_taCr_mm").removeAttr("disabled", "disabled");
//			$("#"+self.name+"_taCr_mm_add").removeAttr("disabled", "disabled");
		}
		if (isCustSaveBtnDisabled) {
			$("#"+self.name+"_btnSaveCustInfo").attr("disabled", "disabled");
		} else {
			$("#"+self.name+"_btnSaveCustInfo").removeAttr("disabled");
		}
		if (isCallClearBtnDisabled) {
			$("#"+self.name+"_btnClearCallInfo").attr("disabled", "disabled");
		} else {
			$("#"+self.name+"_btnClearCallInfo").removeAttr("disabled");
		}
		if (isCallSaveBtnDisabled) {
			$("#"+self.name+"_btnSaveCallInfo").attr("disabled", "disabled");
		} else {
			$("#"+self.name+"_btnSaveCallInfo").removeAttr("disabled");
		}
		
		if (!self.isAutoSave) {		
			if (isCustInfoReset) {
				$("#"+self.name+"_iptCust_id").val("");
				$("#"+self.name+"_iptCust_nm").val("");
				$("#"+self.name+"_iptHand_tel").val("");
				$("#"+self.name+"_iptClass_1").val("");
				$("#"+self.name+"_iptClass_2").val("");
				$("#"+self.name+"_iptClass_3").val("");
			}
	
			if (isCallInfoReset) {
				$(".ListCodeWrap").scrollTop(0);
				$("#"+self.name+"_ulCrCd1 > li:eq(0)").addClass("sel").trigger("click");
				$("#"+self.name+"_selExec_state > li:eq(0)").addClass("sel").trigger("click");
				$("#"+self.name+"_taCr_mm").val("");
				$("#"+self.name+"_taCr_mm_add").val("");
			}
		}
	};
	
	/**
	 * 대분류, 중분류, 소분류 리스트 생성
	 */
	ctrl.makeCodeList = function(target, list) {
		var self = et.callInfo;
		var num = target.replace("#"+self.name+"_ulCrCd", "");
		var li;
		
		$(target).empty();
		
		if (!!list) {
			for (var i = 0; i < list.length; i++) {
				li = $('<li>', {
					id : self.name+"_liCrCd"+num+"_"+(i+1),
					text : list[i]["item_nm"],
					title : list[i]["item_nm"]
				});
				li.data("cd", list[i]["item"+num]);
				li.data("num", num);
				$(target).append(li);
				li.on("click",self.listCode_clickEventListener);
			}
		}
	};
	
	// ============================== 동작 컨트롤 ==============================
	
	// ============================== 동작 컨트롤 : 외부 등록 ==============================
	/**
	 * 고객정보 검색 서비스 호출 결과를 핸들링
	 */
	ctrl.getCustInfoCallSucceed = function(returnData) {
		var self = et.callInfo;
		var data = returnData.data;
		if (!!data) {
			self.callData.cust_id = data.cust_id;
			self.callData.cust_nm = data.cust_nm;
			self.callData.hand_tel = data.hand_tel;
			self.callData.class_1 = data.class_1;
			self.callData.class_2 = data.class_2;
			self.callData.class_3 = data.class_3;
			$("#"+self.name+"_iptCust_id").val(data.cust_id);
			$("#"+self.name+"_iptCust_nm").val(data.cust_nm);
			$("#"+self.name+"_iptHand_tel").val(data.hand_tel);
			$("#"+self.name+"_iptClass_1").val(data.class_1);
			$("#"+self.name+"_iptClass_2").val(data.class_2);
			$("#"+self.name+"_iptClass_3").val(data.class_3);
		}
	};
	
	/**
	 * 저장하기 위한 데이터 담기
	 */
	ctrl.makeSaveParam = function(type) {
		var self = et.callInfo;
		self.callData.saveType = type;
		
		if (type === "cust" || type === "both") {
			self.callData.cust_nm = $("#"+self.name+"_iptCust_nm").val();
			self.callData.hand_tel = $("#"+self.name+"_iptHand_tel").val();
			self.callData.class_1 = $("#"+self.name+"_iptClass_1").val();
			self.callData.class_2 = $("#"+self.name+"_iptClass_2").val();
			self.callData.class_3 = $("#"+self.name+"_iptClass_3").val();
		}
		
		if (type === "call" || type === "both") {
			self.callData.state_cd = $("#"+self.name+"_selExec_state > li").filter(".sel").data("cd");
			self.callData.call_etm = self.afterCallServiceStartTime === null ? $.format.date(new Date(), 'yyyy-MM-dd HH:mm:ss') : self.afterCallServiceStartTime; 
			self.callData.cr_cd1 = $("#"+self.name+"_ulCrCd1 > li").filter(".sel").data("cd");
			self.callData.cr_cd2 = $("#"+self.name+"_ulCrCd2 > li").filter(".sel").data("cd");
			self.callData.cr_cd3 = $("#"+self.name+"_ulCrCd3 > li").filter(".sel").data("cd");
//			self.callData.cr_cd4 = $("#"+self.name+"_ulCrCd4 > li").filter(".sel").data("cd");
			self.callData.cr_mm = $("#"+self.name+"_taCr_mm").val().replace(/\n/g, '<br/>');
			self.callData.cr_mm = $("#"+self.name+"_taCr_mm_add").val().replace(/\n/g, '<br/>');
		}
	};
	
	/**
	 * 저장 여부 판단
	 */
	ctrl.checkSaveStatus = function() {
		var self = et.callInfo;
		if (!self.isSaved && self.statusBefore === BP_STATE.AFTER_CALL_WORK) {
			if (!!self.callData) {
				self.isAutoSave = true;
				et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "상담을 자동 저장합니다.", function() {
					self.makeSaveParam(et.getCheckboxYN("#"+self.name+"_cbCustInfoSave")==="Y"?"both":"call");
					new ETService().setSuccessFunction(self.saveCallSucceed).callService(self.path + "/saveCallInfo", self.callData);
				});
			}
		}
	};

	/**
	 * 저장 서비스 호출 결과를 핸들링.
	 */
	ctrl.saveCallSucceed = function(returnData) {
		var self = et.callInfo;
		var msg = returnData.resultMsg;
		var data = returnData.data;
		if (msg === ETCONST.SUCCESS) {
			if(data.saveType !== "cust") {
				self.isSaved = true;
				
				$("#"+self.name+"_btnSaveCustInfo").attr("disabled", "disabled");
				$("#"+self.name+"_btnSaveCallInfo").attr("disabled", "disabled");
				self.afterCallServiceStartTime = null;
			}
			et.home.showMessageAlert("저장되었습니다.");
			
			// 상담 목록 재검색
			// et.history.createDatatable("click");
			
			// 자동저장일 경우 초기화
			if (self.isAutoSave) {
				self.afterCallServiceStartTime = null;
				$("#"+self.name+"_iptCust_id").val("");
				$("#"+self.name+"_iptCust_nm").val("");
				$("#"+self.name+"_iptHand_tel").val("");
				$("#"+self.name+"_btnClearCallInfo").trigger("click");
			}
		}
	};
	
	// ============================== 이벤트 리스너 ==============================
	/**
	 * 전화 하기 버튼 클릭
	 */
	ctrl.btnDial_clickEventListener = _.debounce(function() {
		var self = et.callInfo;
		var telNumber = $("#"+self.name+"_iptHand_tel").val();
		if (telNumber !== "") {
			telNumber = "9"+telNumber;
			window.bpspat.api.dialNumber(telNumber);
		}
	},500);
	
	
	/**
	 * 대분류 코드, 중분류 코드 클릭
	 */
	ctrl.listCode_clickEventListener = function(e) {
		var self = et.callInfo;
		
		var id = e.currentTarget.id;
		var cd = $("#"+id).data("cd");
		var num = $("#"+id).data("num");
		var codeList;
		
		if (num === "1") {
			$("#"+self.name+"_ulCrCd1 > li").removeClass("sel");
			$("#"+id).addClass("sel");
			
			codeList = et.getCodeList("010", cd);
			$("#"+self.name+"_ulCrCd3").empty();
//			$("#"+self.name+"_ulCrCd4").empty();
			if (!!codeList) {
				self.makeCodeList("#"+self.name+"_ulCrCd2", codeList);
				$("#"+self.name+"_ulCrCd2 > li:eq(0)").addClass("sel").trigger("click");
			} else {
				$("#"+self.name+"_ulCrCd2").empty();
			}
		} else if (num === "2") {
			var selCd = $("#"+self.name+"_ulCrCd1 > li").filter(".sel").data("cd");
			$("#"+self.name+"_ulCrCd2 > li").removeClass("sel");
			$("#"+id).addClass("sel");
			
			codeList = et.getCodeList("010", selCd, cd);
//			$("#"+self.name+"_ulCrCd4").empty();
			if (!!codeList) {
				self.makeCodeList("#"+self.name+"_ulCrCd3", codeList);
				$("#"+self.name+"_ulCrCd3 > li:eq(0)").addClass("sel").trigger("click");
			} else {
				$("#"+self.name+"_ulCrCd3").empty();
			}
		} else if (num === "3") {
			var selCd1 = $("#"+self.name+"_ulCrCd1 > li").filter(".sel").data("cd");
			var selCd2 = $("#"+self.name+"_ulCrCd2 > li").filter(".sel").data("cd");
			$("#"+self.name+"_ulCrCd3 > li").removeClass("sel");
			$("#"+id).addClass("sel");
			
			codeList = et.getCodeList("010", selCd1, selCd2, cd);
			if (!!codeList) {
//				self.makeCodeList("#"+self.name+"_ulCrCd4", codeList);
//				$("#"+self.name+"_ulCrCd4").find("li:eq(0)").addClass("sel");
			} else {
//				$("#"+self.name+"_ulCrCd4").empty();
			}
		} else if (num === "4") {
//			$("#"+self.name+"_ulCrCd4 > li").removeClass("sel");
//			$("#"+id).addClass("sel");
		}
	};
	
	/**
	 * 통화결과 클릭
	 */
	ctrl.state_clickEventListener = function(e) {
		var self = et.callInfo;
		var id = e.currentTarget.id;
		
		$("#"+self.name+"_selExec_state > li").removeClass("sel");
		$("#"+id).addClass("sel");
	};
	
	/**
	 * 고객정보 저장 버튼 클릭
	 */
	ctrl.btnSaveCustInfo_clickEventListener = _.debounce(function(e) {
		var self = et.callInfo;
		
		if ($("#"+self.name+"_btnSaveCustInfo").is("[disabled=disabled]")) {
			return;
		}
		if (!!self.callData) {
			self.makeSaveParam("cust");
			new ETService().setSuccessFunction(self.saveCallSucceed).callService(self.path + "/saveCustInfo", self.callData);
		}
	},500);
	
	
	/**
	 * 상담저장 초기화 버튼 클릭
	 */
	ctrl.btnClearCallInfo_clickEventListener = function(e) {
		var self = et.callInfo;
		
		if (!self.isAutoSave && $("#"+self.name+"_btnClearCallInfo").is("[disabled=disabled]")) {
			return;
		}
		
		$(".ListCodeWrap").scrollTop(0);
		$("#"+self.name+"_ulCrCd1 > li:eq(0)").addClass("sel").trigger("click");
		$("#"+self.name+"_selExec_state > li:eq(0)").addClass("sel").trigger("click");
		$("#"+self.name+"_taCr_mm").val("");
		$("#"+self.name+"_taCr_mm_add").val("");
		
		if (self.isAutoSave) {
			self.isAutoSave = false;
		}
	};

	/**
	 * 상담저장 저장 버튼 클릭
	 */
	ctrl.btnSaveCallInfo_clickEventListener = _.debounce(function(e) {
		var self = et.callInfo;
		
		if ($("#"+self.name+"_btnSaveCallInfo").is("[disabled=disabled]")) {
			return;
		}
		if (!!self.callData) {
			self.makeSaveParam(et.getCheckboxYN("#"+self.name+"_cbCustInfoSave")==="Y"?"both":"call");
			new ETService().setSuccessFunction(self.saveCallSucceed).callService(self.path + "/saveCallInfo", self.callData);
		}
	},500);
	
	/**
	 * [모달]악성민원인 등록 버튼 클릭
	 */
	ctrl.btnBlackList_clickHandler = _.debounce(function(e) {
		var self = et.callInfo;-
		// 악성민원인 초기화
		$("#"+self.name+"_iptBlackListHandTel").val($("#"+self.name+"_iptHand_tel").val());
		$("input:radio[name='"+self.name+"_raBlack']:radio[value='01']").prop('checked', true);
		$("#divRegiste").css("left","");
		$("#divRegiste").css("top","");
		$("#divRegiste").show();
	}, 500);
	
	/**
	 * [메인]악성민원인 등록 버튼 클릭
	 */
	ctrl.btnBlackListSave_clickHandler = _.debounce(function(e) {
		var self = et.callInfo;
		var handTel = $("#"+self.name+"_iptBlackListHandTel").val();
		if ($.trim(handTel) === "") {
			et.alert.show(ETCONST.ALERT_TYPE_INFO,"","악성 민원인 전화번호가 없습니다.");
			return;
		}
		var params = {};
		params.blk_reg_phone = handTel;
		params.blk_cd = $("input[name='"+self.name+"_raBlack']:checked").val();
		if (et.home && et.home.custInfo) {
			if ($.trim($("#"+self.name+"_iptHand_tel").val()) !== "") {
				if (et.home.custInfo.global_id && et.home.custInfo.item_id) {
					var url = et.home.api_url+"/admin/?";
					var giid = et.home.custInfo.global_id;
					var stepid = et.home.custInfo.item_id;
					stepid = stepid.replace(/-/g, '');
					var voiceParams = "giid="+giid+"&stepid="+stepid+"#QM_CDR_RESULTS:QM_REVIEW_VOICE";
					params.blk_voice_url = url+voiceParams; 
				}
			}
		}
		new ETService().setSuccessFunction(self.saveBlackListCallSucceed).callService(self.path + "/saveBlackList", params);
	}, 500);
	
	ctrl.saveBlackListCallSucceed = function(returnData) {
		var self = et.callInfo;
		var msg = returnData.message;
		
		if (msg === ETCONST.SUCCESS) {
			et.home.showMessageAlert("악성민원인이 등록되었습니다.");
		} else {
			et.alert.show(ETCONST.ALERT_TYPE_INFO,"","악성 민원인이 등록되지 않았습니다.");
		}
		$("#divRegiste").hide();
	}
	
	
	/**
	 * 고객 정보 조회 팝업 클리어
	 */
	ctrl.clearDivUserInfo = function() {
		$("#home_user_iptName").val("");
		$("#home_user_iptPhoneNum").val("");
		$("#home_iptCust_nm").val("미등록 고객");
		$("#home_iptHand_tel").val("");
		$("#home_iptHome_tel").val("");
		$("#home_iptOffice_tel").val("");
		$("#home_iptEtc_tel").val("");
		$("#home_selGrade").find("option:eq(0)").prop("selected", true);
		
		$("#home_user_tbList").DataTable().clear().destroy();
	};
	return ctrl;
}));