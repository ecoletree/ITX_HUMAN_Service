/*******************************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * @Author : mk jang
 * @CreateDate : 2019. 12. 12.
 * @DESC : 홈 _ 메인
 ******************************************************************************/
(function(et, ctrl) {
	if (_.isObject(et) && et.name === ETCONST.PROJECT_NAME) {
		if (!et.saveInfoPopup || et.vc.name !== "history") {
			et.saveInfoPopup = ctrl(et);
		}
	} else {
		console.error("ecoletree OR ETCONST is not valid. please check that common.js file was imported.");
	}
}(this.ecoletree, function(et) {
	
	"use strict";
	
	var ctrl = {};
	
	ctrl.name = "history";
	ctrl.path = "/history";
	ctrl.tables = null; // 데이터 테이블
	ctrl.team_cd = null; // 팀코드 정보
	
	ctrl.tableSearching = false;
	
	ctrl.detailData = null;
	ctrl.tmrInfo = null;
	ctrl.api_url = null;
	ctrl.isManager = false;
	
	
	// ============================== 화면 컨트롤 ==============================
	/**
	 * init VIEW
	 */
	ctrl.init = function(teamCd) {
		var self = et.saveInfoPopup;
		
		var data = {};
		data.type = "saveInfoPop";
		data.action = "init";
		window.addEventListener("message", self.receiveMessage, false);
		window.opener.postMessage(data, window.location.protocol+"//"+window.location.host+getContextPath());

		self.team_cd = teamCd;
		var lastMonth = new Date()
		
		$("#"+self.name+"_sdate").datepicker({
			format: "yyyy.mm.dd",
			language: "kr",
			todayHighlight : true
		}).datepicker("setDate", lastMonth);
		$("#"+self.name+"_edate").datepicker({
			format: "yyyy.mm.dd",
			language: "kr",
			todayHighlight : true
		}).datepicker("setDate", new Date());
		
		$("#"+self.name+"_btnSdate").on("click", function(e) {
			$("#"+self.name+"_sdate").datepicker("show",{
				format: "yyyy.mm.dd",
				language: "kr",
				todayHighlight : true,
				setDate : lastMonth
            }).on("changeDate", function (e) {
                $(this).datepicker("hide")
            });
		});
		$("#"+self.name+"_btnEdate").on("click", function(e) {
			$("#"+self.name+"_edate").datepicker("show",{
				format: "yyyy.mm.dd",
				language: "kr",
				todayHighlight : true,
				setDate : new Date()
            }).on("changeDate", function (e) {
                $(this).datepicker("hide")
            });
		});
		
		var option1, option2, num;
		// 상세 영역 통화시간 세팅
		for (var i = 0; i < 24; i++) {
			num = i < 10 ? "0" + i : i;
			option1 = $('<option>', {
				value : num.toString(),
				text : num.toString()
			});
			$("#historyPopup_selHour").append(option1);
		}
		for (var i = 0; i < 60; i++) {
			num = i < 10 ? "0" + i : i;
			option1 = $('<option>', {
				value : num.toString(),
				text : num.toString()
			});
			option2 = $('<option>', {
				value : num.toString(),
				text : num.toString()
			});
			$("#historyPopup_selMin").append(option1);
			$("#historyPopup_selSec").append(option2);
		}
		
		$("#"+self.name+"_sel1").change(self.historySelectBoxChangeHandler);
		$("#"+self.name+"_sel2").change(self.historySelectBoxChangeHandler);
		
		if (0 < $("#"+self.name+"_sel1 option[value='search_tmr_nm']").length) {
			$("#"+self.name+"_sel1").val("search_tmr_nm");
			$("#"+self.name+"_sel1").trigger("change");
		}
		
		$("#"+self.name+"_btnSearch").click(self.btnSearchHandler);
		$("#"+self.name+"_btnDownLoad").click(self.btnDownLoadHandler);
		
		$("#"+self.name+"_sel1_search").on("keydown",function(e){
			if (e.which == 13) {
                e.preventDefault();
                self.btnSearchHandler();
            }
		});
		$("#"+self.name+"_sel2_search").on("keydown",function(e){
			if (e.which == 13) {
				e.preventDefault();
				self.btnSearchHandler();
			}
		});
		
//		$("#"+self.name+"_sel4").change(self.historySelectBoxCDChangeHandler);
//		$("#"+self.name+"_sel5").change(self.historySelectBoxCDChangeHandler);
//		$("#"+self.name+"_sel6").change(self.historySelectBoxCDChangeHandler);
//		$("#"+self.name+"_sel7").change(self.historySelectBoxCDChangeHandler);
		
		
		et.setDataTableRowSelection("#"+self.name+"_tbList", self.tbListRowSelect);
		
		// 상세 영역 이벤트 등록
		$("#historyPopup_btnSave").on("click", self.btnSave_clickEventListener);
		
		$("#historyPopup_selCrCd1").on("change", self.selCrCd_changeEventListener);
		$("#historyPopup_selCrCd2").on("change", self.selCrCd_changeEventListener);
		$("#historyPopup_selCrCd3").on("change", self.selCrCd_changeEventListener);
	};
	
	ctrl.receiveMessage = function (e)
	{	
		var self = et.saveInfoPopup;
		if (e.origin !== window.location.protocol+"//"+window.location.host) {
			return;
		} 
		
		if (e.data.type !== "saveInfoPop") {
			return;
		}
		
		ITX_CODE = e.data.code;
		ITX_ALL_CODE = e.data.codeAll;
		self.tmrInfo = e.data.tmrInfo;
		self.api_url = e.data.api_url;
		self.team_cd = e.data.teamCd;
		self.isManager = e.data.is_manager;
		ITX_TEAM = e.data.teamList;
		$("#"+self.name+"_sel1_search").val(self.tmrInfo.tmr_nm);
		self.setCodeData();
	}
	
	/**
	 * home에서 코드리스트를 가져왔을때 호출
	 * */
	ctrl.setCodeData = function () {
		var self = et.saveInfoPopup;
		var codeList = et.getCodeList ("011");
		et.makeSelectOption(codeList, {value:"item1",text:"item_nm"},"#"+self.name+"_sel3","선택해주세요");
		
		var remarkList = et.getCodeList ("090");
		et.makeSelectOption(remarkList, {value:"item1",text:"item_nm"},"#"+self.name+"_sel1_remark","선택해주세요");
		et.makeSelectOption(remarkList, {value:"item1",text:"item_nm"},"#"+self.name+"_sel2_remark","선택해주세요");
		
		var teamList = ITX_TEAM;
		et.makeSelectOption(teamList, {value:"team_cd",text:"team_name"},"#"+self.name+"_sel1_team","선택해주세요");
		et.makeSelectOption(teamList, {value:"team_cd",text:"team_name"},"#"+self.name+"_sel2_team","선택해주세요");
		
		var cdCodeList = et.getCodeList ("010");
		et.makeSelectOption(cdCodeList, {value:"item1",text:"item_nm"},"#"+self.name+"_sel4","선택해주세요");
		
		if (!self.isManager) {
			$("#"+self.name+"_sel1_team").val(self.team_cd);
			$("#"+self.name+"_sel2_team").val(self.team_cd);
			$("#"+self.name+"_sel1_team").attr('disabled', 'disabled'); 
			$("#"+self.name+"_sel2_team").attr('disabled', 'disabled'); 
		} else {
			$("#"+self.name+"_sel1_team").removeAttr('disabled');
			$("#"+self.name+"_sel2_team").removeAttr('disabled');
		}
		
		// 상담유형(대)
		codeList = et.getCodeList("010");
		et.makeSelectOption(codeList, {value:"item1",text:"item_nm"},"#historyPopup_selCrCd1","선택해주세요");
		
		// 통화결과
		codeList = et.getCodeList("011");
		et.makeSelectOption(codeList, {value:"item1",text:"item_nm"},"#historyPopup_selExec_state","선택해주세요");
	}
	
	// ============================== 동작 컨트롤 ==============================
	/**
	 * 검색 조건 초기화
	 */
	
	ctrl.searchFormInit = function () {
		var self = et.saveInfoPopup;
		
		var lastMonth = new Date();
		$("#"+self.name+"_sel1").val("search_tmr_nm");
		if (!!self.tmrInfo) {
			$("#"+self.name+"_sel1_search").val(self.tmrInfo.tmr_nm);
		}
		$("#"+self.name+"_sel2").val("");
		$("#"+self.name+"_sel2_search").val("");
		
		$("#"+self.name+"_sel3").val("");
		
		if (self.isManager) {
			$("#"+self.name+"_sel1_team").val("");
			$("#"+self.name+"_sel2_team").val("");
		}
		
		$("#"+self.name+"_sel1_remark").val("");
		$("#"+self.name+"_sel2_remark").val("");
		$("#"+self.name+"_sdate").datepicker("setDate",lastMonth);
		$("#"+self.name+"_edate").datepicker("setDate",new Date());
		
//		$("#"+self.name+"_sel4").val("");
//		$("#"+self.name+"_sel5").val("");
//		$("#"+self.name+"_sel6").val("");
//		$("#"+self.name+"_sel7").val("");
//		$("#"+self.name+"_sel4").trigger("change");
		
	}
	
	/**
	 *  조회조건 1, 조회조건2 change Handler
	 */
	ctrl.historySelectBoxChangeHandler = function (e) {
		var self = et.saveInfoPopup;
		if (this.id === self.name+"_sel1") {
			if ($(this).val() === "search_tmr_nm") {
				if (!!self.tmrInfo) {
					$("#"+this.id+"_search").val(self.tmrInfo.tmr_nm);
				}
			} else {
				$("#"+this.id+"_search").val("");
			}
		} else {
			$("#"+this.id+"_search").val("");
		}
		
		
		if ($(this).val() === "") {
			$("#"+this.id+"_search").prop("readonly","readonly");
			$("#"+this.id+"_search").show();
			$("#"+this.id+"_team").hide();
			$("#"+this.id+"_remark").hide();
		} else {
			if ($(this).val() === "team_cd") {
				$("#"+this.id+"_search").hide();
				$("#"+this.id+"_team").show();
				$("#"+this.id+"_remark").hide();
			} else if ($(this).val() === "remark_1") {
				$("#"+this.id+"_search").hide();
				$("#"+this.id+"_team").hide();
				$("#"+this.id+"_remark").show();
			} else {
				$("#"+this.id+"_search").show();
				$("#"+this.id+"_team").hide();
				$("#"+this.id+"_remark").hide();
				$("#"+this.id+"_search").prop("readonly","");
			}
		}
	}
	
	/**
	 *  대중소,상세 change Handler
	 */
	ctrl.historySelectBoxCDChangeHandler = function (e) {
		var self = et.saveInfoPopup;
		var selID = $(this).prop("id");
		var value = $(this).val();
		var codeList = [];
		if (selID === self.name+"_sel4") {
			if (value === "") {
				$("#"+self.name+"_sel5").attr('disabled', 'disabled'); 
				et.makeSelectOption(codeList, {value:"item2",text:"item_nm"},"#"+self.name+"_sel5","선택해주세요");
				et.makeSelectOption(codeList, {value:"item3",text:"item_nm"},"#"+self.name+"_sel6","선택해주세요");
				et.makeSelectOption(codeList, {value:"item4",text:"item_nm"},"#"+self.name+"_sel7","선택해주세요");
			} else {
				codeList = et.getCodeList("010",value);
				$("#"+self.name+"_sel5").removeAttr('disabled');
				et.makeSelectOption(codeList, {value:"item2",text:"item_nm"},"#"+self.name+"_sel5","선택해주세요");
				et.makeSelectOption([], {value:"item3",text:"item_nm"},"#"+self.name+"_sel6","선택해주세요");
				et.makeSelectOption([], {value:"item4",text:"item_nm"},"#"+self.name+"_sel7","선택해주세요");
			}
			$("#"+self.name+"_sel6").attr('disabled', 'disabled'); 
			$("#"+self.name+"_sel7").attr('disabled', 'disabled'); 
			
		} else if (selID === self.name+"_sel5") {
			if (value === "") {
				$("#"+self.name+"_sel6").attr('disabled', 'disabled'); 
				et.makeSelectOption(codeList, {value:"item3",text:"item_nm"},"#"+self.name+"_sel6","선택해주세요");
				et.makeSelectOption(codeList, {value:"item4",text:"item_nm"},"#"+self.name+"_sel7","선택해주세요");
			} else {
				codeList = et.getCodeList("010",$("#"+self.name+"_sel4").val(),value);
				$("#"+self.name+"_sel6").removeAttr('disabled');
				et.makeSelectOption(codeList, {value:"item3",text:"item_nm"},"#"+self.name+"_sel6","선택해주세요");
				et.makeSelectOption([], {value:"item4",text:"item_nm"},"#"+self.name+"_sel7","선택해주세요");
			}
			$("#"+self.name+"_sel7").attr('disabled', 'disabled'); 
		} else if (selID === self.name+"_sel6") {
			if (value === "") {
				$("#"+self.name+"_sel7").attr('disabled', 'disabled'); 
				et.makeSelectOption(codeList, {value:"item4",text:"item_nm"},"#"+self.name+"_sel7","선택해주세요");
			} else {
				codeList = et.getCodeList("010",$("#"+self.name+"_sel4").val(),$("#"+self.name+"_sel5").val(),value);
				$("#"+self.name+"_sel7").removeAttr('disabled');
				et.makeSelectOption(codeList, {value:"item4",text:"item_nm"},"#"+self.name+"_sel7","선택해주세요");
			}
		} else if (selID === self.name+"_sel7") {
			
		}
	}
	
	// ============================== 동작 컨트롤 : 외부 등록 ==============================
	
	/**
	 * dataTable을 생성
	 */
	ctrl.createDatatable = _.debounce(function(type) {
		var self = et.saveInfoPopup;
		var param = {};
		self.tableSearching = true;
		self.setDetailView(null);
		
		$("#"+self.name+"_btnSearch").prop("disabled", true);
		$("#"+self.name+"_btnDownLoad").prop("disabled", true);
		var search_1 = $("#"+self.name+"_sel1").val();
		var search_2 = $("#"+self.name+"_sel2").val();
		
		param.sdate = $("#"+self.name+"_sdate").val();
		param.edate = $("#"+self.name+"_edate").val();
		
		if (!self.isManager) {
			var ar1 = param.sdate.split('.');
		    var ar2 = param.edate.split('.');
		    var da1 = new Date(ar1[0], ar1[1], ar1[2]);
		    var da2 = new Date(ar2[0], ar2[1], ar2[2]);
		    var dif = da2 - da1;
		    var cDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
		    var cMonth = cDay * 30;// 월 만듬
		    var cYear = cMonth * 12; // 년 만듬
		    
		    if (param.sdate === "" || param.edate === "") {
				et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "검색조건은 최대 3개월까지 입니다.");
				self.tableSearching = false;
				$("#"+self.name+"_btnSearch").prop("disabled", false);
				$("#"+self.name+"_btnDownLoad").prop("disabled", false);
				return;
			} else {
				if (parseInt(dif/cMonth) > 3) {
					et.alert.show(ETCONST.ALERT_TYPE_INFO, "", "검색조건은 최대 3개월까지 입니다.");
					$("#"+self.name+"_btnSearch").prop("disabled", false);
					$("#"+self.name+"_btnDownLoad").prop("disabled", false);
					self.tableSearching = false;
					return;
				}
			}
		}
		
		if (search_1 !== "") {
			if (search_1 === "team_cd") {
				param[search_1] = $("#"+self.name+"_sel1_team").val() === "" ? undefined : $("#"+self.name+"_sel1_team").val();
			} else if (search_1 === "remark_1"){
				param[search_1] = $("#"+self.name+"_sel1_remark").val() === "" ? undefined : $("#"+self.name+"_sel1_remark").val();
			} else {
				param[search_1] = $("#"+self.name+"_sel1_search").val() === "" ? undefined : $("#"+self.name+"_sel1_search").val();
			}
		}
		
		if (search_2 !== "") {
			if (search_2 === "team_cd") {
				param[search_2] = $("#"+self.name+"_sel2_team").val() === "" ? undefined : $("#"+self.name+"_sel2_team").val();
			} else if (search_2 === "remark_1"){
				param[search_2] = $("#"+self.name+"_sel2_remark").val() === "" ? undefined : $("#"+self.name+"_sel2_remark").val();
			} else {
				param[search_2] = $("#"+self.name+"_sel2_search").val() === "" ? undefined : $("#"+self.name+"_sel2_search").val();
			}
		}
			
		var columns = [ {
				className : "txtCenter",
				data : "rownum",
				render : function(data, type, full, meta) {
					var str = ""+(parseInt(data)+parseInt(meta.settings._iDisplayStart));
					return str;
					
				}
			}, {
				className : "txtCenter",
				data : "call_bdtm"
			}, {
				data : "call_id",
			}, {
				data : "tmr_id",
			}, {
				data : "tmr_nm",
			}, {
				data : "cust_id",
			}, {
				data : "view_cust_nm",
			}, {
				data : "view_hand_tel",
			}, {
				className : "txtCenter",
				data : "item_id",
				render : function(data, type, full, meta) {
					var str = '<img src="'+getContextPath()+'/resources/ecoletree/img/icon_speaker.png" />';
					return '<a class="btnBlue btnSmall">'+str+'</a>';
					
				}
			}
		];
		var option = et.createDataTableSettings(columns, self.path + "/getCallNotSaveHistoryList", param, self.dataTableDrawCallback);
		option.autoWidth = false;
		self.tables = $("#"+self.name+"_tbList").DataTable(option);
	},500);
	
	// ============================== 이벤트 리스너 ==============================
	
	/**
	 * 엑셀 익스포트
	 */
	ctrl.excelExport = function (headers, dataList) {
	    // step 1. workbook 생성
	    var wb = XLSX.utils.book_new();

	    // step 2. 시트 만들기 
	    var sheet = [];
	    var row = [];
	    $.each(headers, function (i,item) {
	    	row.push(item.field_nm);
	    });
	    // 헤더 셋팅
	    sheet.push(row);
	    
	    $.each(dataList, function (i,data) {
	    	row = [];
	    	
	    	$.each(headers, function (y,header) {
		    	row.push(data[header.field_id]);
		    });
	    	sheet.push(row);
	    });
	    
	    var newWorksheet = XLSX.utils.aoa_to_sheet(sheet);
	    
	    // step 3. workbook에 새로만든 워크시트에 이름을 주고 붙인다.  
	    XLSX.utils.book_append_sheet(wb, newWorksheet, "Sheet");

	    // step 4. 엑셀 파일 만들기 
	    XLSX.writeFile(wb, "상담이력미등록_"+new Date().getTime()+".xlsx", {compression:true});
	    
	    $(".bxProcess").hide();
	}
	
	
	/**
	 *  조회 버튼 클릭
	 */
	ctrl.btnSearchHandler = function() {
		var self = et.saveInfoPopup;
		self.createDatatable("click");
	}
	
	/**
	 *  조회 버튼 클릭
	 */
	ctrl.btnDownLoadHandler = function() {
		var self = et.saveInfoPopup;
		
		$(".bxProcess").show();
		var headers = [ {
				field_id : "rownum",
				field_nm : "순번"
			}, {
				field_id : "call_bdtm",
				field_nm : "통화일자"
			}, {
				field_id : "tmr_id",
				field_nm : "상담원ID"
			}, {
				field_id : "tmr_nm",
				field_nm : "상담원명"
			}, {
				field_id : "view_cust_nm",
				field_nm : "고객명"
			}, {
				field_id : "cust_id",
				field_nm : "고객아이디"
			}, {
				field_id : "view_hand_tel",
				field_nm : "전화번호"
			}, {
				field_id : "callform_cd",
				field_nm : "인아웃구분"
			}
		];
		
		//self.excelExport(headers, self.tables.data());
		var params = $(self.tables.settings())[0].ajax.data;
		params.tmr_id = self.tmrInfo.tmr_id;
		params.log_tmr_nm = self.tmrInfo.tmr_nm;
		
		var url = "/ITX_KCDC_Admin"+"/history/notSaveDownload";
		$.ajax({
		    type: "post",
		    url: url,
		    cache: false,
		    data: params,
		    success: function (result) {
		    	var data = result.data;
		    	if (!!data) {
		    		self.excelExport(headers, result.data);
		    	} else {
		    		$(".bxProcess").hide();
		    		et.alert.show(ETCONST.ALERT_TYPE_INFO,"","엑셀 다운로드할 데이터가 없습니다.");
		    	}
		    },
		    error: function(xhr, ajaxOptions, thrownError) {
		    	$(".bxProcess").hide();
		    	et.alert.show(ETCONST.ALERT_TYPE_INFO,"","엑셀 다운로드가 실패 하였습니다. 관리자에게 문의하세요");
		    }
		});
	}
	
	/**
	 * 데이터 테이블 출력 완료 후, 비활성화한 화면 컨트롤 활성화.
	 * 
	 * @param {} settings
	 */
	ctrl.dataTableDrawCallback = function(settings) {
		var self = et.saveInfoPopup;
		
		//settings.ajax.data.totalCount = settings.json.recordsFiltered;
		self.tableSearching = false;
		$("#"+self.name+"_btnSearch").prop("disabled", false);
		if (0 < this.fnGetData().length) {
			$("#"+self.name+"_btnDownLoad").prop("disabled", false);
			$("#"+self.name+"_tbList tbody td:eq(0)").trigger("click");
		}
	};
	
	/**
	 * 테이블 row 선택시, 상세화면으로 이동
	 * 
	 * @param {jQuery} $target 클릭한 대상
	 * @param {} row 행 인덱스
	 * @param {} col 열 인덱스
	 */
	ctrl.tbListRowSelect = function($target, row, col) {
		var self = et.saveInfoPopup;
		var rowData = et.getRowData("#"+self.name+"_tbList", $target.closest("tr"));
		if (!$target.is("img")) {
			console.log(rowData);
			self.setDetailView(rowData);
		} else {
			var url = self.api_url+"/admin/?";
			var giid = rowData.global_id;
			var stepid = rowData.item_id
			stepid = stepid.replace(/-/g, '');
			var param = "giid="+giid+"&stepid="+stepid+"#QM_CDR_RESULTS:QM_REVIEW_VOICE";
			window.open(url+param,'ITX REVIEW VIOCE','height=' + screen.height + ',width=' + screen.width + 'fullscreen=yes');
		}
		
	};
	
	ctrl.setDetailView = function (data) {
		var self = et.saveInfoPopup;
		
		self.detailData = data;

		if (!!data) {
			$("#historyPopup_iptTmr_nm").val(data.tmr_nm);
			$("#historyPopup_iptTeam_name").val(data.team_name);
			$("#historyPopup_iptCall_id").val(data.call_id);
			$("#historyPopup_iptCall_bdt").val(data.call_bdtm);
			$("#historyPopup_iptCall_btm").val(data.call_bdttm);
			$("#historyPopup_iptCall_stm").val(data.call_sdttm);
			$("#historyPopup_iptSave_tm").val(data.save_tm);
			
			$("#historyPopup_iptCust_num").val(data.cust_id);
			$("#historyPopup_iptCust_nm").val(data.cust_nm);
			$("#historyPopup_iptHand_tel").val(data.view_hand_tel);
			$("#historyPopup_iptClass_1").val(data.class_1);
			$("#historyPopup_iptClass_2").val(data.class_2);
			$("#historyPopup_iptClass_3").val(data.class_3);
			
		} else {
			$("#historyPopup_iptTmr_nm").val("");
			$("#historyPopup_iptTeam_name").val("");
			$("#historyPopup_iptCall_id").val("");
			$("#historyPopup_iptCall_bdt").val("");
			$("#historyPopup_iptCall_btm").val("");
			$("#historyPopup_iptCall_stm").val("");
			$("#historyPopup_iptSave_tm").val("");
			
			$("#historyPopup_iptCust_num").val("");
			$("#historyPopup_iptCust_nm").val("");
			$("#historyPopup_iptHand_tel").val("");
			$("#historyPopup_iptClass_1").val("");
			$("#historyPopup_iptClass_2").val("");
			$("#historyPopup_iptClass_3").val("");
		}
		
		$("#historyPopup_selHour").find("option:eq(0)").prop("selected", true);
		$("#historyPopup_selMin").find("option:eq(0)").prop("selected", true);
		$("#historyPopup_selSec").find("option:eq(0)").prop("selected", true);
		$("#historyPopup_selCrCd1").find("option:eq(0)").prop("selected", true).trigger("change");
		$("#historyPopup_selExec_state").find("option:eq(0)").prop("selected", true);
		$("#historyPopup_taCr_mm").val("");
		$("#historyPopup_taCr_mm_add").val("");
	};
	
	/**
	 * append select option
	 */
	ctrl.appendOption = function (sId, sValue, sLabel) {
		$(sId).empty();
		
		if (!!sValue) {
			$(sId).append("<option value='"+sValue+"'>"+ sLabel +"</option>");
		} else {
			$(sId).append("<option value=''>선택해주세요</option>");
		}
	};
	
	/**
	 * 상담유형 변경시
	 */
	ctrl.selCrCd_changeEventListener = function (e) {
		var self = et.saveInfoPopup;
		var id = e.currentTarget.id;
		var num = id.replace("historyPopup_selCrCd","");
		var selCd1, selCd2, selCd3, codeList;
		
		selCd1 = $("#historyPopup_selCrCd1").val();
		selCd2 = $("#historyPopup_selCrCd2").val();
		selCd3 = $("#historyPopup_selCrCd3").val();
		
		if (num === "1") {
			codeList = et.getCodeAllList("010", selCd1);
			et.makeSelectOption(null, {value:"item3",text:"item_nm"},"#historyPopup_selCrCd3","선택해주세요");
			$("#historyPopup_selCrCd3").find("option:eq(0)").prop("selected", true);
			et.makeSelectOption(null, {value:"item4",text:"item_nm"},"#historyPopup_selCrCd4","선택해주세요");
			$("#historyPopup_selCrCd4").find("option:eq(0)").prop("selected", true);
			if (!!codeList) {
				et.makeSelectOption(codeList, {value:"item2",text:"item_nm"},"#historyPopup_selCrCd2","선택해주세요");
				$("#historyPopup_selCrCd2").find("option:eq(0)").prop("selected", true);
				$("#historyPopup_selCrCd2").trigger("change");
			} else {
				et.makeSelectOption(null, {value:"item2",text:"item_nm"},"#historyPopup_selCrCd2","선택해주세요");
				$("#historyPopup_selCrCd2").find("option:eq(0)").prop("selected", true);
			}
		} else if (num === "2") {
			codeList = et.getCodeAllList("010", selCd1, selCd2);
			et.makeSelectOption(null, {value:"item4",text:"item_nm"},"#historyPopup_selCrCd4","선택해주세요");
			$("#historyPopup_selCrCd4").find("option:eq(0)").prop("selected", true);
			if (!!codeList) {
				et.makeSelectOption(codeList, {value:"item3",text:"item_nm"},"#historyPopup_selCrCd3","선택해주세요");
				$("#historyPopup_selCrCd3").find("option:eq(0)").prop("selected", true);
				$("#historyPopup_selCrCd3").trigger("change");
			} else {
				et.makeSelectOption(null, {value:"item3",text:"item_nm"},"#historyPopup_selCrCd3","선택해주세요");
				$("#historyPopup_selCrCd3").find("option:eq(0)").prop("selected", true);
			}
		} else if (num === "3") {
			codeList = et.getCodeAllList("010", selCd1, selCd2, selCd3);
			if (!!codeList) {
				et.makeSelectOption(codeList, {value:"item4",text:"item_nm"},"#historyPopup_selCrCd4","선택해주세요");
			} else {
				et.makeSelectOption(null, {value:"item4",text:"item_nm"},"#historyPopup_selCrCd4","선택해주세요");
			}
			$("#historyPopup_selCrCd4").find("option:eq(0)").prop("selected", true);
		}
	};
	
	/**
	 * 저장버튼 클릭시
	 */
	ctrl.btnSave_clickEventListener = function (e) {
		var self = et.saveInfoPopup;
		var totalSec, date;

		if (!self.detailData) return;
		
		totalSec = (Number($("#historyPopup_selHour").val())*60*60) + (Number($("#historyPopup_selMin").val())*60) + Number($("#historyPopup_selSec").val());
		date = new Date($("#historyPopup_iptCall_btm").val());
		date.setSeconds(date.getSeconds() + totalSec);
		
		self.detailData.state_cd = $("#historyPopup_selExec_state").val();
		self.detailData.cr_cd1 = $("#historyPopup_selCrCd1").val();
		self.detailData.cr_cd2 = $("#historyPopup_selCrCd2").val();
		self.detailData.cr_cd3 = $("#historyPopup_selCrCd3").val();
		self.detailData.cr_cd4 = $("#historyPopup_selCrCd4").val();
		self.detailData.cr_mm = $("#historyPopup_taCr_mm").val().replace(/\n/g, '<br/>');
		self.detailData.cr_mm_add = $("#historyPopup_taCr_mm_add").val().replace(/\n/g, '<br/>');
		self.detailData.call_etm = $.format.date(date, 'yyyy-MM-dd HH:mm:ss');
		self.detailData.save_tm = "0";
		self.detailData.saveType = "pop";
		self.detailData.viewStatus = "saveInfo";
		
		var optionData = $("#historyPopup_selCrCd4").find("option:selected").data();
		self.detailData.o_state_cd = optionData.value1;
		self.detailData.o_state_scd = optionData.value2;
		
		new ETService().setSuccessFunction(self.saveCallSucceed).callService("/callInfo/saveCallInfo", self.detailData);
	};
	/**
	 * 저장 서비스 호출 결과를 핸들링.
	 */
	ctrl.saveCallSucceed = function(returnData) {
		var self = et.saveInfoPopup;
		var msg = returnData.resultMsg;
		var data = returnData.data;
		
		if (msg === ETCONST.SUCCESS) {
			self.createDatatable("click");
			ecoletree.alert.show(ETCONST.ALERT_TYPE_INFO, "", "미저장 등록을 완료했습니다.");
		}
	};
	
	return ctrl;
}));