/*******************************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * @Author : aileen
 * @CreateDate : 2018. 08. 06.
 * @DESC : 홈 _ 메인
 ******************************************************************************/
(function(et, ctrl) {
	if (_.isObject(et) && et.name === ETCONST.PROJECT_NAME) {
		if (!et.home || et.vc.name !== "home") {
			et.home = ctrl(et);
		}
	} else {
		console.error("ecoletree OR ETCONST is not valid. please check that common.js file was imported.");
	}
}(this.ecoletree, function(et) {
	
	"use strict";
	
	var ctrl = {};
	
	ctrl.name = "home";
	ctrl.path = "";
	ctrl.popup = null;
	ctrl.data = null; // 넘겨받은 데이터 저장용
	ctrl.customer = null; // 전화온 고객 정보 저장용
	ctrl.intervalID = null;
	ctrl.isFirst = false;
	ctrl.popupObj = {};
	ctrl.noticeData = null;
	ctrl.emergencyData = null;
	ctrl.saveCallId = null;
	
	ctrl.VIEW_STATUS = "call";
	ctrl.reserveCallbackData = null;
	
	ctrl.callIng = false;
	
	ctrl.custInfo = null;
	
	ctrl.api_url = "https://govsp1.ringcloud.co.kr"; // 효성 리얼서버
//	ctrl.api_url = "https://dev1.hsitx-ccc.com"; // 효성 리얼서버
//	ctrl.api_url = "https://hsitx-ccc.ga"; // 효성 테스트서버
//	ctrl.api_url = "https://hsitx-app.ga"; // 이콜트리 테스트서버
	
	
	// 메시지 알랏을 자동으로 사라지게 하기위한 타이머 객체
	ctrl.messageAlertTimer = null; 
	
	ctrl.TEL_PRIFIX_NUM = 9;
	
	// 어드민 콜백 경로
	ctrl.admin_path = "http://localhost:8080/ITX_HUMAN_Admin/login/doLoginSSO?tmr_id=";
	
	// ============================== 화면 컨트롤 ==============================
	/**
	 * init VIEW
	 */
	ctrl.init = function(initData, api_url) {
		var self = et.home;
		self.data = initData;
		
		self.api_url = api_url;
		
		// 나중에 서버에 붙힐때만
		try {
			self.setBpspatHandler();
		} catch(error){
			console.log(error);
		}
		
		$("#"+self.name+"_divCallBack").draggable({cancel:'.popupBody'});
		$("#"+self.name+"_divNotice").draggable({cancel:'.popupBody'});
		$("#"+self.name+"_divEmergency").draggable({cancel:'.popupBody'});
		$("#divRegiste").draggable({cancel:'.popupBody'});
		
		$("#"+self.name+"_imgTalk").click(self.shortCutClickHandler);
		$("#"+self.name+"_imgCallBack").click(self.shortCutClickHandler);
		$("#"+self.name+"_imgNotice").click(self.shortCutClickHandler);
		$("#"+self.name+"_healthCenter").click(self.shortCutClickHandler);
		$("#"+self.name+"_imgEmergency").click(self.shortCutClickHandler);
		$("#"+self.name+"_emergency").click(self.shortCutClickHandler);
		$("#"+self.name+"_satisfaction").click(self.shortCutClickHandler);
		
		$("#"+self.name+"_CBRefresh").click(self.openCallBackModal);
		$("#"+self.name+"_NotiRefresh").click(self.openNoticeModal);
		$("#"+self.name+"_EmergencyRefresh").click(self.openEmergencyModal);
		$("#"+self.name+"_noti_search").click(self.openNoticeModal);
		
		$("#"+self.name+"_btnLogout").click(function () {
			// 모바일인지 체크후에 SCS 에 접속
			self.logout();
		});
		
		$("#"+self.name+"_noti_searchData").on("keydown",function(e){
			if (e.which == 13) {
                e.preventDefault();
                self.openNoticeModal();
            }
		});
		
		if (self.intervalID !== null) {
			clearInterval(self.intervalID);
		}
		
		window.addEventListener("message", self.receiveMessage);		
		self.intervalID = setInterval(self.getNewCallBackNotice, 5*60*1000);
		self.getCodeList();
		self.getNewCallBackNotice();
		
		et.setDataTableRowSelection("#"+self.name+"_cb_tbList", self.cbTBListRowSelect);
		et.setDataTableRowSelection("#"+self.name+"_noti_tbList", self.noticeTBListRowSelect);
		et.setDataTableRowSelection("#"+self.name+"_emergency_tbList", self.emergencyTBListRowSelect);
		
		
		$("#"+self.name+"_frm").submit(function( event ) {
			et.alert.show(ETCONST.ALERT_TYPE_INFO,"","만족도 조사 메시지를 전달하였습니다.");
		});
		
		// sso 연동
		$("#home_goAdminCallBack").attr("href", self.admin_path + btoa(initData.tmr_info.tmr_id));
		
	};
	
	// ============================== 동작 컨트롤 ==============================
	
	/**
	 * 콜백, 예약콜, 공지사항 코드정보를 가져온다
	 */
	ctrl.getCodeList = _.debounce(function () {
		var self = et.home;
		var params = {};
		new ETService().setSuccessFunction(self.getCodeListCallSucceed).callService(self.path + "/getCodeList", params);
	},500);
	
	ctrl.getCodeListCallSucceed = function (result) {
		var self = et.home;
		if (result.message === ETCONST.SUCCESS) {
			ITX_CODE = result.data.list;
			ITX_ALL_CODE = result.data.allList;
			ITX_TEAM = result.data.teamList;
			
			//나중에 서버에 붙힐때만
			try {
				window.bpspat.api.getState(function (data) {
					console.log(data);
					if (data.data.agent_id !== $("#home_spTmrId").text()){
						et.alert.showModal(ETCONST.ALERT_TYPE_INFO,"","AGENT 아이디와 솔루션 아이디가 서로 다릅니다. 로그아웃합니다.", function (e){
							self.logout();
						});
					} else {
						if (et.history != null && et.history != undefined) {
							et.history.setStatus(data.data.status);
						}
						if (et.callInfo != null && et.callInfo != undefined) {
							et.callInfo.setStatus(data.data.status);
						}
						if (et.historyDetail != null && et.historyDetail != undefined) {
							et.historyDetail.setStatus(data.data.status);
						}
						if (et.shortCutClickHandler != null && et.shortCutClickHandler != undefined) {
							et.shortCutClickHandler.setStatus(data.data.status);
						}
						
						var status = data.data.status;
						var reason = data.data.reason;
						var obj = {};
						if (status === BP_STATE.READY) {
							obj.state = "0";
						} else if (status === BP_STATE.AFTER_CALL_WORK) {
							obj.state = "13";
						} else if (status === BP_STATE.RINGING || status === BP_STATE.BUSY) {
							obj.state = "12";
						} else if (status === BP_STATE.NOT_READY) {
							if (reason === "Away from Desk") {
								obj.state = "2";
							} else if (reason === "Lunch") {
								obj.state = "3";
							} else if (reason === "Break") {
								obj.state = "4";
							} else if (reason === "Logout Pending") {
								obj.state = "999";
							} else {
								obj.state = "2";
							}
						} else {
							obj.state = "2";
						}
						
						if (obj.state !== "999") {
							self.setTMRStatus(obj);
						}
						
						
						if (status === BP_STATE.READY) {  // 상태가 ready 이면 그때 고객 정보를 초기화
							self.customer = {};
						} else if (status === BP_STATE.BUSY) {
							self.customer = {};
							self.isFirst = true;
							self.callIng = true;
							var interactions = data.data.interactions;
							
							self.customer.item_id = interactions[0].item_id;
							self.customer.global_id = interactions[0].global_id;
							self.customer.hand_tel = interactions[0].phone_number;
							
							window.bpspat.api.addCallRecordingStatusHandler(self.addCallRecordingStatusHandler);
							window.bpspat.api.getCallRecordingStatus(interactions[0].item_id);
						}
					}
					
				});
			} catch(error){
				console.log(error);
			}
			
			if (et.history != null && et.history != undefined) {
				et.history.setCodeData();
			}
			if (et.callInfo != null && et.callInfo != undefined) {
				et.callInfo.setCodeData();
			}
			if (et.historyDetail != null && et.historyDetail != undefined) {
				et.historyDetail.setCodeData();
			}
		} else {
			// 코드를 못가져왔을경우
		}
	}
	
	/**
	 * 콜백, 예약콜, 공지사항 정보를 가져온다
	 */
	ctrl.getNewCallBackNotice = _.debounce(function () {
		var self = et.home;
		var params = {};
		new ETService().setSuccessFunction(self.getCallBackNoticeCountCallSucceed).callService(self.path + "/getCallBackNoticeCount", params);
	},500);
	
	ctrl.getCallBackNoticeCountCallSucceed = function (result) {
		var self = et.home;
		if (result.message === ETCONST.SUCCESS) {
			// 상단에 공지 사항, 콜백 count를 보여준다
			$("#"+self.name+"_spCallBackCnt").html(result.data.callbackCount);
			if (0 < result.data.noticeCount) {
				$(".bxNoticeAlert").show();
			} else {
				$(".bxNoticeAlert").hide();
			}
			$("#"+self.name+"_spNoticeCnt").html(result.data.noticeCount);
			if (0 < result.data.ucaseCount) {
				$(".bxEmergencyAlert").show();
			} else {
				$(".bxEmergencyAlert").hide();
			}
			$("#"+self.name+"_spUCaseCnt").html(result.data.ucaseCount);
			
			if (0 < result.data.blackListCount) {
				$("#home_divBlackList").show();
			} else {
				$("#home_divBlackList").hide();
			}
			
		} else {
			
		}
	}
	
	/**
	 * 테스트로 bp와 연결
	 */
	ctrl.testBpspatHandler = function () {
		var self = et.home;
		var obj = {command:BP_COMMAND.STATE_CHANGED,data:{status:"ready"}};
		self.bpspatHandler(obj);
		obj = {command:"INTERACTION_RENDERED",data:{item_id:"74423C5C-6307-4F91-9C44-46CE96BA9D16",global_id:"5AFD43A8-59E9-4E7D-BC10-6B083776765C",phone_number:"01046868851"}};
		self.bpspatHandler(obj);
		if (this.id === self.name+"_imgCallBack") {
			obj = {command:"_I_CALL_ANSWERED",data:{call_id:"callID"+(new Date().getTime()),io_flag:"I"}};
		} else {
			self.customer = {};
			obj = {command:BP_COMMAND.CALL_RECORDING_STATUS,data:{call_id:"clll"}};
		}
		self.bpspatHandler(obj);
	}

	/**
	 * 	 BP Handler 등록
	 */
	ctrl.setBpspatHandler = function () {
		var self = et.home;
		// 리얼서버
		//window.bpspat.api.init("https://sp1.hsitx-ccc.com");
		// 테스트 서버
		window.bpspat.api.init(self.api_url);
		
		window.bpspat.api.addAgentLoginHandler(self.bpspatHandler);
		window.bpspat.api.addInteractionCompletedHandler(self.bpspatHandler);
		window.bpspat.api.addInboundCallOfferedHandler(self.bpspatHandler);
		window.bpspat.api.addStatusChangeHandler(self.bpspatHandler);
		window.bpspat.api.addInteractionRenderedHandler(self.bpspatHandler);
		window.bpspat.api.addInboundCallAnsweredHandler(self.bpspatHandler);
		window.bpspat.api.addOutboundCallDialingHandler(self.bpspatHandler);
		window.bpspat.api.addCallEndedHandler(self.bpspatHandler);
		window.bpspat.api.addOutboundCallConnectedHandler(self.bpspatHandler);
		window.bpspat.api.addCallHeldHandler(self.bpspatHandler);
		window.bpspat.api.addCallResumedHandler(self.bpspatHandler);
		window.bpspat.api.addBlindTransferHandler(self.bpspatHandler);
		window.bpspat.api.addConsultTransferHandler(self.bpspatHandler);
		window.bpspat.api.addCallRecordingStatusHandler(self.bpspatHandler);
		window.bpspat.api.addScreenRecordingStatusHandler(self.bpspatHandler);
	}
	
	// ============================== 동작 컨트롤 : 외부 등록 ==============================
	/**
	 * BP 의 이벤트 핸들러
	 */
	ctrl.bpspatHandler = function (data) {
		var self = et.home;
		console.log(data);
		try {
			if (data.command === BP_COMMAND.STATE_CHANGED ) { // BP 왼쪽 상단에 상담사 현재 상태를 변경 했을때.. READY 면 모든걸 초기화 시켜준다
				if (data.data.status === BP_STATE.READY) {  // 상태가 ready 이면 그때 고객 정보를 초기화
					self.customer = {};
					self.custInfo = null;
				}
				
				// BP 왼쪽 상단에 상담사 현재 상태를 변경 했을때.. 각 DIV 에 현재의 상태 값을 넘겨준다
				if (et.history != null && et.history != undefined) {
					et.history.setStatus(data.data.status);
				}
				if (et.callInfo != null && et.callInfo != undefined) {
					et.callInfo.setStatus(data.data.status);
				}
				if (et.historyDetail != null && et.historyDetail != undefined) {
					et.historyDetail.setStatus(data.data.status);
				}
				if (et.shortCutClickHandler != null && et.shortCutClickHandler != undefined) {
					et.shortCutClickHandler.setStatus(data.data.status);
				}
				
				var status = data.data.status;
				var reason = data.data.reason;
				var obj = {};
				if (status === BP_STATE.READY) {
					self.custInfo = null;
					obj.state = "0";
				} else if (status === BP_STATE.AFTER_CALL_WORK) {
					obj.state = "13";
				} else if (status === BP_STATE.RINGING || status === BP_STATE.BUSY) {
					obj.state = "12";
				} else if (status === BP_STATE.NOT_READY) {
					if (reason === "Away from Desk") {
						obj.state = "2";
					} else if (reason === "Lunch") {
						obj.state = "3";
					} else if (reason === "Break") {
						obj.state = "4";
					} else if (reason === "Logout Pending") {
						obj.state = "999";
					} else {
						obj.state = "2";
					}
				} else {
					obj.state = "2";
				}
				if (obj.state !== "999") {
					// CTI를 위해서 사용 (모니터링..)
					self.setTMRStatus(obj);
				}
				
			} else if (data.command === BP_COMMAND.INTERACTION_RENDERED ) {
				// inbound 에서 전화벨일 울릴때
				if (self.customer === undefined || self.customer === null) {
					self.customer = {};
				}
				if (!self.callIng) {
					self.customer.item_id = data.data.item_id;
					self.customer.global_id = data.data.global_id;
					self.customer.hand_tel = data.data.phone_number;
					if (self.customer.call_id !== undefined) {
						if (self.customer.io_flag === "O" && self.customer.item_id !== undefined && self.customer.global_id !== undefined && self.customer.hand_tel !== undefined) {
							self.callIng = true;
							self.sendCustomerInfo();
						}
					}
				}
			} else if (data.command === BP_COMMAND.I_CALL_ANSWERED) {
				// 전화를 받았을 경우 (실제 통화중)
				// inbound에서만 발생
				self.customer.call_id = data.data.call_id;
				self.customer.io_flag = "I";
				self.VIEW_STATUS = "call";
				self.reserveCallbackData = null;
				if (self.customer.item_id !== undefined && self.customer.global_id !== undefined && self.customer.hand_tel !== undefined) {
					self.callIng = true;
					self.sendCustomerInfo();
				}
			} else if (data.command === BP_COMMAND.O_CALL_DIALING) {
				// 전화를 걸었을 경우 (상대방에게 다이얼이 걸어진상태 - 통화대기중)
				// outbound에서만 발생
				if (!self.callIng) {
					if (self.customer === undefined || self.customer === null) {
						self.customer = {};
					}
					self.customer.call_id = data.data.call_id;
					self.customer.io_flag = "O";
				}
			} else if (data.command === BP_COMMAND.CALL_ENDED) {
				// 전화가 끝났을 경우, inbound 시에는 후처리 상태가 발생후 CALL_END 이벤트 발생
				// 통화가 다 끝났으므로 상담을 저장 할 수 있는 상태로 변경 해야한다 DBDate 는 DB에 실제 date 값을 가져온다
				self.customer = {};
				self.VIEW_STATUS = "call";
				self.reserveCallbackData = null;
				self.callIng = false;
				self.getDBDate();
			} else if (data.command === BP_COMMAND.LOGIN_CHANGED) {
				// 로그인 상태변경
				// BP 로그인을 했을 경우엔 ID 비교
				// BP 로그아웃 하면 그냥 시스템도 로그아웃
				if (data.data.login) { // 로그인
					// 아이디와 로그인한 id와 비교 다르면 에러페이지로
					if (data.data.agent_id !== $("#home_spTmrId").text()) {
						et.alert.showModal(ETCONST.ALERT_TYPE_INFO,"","AGENT 아이디와 솔루션 아이디가 서로 다릅니다. 로그아웃합니다.", function (e){
							self.logout();
						});
					}
				} else { // 로그아웃
					// 모바일인지 체크후에 SCS 에 접속
					et.alert.showModal(ETCONST.ALERT_TYPE_INFO,"","BP를 로그아웃하셨습니다. 재로그인 부탁드립니다.", function (e){
						self.logout();
					});
				}
			}
		} catch(error) {
			// 어느 하나라도 에러가 발생되면 초기화면으로 이동
			console.log(error);
			window.location.href = getContextPath();
		}
	}
	
	// 로그아웃 관련 펑션
	ctrl.logout = function () {
		var self = et.home;
		
		var obj = {};
		obj.state = "11";
		self.setTMRStatus(obj);
		if (et.isMobile()) {
			et.scsLogin({},"logout");
		}
		location.href = getContextPath()+"/logout";
		
	}
	
	/**
	 * Status 값을 저장한다
	 */
	ctrl.setTMRStatus = function(status) {
		var self = et.home;
		new ETService().setSuccessFunction(function (data) {}).callService(self.path + "/setTMRStatus", status);
	}
	
	/**
	 * RecordingStatus값을 가져온다 (call id 를 참조 하기 위해서)
	 * 해당 항목은 첫화면이 BUSY 일때만 호출
	 */
	ctrl.addCallRecordingStatusHandler = function (data) {
		var self = et.home;
		console.log(data);
		try {
			if (data.command === BP_COMMAND.CALL_RECORDING_STATUS) {
				if (self.isFirst) {
					if (self.customer !== undefined) {
						self.customer.call_id = data.call_id;
						self.customer.io_flag = "I";
						if (self.customer.item_id !== undefined && self.customer.global_id !== undefined && self.customer.hand_tel !== undefined) {
							self.callIng = true;
							self.sendCustomerInfo();
							self.isFirst = false;
						}
					}
				}
			}
		} catch (error) {
			console.log(error);
			window.location.href = getContextPath();
		}
	}
	
	/**
	 * 전화를 끊었을때 현재 DB 날자를 가져온다. 
	 */
	ctrl.getDBDate = function () {
		var self = et.home;
		new ETService().setSuccessFunction(self.getDBDateCallSucceed).callService(self.path + "/getDBDate", {});
	}
	
	/**
	 * 전화를 끊었을때 현재 DB 날자를 가져온다 콜백 함수. 
	 */
	ctrl.getDBDateCallSucceed = function (result) {
		var self = et.home;
		if (result.message === ETCONST.SUCCESS) {
			if (et.callInfo != null && et.callInfo != undefined) {
				et.callInfo.setCallEndTime(result.data.call_edttm);
			}
			
		}
	}
	
	/**
	 * 전화를 받거나 전화를 걸고 상대방이 받았을때 
	 * 고객정보를 가져온다
	 */
	ctrl.sendCustomerInfo = _.debounce(function () {
		var self = et.home;
		if (self.saveCallId === null || self.saveCallId !== self.customer.call_id) {
			self.saveCallId = self.customer.call_id;
			if (self.customer !== null) {
				if (self.customer.hand_tel !== undefined && self.customer.hand_tel !== null && self.customer.hand_tel !== "") {
					new ETService().setSuccessFunction(self.setCustCallInfoCallSucceed).callService(self.path + "/setCustCallInfo", self.customer);
				}
			}
			
		}
		
	},500);
	
	/**
	 * 고객정보를 가져와서 불려지는 콜백 함수
	 */
	ctrl.setCustCallInfoCallSucceed = function (result) {
		var self = et.home;
		if (result.message === ETCONST.SUCCESS) {
			result.data.viewStatus = self.VIEW_STATUS;
			self.custInfo = result.data; 
			if (self.reserveCallbackData !== null) {
				if (self.reserveCallbackData.cust_id != null && self.reserveCallbackData.cust_id !== undefined) { // 예약
					if (self.reserveCallbackData.cust_id === result.data.cust_id) {
						result.data.reserveCallbackData = self.reserveCallbackData;
					}
				} else {										// 콜백
					if (self.TEL_PRIFIX_NUM+""+self.reserveCallbackData.tel === result.data.hand_tel ||
						self.reserveCallbackData.tel === result.data.hand_tel) {
						result.data.reserveCallbackData = self.reserveCallbackData;
						
					}
				}
			}
			if (et.history != null && et.history != undefined) {
				et.history.sendData(result.data);
			}
			if (et.callInfo != null && et.callInfo != undefined) {
				et.callInfo.sendData(result.data);
			}
		} else {
			
		}
	}
	
	/**
	 * 콜백 리스트 정보를 가져와서 모달화면을 갱신한다
	 */
	ctrl.openCallBackModal = _.debounce(function () {
		var self = et.home;
		new ETService().setSuccessFunction(self.getCallBackListCallSucceed).callService(self.path + "/getCallBackList", {});
	},500);
	
	/**
	 * 콜백 리스트 정보를 가져와서 불려지는 콜백 함수
	 */
	ctrl.getCallBackListCallSucceed = function (result) {
		var self = et.home;
		if (result.message === ETCONST.SUCCESS) {
			$("#"+self.name+"_spCallBackUpdate").html(result.data.update_dttm);
			var columns = [ {
					className : "txtCenter",
					data : "rownum"
				}, {
					className : "txtCenter",
					data : "tel",
					render : function(data, type, full, meta) {
						var str = '<a class="btnBlue btnSmall"><img src="'+getContextPath()+'/resources/ecoletree/img/icon_call.png" data-callnumber="'+full.tel+'" data-calltype="tel" /><span>'+data+'</span></a>';
						return str;
					}
				}, {
					className : "txtCenter",
					data : "key_in_number",
					render : function(data, type, full, meta) {
						var str = '<a class="btnBlue btnSmall"><img src="'+getContextPath()+'/resources/ecoletree/img/icon_call.png" data-callnumber="'+full.key_in_number+'" data-calltype="keyIn" /><span>'+data+'</span></a>';
						return str;
					}
				},  {
					className : "txtCenter",
					data : "service_name",
					render : function(data, type, full, meta) {
						var str = null;
						if(data === null) {
							str = "";
						} else {
							str = '<span>'+data+'</span>';
						}
						return str;
					}
				}, {
					className : "txtCenter",
					data : "view_cb_time",
					render : function(data, type, full, meta) {
						var str = '<span>'+data+'</span>';
						return str;
					}
				}
			];
			self.createTables (columns, "#"+self.name+"_cb_tbList", result.data.list);
		} else {
			
		}
	}
	
	/**
	 * 공지사항 리스트 정보를 가져와서 모달화면을 갱신한다
	 * 공지사항은 DataTables의ajax를 쓰기 때문에 방식을 변경한다 
	 */
	ctrl.openNoticeModal = _.debounce(function () {
		var self = et.home;
		var param = {};
		if ($("#"+self.name+"_noti_searchData").val() !== "") {
			param.search_data = $("#"+self.name+"_noti_searchData").val();
		}
		var columns = [ {
				className : "txtCenter",
				data : "rownum"
			}, {
				className : "tdNoticeTitle",
				data : "subject",
				render : function(data, type, full, meta) {
					var image = "";
					if (full.read_notice_id === null) {
						image = '<img src="'+getContextPath()+'/resources/ecoletree/img/icon_new.png" />';
					}
					var str = '<span>'+data+'</span>' + image;
					return str;
				}
			}
		];
		
		$("#"+self.name+"_noti_tbList").DataTable(et.createDataTableSettings(columns, self.path + "/getNoticeList", param, self.dataTableNoticeDrawCallback,"200px",false));
	},500);
	
	/**
	 * 긴급접수 리스트 정보를 가져와서 모달화면을 갱신한다
	 * 긴급접수은 DataTables의ajax를 쓰기 때문에 방식을 변경한다 
	 */
	ctrl.openEmergencyModal = _.debounce(function () {
		var self = et.home;
		var param = {};
		var columns = [ {
				className : "txtCenter",
				data : "rownum"
			}, {
				className : "txtCenter",
				data : "view_insert_dttm",
			}, {
				className : "txtCenter",
				data : "view_r_code"
			}
		];
		
		$("#"+self.name+"_emergency_tbList").DataTable(et.createDataTableSettings(columns, "/ucase/getUCaseList", param, self.dataTableEmergencyDrawCallback,"200px",false));
	},500);
	
	/**
	 * kakao talk 전송 팝업 열기
	 */
	ctrl.openTalkPopup = _.debounce(function () {
		var self = et.home;
		var popupId = "pop"+Date.now();
		// 팝업 띄우기
		var tempPop = window.open(
							getContextPath()+"/pop.sendTalkPopup.sp", 
							popupId,
							"width=290px, height=610px, location=no, toolbars=no, status=no");
		
		self.popupObj[popupId] = tempPop;
	},500);
	
	/**
	 * 전국 보건소 팝업 열기
	 */
	ctrl.openHealthCenter = _.debounce(function () {
		var self = et.home;
		var popupId = "pop"+Date.now();
		// 팝업 띄우기
		var tempPop = window.open(
				getContextPath()+"/pop.healthCenterPopup.sp", 
				popupId,
		"width=1500px, height=600px, location=no, toolbars=no, status=no");
		
		self.popupObj[popupId] = tempPop;
	},500);
	
	/**
	 * 긴급접수 팝업 열기
	 */
	ctrl.openEmergencyPopup = _.debounce(function () {
		var self = et.home;
		var popupId = "emergencyPopup";
		// 팝업 띄우기
		var tempPop = window.open(
							getContextPath()+"/pop.emergencyPopup.sp", 
							popupId,
							"width=770px, height=770px, location=no, toolbars=no, status=no");
		
		self.popupObj[popupId] = tempPop;
	},500);
	
	/**
	 * 예약콜 과 콜백의 DataTable를 만든다
	 * 두 리스트는 페이지 및 DataTable 의 Apia가 필요 없으므로 
	 * 데이터를 가져와서 넣어준다
	 */
	ctrl.createTables =  function(columns, tableId, dataSet) {
		var self = et.home;
		$(tableId).DataTable(et.createDataTableSettings(columns, null, {}, self.dataTableDrawCallback,"100%",false,dataSet));
	}
	
	/**
	 * 예약콜 과 콜백의 DataTable를 만들고 난 후 콜백함수
	 */
	ctrl.dataTableDrawCallback = function (setting) {
		var self = et.home;
		console.log(setting);
	}
	
	/**
	 * 공지사항 DataTables를 만들고 난 후 콜백 함수
	 */
	ctrl.dataTableNoticeDrawCallback = function (setting) {
		var self = et.home;
		console.log(setting);
		$("#"+self.name+"_spNoticeUpdate").html(setting.json.update_dttm);
	}
	
	/**
	 * 긴급접수 DataTables를 만들고 난 후 콜백 함수
	 */
	ctrl.dataTableEmergencyDrawCallback = function (setting) {
		var self = et.home;
		console.log(setting);
		$("#"+self.name+"_spEmergencyUpdate").html(setting.json.update_dttm);
	}
	
	/**
	 * 홈 화면의 사라지는 알람 제어
	 * 
	 * @param {string} 알람에서 보이고자 하는 text
	 */
	ctrl.showMessageAlert = function(sMsg) {
		var self = et.home;
		
		if (!!self.messageAlertTimer) {
			clearTimeout(self.messageAlertTimer);
			$("#home_divMessageAlert").hide();
			$("#home_divMessageAlert > p").text("");
		}
		
		if (!!sMsg) {
			$("#home_divMessageAlert").show();
			$("#home_divMessageAlert > p").text(sMsg);
			
			self.messageAlertTimer = setTimeout(function() {
				if (!!self.messageAlertTimer) {
					clearTimeout(self.messageAlertTimer);
					$("#home_divMessageAlert").hide();
				}
			}, 3*1000);
		}
	};
	
	/**
	 * 콜백 팝업창에서 table 클릭
	 * 
	 * @param {jQuery} $target 클릭한 대상
	 * @param {} row 행 인덱스
	 * @param {} col 열 인덱스
	 */
	ctrl.cbTBListRowSelect = function($target, row, col) {
		var self = et.home;
		
		if (col === 1 || col === 2) {
			self.VIEW_STATUS = "callback";
			var rowData = et.getRowData("#"+self.name+"_cb_tbList", $target.closest("tr"));
			if ($target.closest("td").find("img").data("calltype") === "keyIn") {
				rowData.tel = rowData.key_in_number;
			}
			rowData.viewStatus = self.VIEW_STATUS;
			self.reserveCallbackData = rowData;
			if (et.history != null && et.history != undefined) {
				et.history.sendData(rowData);
			}			
			if (et.callInfo != null && et.callInfo != undefined) {
				et.callInfo.sendData(rowData);
			}
			$("#"+self.name+"_divCallBack").hide();
		}
	};
	
	/**
	 * 공지사항 팝업창에서 table 클릭
	 * 
	 * @param {jQuery} $target 클릭한 대상
	 * @param {} row 행 인덱스
	 * @param {} col 열 인덱스
	 */
	ctrl.noticeTBListRowSelect = function($target, row, col) {
		var self = et.home;
		var rowData = et.getRowData("#"+self.name+"_noti_tbList", $target.closest("tr"));
		if (!!rowData){
			new ETService().setSuccessFunction(function (result){
				var self = et.home;
				self.openNoticeModal();
				if (result.message === ETCONST.SUCCESS) {
					var popupId = "pop"+Date.now();
					// 팝업 띄우기
					var tempPop = window.open(
										getContextPath()+"/pop.noticePopup.sp", 
										popupId,
										"location=no, toolbars=no, status=no,width=350px,height=450px");
					
					self.popupObj[popupId] = tempPop;
					self.noticeData = rowData;
				} else {
					
				}
			}).callService(self.path + "/setNoticeRead", rowData);
			
		}
	};
	
	/**
	 * 긴급접수 팝업창에서 table 클릭
	 * 
	 * @param {jQuery} $target 클릭한 대상
	 * @param {} row 행 인덱스
	 * @param {} col 열 인덱스
	 */
	ctrl.emergencyTBListRowSelect = function($target, row, col) {
		var self = et.home;
		var rowData = et.getRowData("#"+self.name+"_emergency_tbList", $target.closest("tr"));
		if (!!rowData){
			self.emergencyData = rowData;
			self.openEmergencyPopup();
		}
	};
	
	// ============================== 이벤트 리스너 ==============================
	/**
	 * 상당 예약콜, 콜백, 공지사항 버튼을 클릭했을때 발생 하는 이벤트
	 */
	ctrl.shortCutClickHandler = function () {
		var self = et.home;
		var type = (this.id).replace(self.name+"_img","");
		if (!$("#"+self.name+"_div"+type).is(':visible')) { 
			$("#"+self.name+"_div"+type).show();
			if (type === "CallBack") {
				self.openCallBackModal();
			} else if (type === "Notice") {
				self.openNoticeModal();
			} else if (type === "Talk") {
				self.openTalkPopup();
			} else if (type === "Emergency") {
				self.openEmergencyModal();
			}
			
			if (this.id === self.name+"_healthCenter") {
				self.openHealthCenter();
			}
			
			if (this.id === self.name+"_emergency") {
				self.emergencyData = null;
				self.openEmergencyPopup();
			}
			
			if (this.id === self.name+"_satisfaction") {
				if ($("#callInfo_iptHand_tel").val() !== "") {
					var attachedData = $("#"+self.name+"_spTmrId").text()+"|"+$("#"+self.name+"_spTmrNm").text()+"|"+$("#callInfo_iptHand_tel").val();
					$("#"+self.name+"_attachedData").val(attachedData);
					$("#"+self.name+"_frm").submit();
				} else {
					et.alert.show(ETCONST.ALERT_TYPE_INFO,"","인입된 고객 전화번호가 없습니다.");
				}
			}
		}
	}
	
	/**
	 * 팝업창과의 메시지 전송
	 */
	ctrl.receiveMessage = function (event) {
		var self = et.home;
		if (event.origin !== window.location.protocol+"//"+window.location.host) {
			return;
		}
		
		if (!!event.data.type && event.data.type === "notice") {
			event.source.postMessage(self.noticeData, event.origin);
		}
		if (!!event.data.type && event.data.type === "talkPop") {
			self.receiveMessageSendTalkPopup(event);
		}
		if (!!event.data.type && event.data.type === "historyPop") {
			if (et.historyDetail != null && et.historyDetail != undefined) {
				et.historyDetail.receiveMessage(event);
			}
		}
		if (!!event.data.type && event.data.type === "healthCenterPop") {
			self.receiveMessageHealthCenterPopup(event);
		}
		if (!!event.data.type && event.data.type === "emergencyPop") {
			self.receiveMessageEmergencyPopup(event);
		}
		if (!!event.data.type && event.data.type === "historyTablePop" || !!event.data.type && event.data.type === "saveInfoPop") {
			if (et.history != null && et.history != undefined) {
				et.history.receiveMessage(event);
			}
		}
	}
	
	/**
	 * kakao talk 전송 팝업창과의 메시지 전송
	 */
	ctrl.receiveMessageSendTalkPopup = function(e) {
		var self = et.home;
		
		if (e.origin !== window.location.protocol+"//"+window.location.host) {
			return;
		}
		
		var toNum = $("#callInfo_iptHand_tel").val();
		if (toNum === "") {
			toNum = $("#historyDetail_iptHand_tel").val();
			if (toNum === "") {
				toNum = "";
			}
		}
		
		if (e.data.action === "init") {
			var data = {};
			data.contextPath = getContextPath();
			data.to = toNum;
			data.tmr_id = $("#home_spTmrId").text();
			data.scriptList = self.data.script_list;
			e.source.postMessage(data, e.origin);
		}
	}
	
	/**
	 * 전국 보건소 팝업창과의 메시지 전송
	 */
	ctrl.receiveMessageHealthCenterPopup = function(e) {
		var self = et.home;
	}
	
	/**
	 * 긴급접수의 메시지 전송
	 */
	ctrl.receiveMessageEmergencyPopup = function(e) {
		var self = et.home;
		if (e.origin !== window.location.protocol+"//"+window.location.host) {
			return;
		}
		if (e.data.action === "init") {
			var data = {};
			data.contextPath = getContextPath();
			data.detailData = self.emergencyData;
			data.code = ITX_CODE;
			data.cTmrList = self.data.manager_list;
			data.tmr_nm = $("#home_spTmrNm").text();
			e.source.postMessage(data, e.origin);
		} else if (e.data.action === "saveComplete") {
			if (0 < $("#home_imgEmergency").length) {
				self.openEmergencyModal();
			}
		}
	}
	
	return ctrl;
}));