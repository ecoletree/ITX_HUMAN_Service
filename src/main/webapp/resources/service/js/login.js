/*******************************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * @Author : dongsuk
 * @CreateDate : 2019. 12. 30.
 * @DESC : 로그인
 ******************************************************************************/
(function(et, ctrl) {
	if (_.isObject(et) && et.name === ETCONST.PROJECT_NAME) {
		if (!et.vc || et.vc.name !== "login") {
			et.vc = ctrl(et);
		}
	} else {
		console.error("ecoletree OR ETCONST is not valid. please check that common.js file was imported.");
	}
}(this.ecoletree, function(et) {
	
	"use strict";
	
	var ctrl = {};
	
	ctrl.name = "login";
	ctrl.path = "/login";
	
	ctrl.initData = null;
	ctrl.lockTimer = null;
	ctrl.tmrInfo = null;
	
	ctrl.pattern = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;

	// ============================== 화면 컨트롤 ==============================
	/**
	 * init VIEW
	 */
	ctrl.init = function(initData) {
		var self = this;
		
		self.initData = initData;
		
		// set event
		$("#login_btnLogin").on("click", self.btnLogin_clickEventListener);
		$("#login_btnSubmit_I").on("click", self.btnSubmit_clickEventListener);
		$("#login_btnSubmit_O").on("click", self.btnSubmit_clickEventListener);
		$("#login_div1_btnSave").on("click", self.btnSave_clickEventListener);
		$("#login_div2_btnSave").on("click", self.btnSave_clickEventListener);
		$("#login_div3_btnSave").on("click", self.btnSave_clickEventListener);

		et.setEnterKeyDownEvent("#login_iptPW", self.btnLogin_clickEventListener);
		et.setEnterKeyDownEvent("#login_div1_iptNewPWConfirm", self.btnSave_clickEventListener);
		et.setEnterKeyDownEvent("#login_div2_iptNewPWConfirm", self.btnSave_clickEventListener);
		et.setEnterKeyDownEvent("#login_div3_iptNewPWConfirm", self.btnSave_clickEventListener);
	};
	
	// ============================== 동작 컨트롤 ==============================
	
	// ============================== 동작 컨트롤 : 외부 등록 ==============================
	/**
	 * 로그인 서비스 호출 결과를 핸들링.
	 */
	ctrl.loginCallSucceed = function(returnData) {
		var self = et.vc;
		var msg = returnData.resultMsg;
		var data = returnData.data;
		self.tmrInfo = data;
		
		$("#login_btnLogin").removeAttr('disabled');
		$("#login_hiddenType").val(msg);
		
		if (msg === ETCONST.SUCCESS) {
			self.showURLSelectDiv();
		
		} else if (msg === "no_match_data") {
			et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "로그인 정보를 확인해주세요.");
			
		} else if (msg === "locked_account") {
			et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "잘못된 정보로 5회의 로그인 시도를 하였습니다. 3분 후 로그인이 가능합니다.");
			// 3분 타이머 시작
			$("#login_btnLogin").attr("disabled", "disabled");
			self.lockTimer = setTimeout(function() {
				if (self.lockTimer !== null) {
					new ETService().setSuccessFunction(self.resetFailCountSucceed).callService(self.path + "/resetFailCount", data);
				}
			}, 3 * 60 * 1000);
		
		} else if (msg === "login_user") {
			et.alert.showCustomBtn(ETCONST.ALERT_TYPE_CONFIRM, "", "다른 컴퓨터에 이미 로그인되어 있습니다. \n해당 컴퓨터를 로그아웃 처리 후 로그인 하시겠습니까?", ["네", "아니요"], function(e) {
				new ETService().setSuccessFunction(self.closeMultiLoginSucceed).callService(self.path + "/closeMultiLogin", data);
			}, null);
		
		} else if (msg === "initialized_pw") {
			// 최초 로그인 or 비밀번호 초기화된 계정
			$("#login_div1").show();
			$("#login_div1_iptID").val(data.tmr_id);
		
		} else if (msg === "close_account") {
			// 14일 미접속 계정
			$("#login_div3").show();
			$("#login_div3_iptID").val(data.tmr_id);
	
		} else if (msg === "change_pw") {
			// 비밀번호 변경 30일 계정
			$("#login_div2").show();
			$("#login_div2_iptID").val(data.tmr_id);
		} 
	};
	
	/**
	 * 멀티 로그인 방지 서비스 호출 결과를 핸들링.
	 */
	ctrl.closeMultiLoginSucceed = function(returnData) {
		var self = et.vc;
		
		if (returnData.message === ETCONST.SUCCESS) {
			self.showURLSelectDiv();
		} else {
			et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "다시 시도해주세요.");
		}
	};

	/**
	 * 로그인 실패 카운트 초기화 서비스 호출 결과를 핸들링.
	 */
	ctrl.resetFailCountSucceed = function(returnData) {
		var self = et.vc;
		var msg = returnData.resultMsg;
		
		if (msg === ETCONST.SUCCESS) {
			$("#login_btnLogin").removeAttr("disabled");
			clearTimeout(self.lockTimer);
		} else {
			et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "다시 시도해주세요.");
		}
	};
	
	/**
	 * 비밀번호 변경 서비스 호출 결과를 핸들링.
	 */
	ctrl.changePWSucceed = function(returnData) {
		var self = et.vc;
		var msg = returnData.resultMsg;
		var data = returnData.data;
		
		$("#login_div1").hide();
		$("#login_div2").hide();
		$("#login_div3").hide();
		$("#login_div1_btnSave").removeAttr("disabled");
		$("#login_div2_btnSave").removeAttr("disabled");
		$("#login_div3_btnSave").removeAttr("disabled");
		
		if (msg === ETCONST.SUCCESS) {
			if (data.type === "close_account") {
				self.showURLSelectDiv();
			} else {
				et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "비밀번호가 변경되었습니다. 다시 로그인 해주세요.");
			}
			
		} else {
			et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "다시 시도해주세요.");
		}
	};
	
	// ============================== 이벤트 ==============================
	/**
	 * 로그인 버튼 클릭
	 */
	ctrl.btnLogin_clickEventListener = _.debounce(function(e) {
		var self = et.vc;
		
		if ($("#login_btnLogin").is("[disabled=disabled]")) {
			return;
		}
		
		var postData = {
				tmr_id : $("#login_iptID").val().trim(),
				tmr_pw : $("#login_iptPW").val().trim(),
			};
			if(postData.tmr_id === "" || postData.tmr_pw === "") {
				et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "로그인 정보를 확인해주세요.");
				return;
			} 

			var tempPw = postData.tmr_pw;
			if (self.pattern.test(tempPw)) {
				tempPw = tempPw.replace(self.pattern, "");
			}
			if (!(new RegExp("[a-zA-Z]").test(tempPw) && new RegExp("[0-9]+").test(tempPw))) {
				et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "로그인 정보를 확인해주세요.");
				return;
			} 

			if (!new RegExp("^.{10,}$").test(postData.tmr_pw)) {
				et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "로그인 정보를 확인해주세요.");
				return;
			}
			
			$("#login_btnLogin").attr("disabled", "disabled");
			
			postData.tmr_pw = btoa(postData.tmr_pw);
			new ETService().setSuccessFunction(self.loginCallSucceed).callService(self.path + "/doLogin", {cryptoInfo:et.cryptoAES(postData)});
			
	},500);
	
	/**
	 * bp url, 인/아웃바운드 선택확인
	 */
	ctrl.btnSubmit_clickEventListener = _.debounce(function(e) {
		var self = et.vc;
		var type = (e.currentTarget.id).replace("login_btnSubmit_", "");
		
		var postData = {
				bp_url : $("input:radio[name='url']:checked").val(),
				biz_type : type,
		};
		
		ctrl.moveToPledge(postData);
	},500);
	
	/**
	 * 비밀번호 변경, 잠금해제 팝업에서 저장
	 */
	ctrl.btnSave_clickEventListener = _.debounce(function(e) {
		var self = et.vc;
		var tmrInfo = self.tmrInfo;
		var divId = (e.currentTarget.id).replace("btnSave", "");

		var id = $("#"+divId+"iptID").val().trim();
		var oldPw = $("#"+divId+"iptOldPW").val().trim();
		var newPw = $("#"+divId+"iptNewPW").val().trim();
		var confirmPw = $("#"+divId+"iptNewPWConfirm").val().trim();
		
		if (id === "" || newPw === "" || confirmPw === "") {
			et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "값을 입력해주세요.");
			return;
		}
		if (id === newPw) {
			et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "아이디와 비밀번호는 같을 수 없습니다.");
			return;
		}
		if (oldPw === newPw) {
			et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "기존 비밀번호를 사용할 수 없습니다.");
			return;
		}
		
		var tempPw = newPw;
		if (self.pattern.test(tempPw)) {
			tempPw = tempPw.replace(self.pattern, "");
		}
		if (!(new RegExp("[a-zA-Z]").test(tempPw) && new RegExp("[0-9]+").test(tempPw))) {
			et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "비밀번호는 영문자와 숫자를 조합하여 10자리 이상으로 설정해주세요.");
			return;
		}
		if (!new RegExp("^.{10,}$").test(newPw)) {
			et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "비밀번호는 영문자와 숫자를 조합하여 10자리 이상으로 설정해주세요.");
			return;
		}
		if (newPw !== confirmPw) {
			et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "신규 비밀번호 확인이 일치하지 않습니다.");
			return;
		}
		
		$("#"+e.currentTarget.id).attr("disabled", "disabled");
		
		tmrInfo.type = $("#login_hiddenType").val();
		tmrInfo.tmr_oldPw = btoa(oldPw);
		tmrInfo.tmr_newPw = btoa(newPw);
		new ETService().setSuccessFunction(self.changePWSucceed).callService(self.path + "/changePW", {cryptoInfo:et.cryptoAES(tmrInfo)});
	},500);
	
	/**
	 * bp url 선택, 인/아웃바운드 선택 div 열기
	 */
	ctrl.showURLSelectDiv = function() {
		var self = et.vc;
		var data = self.initData.codeList;
		
		// url 동적 생성
		$("#login_divUrl").empty();
		var index = 0;
		_.forEach(data, function(item){
			if (index === 0) {
				$("#login_divUrl").append('<label class="ecoleRadio"><input type="radio" name="url" checked="checked" value="'+item.item_nm+'"><i></i>'+item.item_nm+'</label>');
				index++;
			} else {
				$("#login_divUrl").append('<label class="ecoleRadio"><input type="radio" name="url" value="'+item.item_nm+'"><i></i>'+item.item_nm+'</label>');
			}
		});
		$("#login_url").show();
	};
	
	/**
	 * 로그인 성공으로 서약서 작성화면으로 이동
	 */
	ctrl.moveToPledge = function(postData) {
		var self = et.vc;
		
		// 로그인 성공
		if (et.isMobile()) {
			et.scsLogin(self.tmrInfo,"login");
		}
		new ETService().callView("/pledge", postData);
	};
	
	return ctrl;
}));