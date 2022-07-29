/*******************************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * @Author : mk jang
 * @CreateDate : 2019. 12. 12.
 * @DESC : 홈 _ 메인
 ******************************************************************************/
(function(et, ctrl) {
	if (_.isObject(et) && et.name === ETCONST.PROJECT_NAME) {
		if (!et.history || et.vc.name !== "history") {
			et.history = ctrl(et);
		}
	} else {
		console.error("ecoletree OR ETCONST is not valid. please check that common.js file was imported.");
	}
}(this.ecoletree, function(et) {
	
	"use strict";
	
	var ctrl = {};
	
	ctrl.name = "history";
	ctrl.path = "/history";
	ctrl.data = null; // 넘겨받은 데이터 저장용
	ctrl.tables = null; // 데이터 테이블
	ctrl.team_cd = null; // 팀코드 정보
	
	ctrl.tableSearching = false;
	
	// ============================== 화면 컨트롤 ==============================
	/**
	 * init VIEW
	 */
	ctrl.init = function(initData, teamCd) {
		var self = et.history;
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
		
		$("#"+self.name+"_sel1").change(self.historySelectBoxChangeHandler);
		$("#"+self.name+"_sel2").change(self.historySelectBoxChangeHandler);
		
		if (0 < $("#"+self.name+"_sel1 option[value='search_tmr_nm']").length) {
			$("#"+self.name+"_sel1").val("search_tmr_nm");
			$("#"+self.name+"_sel1").trigger("change");
		}
		
		$("#"+self.name+"_btnSearch").click(self.btnSearchHandler);
		$("#"+self.name+"_btnDownLoad").click(self.btnDownLoadHandler);
		$("#"+self.name+"_btnHistoryTable").click(self.btnHistoryTableHandler);
		$("#"+self.name+"_btnSaveInfo").click(self.btnSaveInfoHandler);
		
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
		
	};
	
	/**
	 * home 에서 customer 콜이 왔을때 데이터를 넘겨 받음
	 */
	ctrl.sendData = function (data) {
		var self = et.history;
		self.searchFormInit();
		self.data = data;
		if (self.data) {
			self.createDatatable();
		}
	}
	
	/**
	 * BP에서 state 값이 변경 되었을때 호출
	 */
	ctrl.setStatus = function (state) {
		var self = et.history;
		if (state === BP_STATE.BUSY) {
			$("#"+self.name+"_sel1").val("search_tmr_nm");
			$("#"+self.name+"_sel2").val("");
		} else if (state === BP_STATE.READY) {
			$("#"+self.name+"_btnDownLoad").prop("disabled", true);
			if (self.tables != null && !self.tableSearching) {
				self.tables.clear();
				self.tables.destroy();
				self.tables = null;
				if (et.historyDetail != null && et.historyDetail != undefined) {
					et.historyDetail.sendData(null);
				}
			}
			self.data = null;
		} else if (state === BP_STATE.AFTER_CALL_WORK){
		} else {
			self.data = null;
		}
	}
	
	/**
	 * home에서 코드리스트를 가져왔을때 호출
	 * */
	ctrl.setCodeData = function () {
		var self = et.history;
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
		
		if (0 === $("#home_imgEmergency").length) {
			$("#"+self.name+"_sel1_team").val(self.team_cd);
			$("#"+self.name+"_sel2_team").val(self.team_cd);
			$("#"+self.name+"_sel1_team").attr('disabled', 'disabled'); 
			$("#"+self.name+"_sel2_team").attr('disabled', 'disabled'); 
		} else {
			$("#"+self.name+"_sel1_team").removeAttr('disabled');
			$("#"+self.name+"_sel2_team").removeAttr('disabled');
		}
		
		
	}
	
	// ============================== 동작 컨트롤 ==============================
	/**
	 * 검색 조건 초기화
	 */
	
	ctrl.searchFormInit = function () {
		var self = et.history;
		
		var lastMonth = new Date();
		$("#"+self.name+"_sel1").val("search_tmr_nm");
		$("#"+self.name+"_sel1_search").val($("#home_spTmrNm").text());
		$("#"+self.name+"_sel2").val("");
		$("#"+self.name+"_sel2_search").val("");
		
		$("#"+self.name+"_sel3").val("");
		
		if (0 !== $("#home_imgEmergency").length) {
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
		var self = et.history;
		if (this.id === self.name+"_sel1") {
			if ($(this).val() === "search_tmr_nm") {
					$("#"+this.id+"_search").val($("#home_spTmrNm").text());
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
		var self = et.history;
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
		var self = et.history;
		var param = {};
		self.tableSearching = true;
		$("#"+self.name+"_btnSearch").prop("disabled", true);
		$("#"+self.name+"_btnDownLoad").prop("disabled", true);
		param.totalCount = 0;
		var search_1 = $("#"+self.name+"_sel1").val();
		var search_2 = $("#"+self.name+"_sel2").val();
		
		if (type === undefined) {
			param = !self.data ? {} : _.cloneDeep(self.data);
			
			if (param === {}) {
				if (et.home.custInfo === null) {
					param.search_tmr_nm = $("#home_spTmrNm").text();
				} else {
					if (et.home.custInfo.cust_id === null || et.home.custInfo.cust_id === undefined) {
						param.search_tmr_nm = $("#home_spTmrNm").text();
					} else {
						param.cust_id = et.home.custInfo.cust_id;
					}
				}
			}
		} else {
			param.sdate = $("#"+self.name+"_sdate").val();
			param.edate = $("#"+self.name+"_edate").val();
			
			if (et.home.data !== undefined && et.home.data !== null ) {
				if (et.home.data.manager_id === undefined || et.home.data.manager_id === null ) {
					var ar1 = param.sdate.split('.');
				    var ar2 = param.edate.split('.');
				    var da1 = new Date(ar1[0], ar1[1], ar1[2]);
				    var da2 = new Date(ar2[0], ar2[1], ar2[2]);
				    var dif = da2 - da1;
				    var cDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
				    var cMonth = cDay * 30;// 월 만듬
				    var cYear = cMonth * 12; // 년 만듬
				    
					if (param.sdate === "" || param.edate === "") {
						et.home.showMessageAlert("검색조건은 최대 3개월까지 입니다.");
						self.tableSearching = false;
						$("#"+self.name+"_btnSearch").prop("disabled", false);
						$("#"+self.name+"_btnDownLoad").prop("disabled", false);
						return;
					} else {
						if (parseInt(dif/cMonth) > 3) {
							et.home.showMessageAlert("검색조건은 최대 3개월까지 입니다.");
							$("#"+self.name+"_btnSearch").prop("disabled", false);
							$("#"+self.name+"_btnDownLoad").prop("disabled", false);
							self.tableSearching = false;
							return;
						}
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
			
//			if (0 === $("#home_imgEmergency").length) {
//				param.search_tmr_id = $("#home_spTmrId").text();
//			}
		}
		
		
		
//		if ($("#"+self.name+"_sel3").val() !== "") {
//			param.state_cd = $("#"+self.name+"_sel3").val();
//		}
//		
//		if ($("#"+self.name+"_sel4").val() !== "") {
//			param.cr_cd1 = $("#"+self.name+"_sel4").val();
//		}
//		if ($("#"+self.name+"_sel5").val() !== "") {
//			param.cr_cd2 = $("#"+self.name+"_sel5").val();
//		}
//		if ($("#"+self.name+"_sel6").val() !== "") {
//			param.cr_cd3 = $("#"+self.name+"_sel6").val();
//		}
//		if ($("#"+self.name+"_sel7").val() !== "") {
//			param.cr_cd4 = $("#"+self.name+"_sel7").val();
//		}
		
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
				data : "team_name",
			}, {
				data : "tmr_nm",
			}, {
				data : "view_cust_nm",
			}, {
				data : "view_cr_cd1",
			}, {
				data : "view_cr_cd2",
			}, {
				data : "view_cr_cd3",
			}, 
//			{
//				data : "view_cr_cd4",
//			}, 
			{
				data : "cr_mm",
				render : function(data, type, full, meta) {
					var str = data.replace(/<br\s*\/?>/mg,"\n");
					return str;
					
				}
			}, {
				data : "state_nm", // 통화유형
			}, {
				data : "call_bdttm", // 콜시작시간
			}, {
				data : "call_sdttm",  // 콜저장시간
			}, {
				data : "call_tm",  // 통화시간
			}, {
				data : "view_hand_tel",
			}, {
				data : "cust_id",
			}, {
				data : "callform_cd",
			}, {
				className : "txtCenter",
				data : "item_id",
				render : function(data, type, full, meta) {
					var str = '<img src="'+getContextPath()+'/resources/ecoletree/img/icon_speaker.png" />';
					return '<a class="btnBlue btnSmall">'+str+'</a>';
					
				}
			}
		];
		
		// 테이블에서 툴팁이 보여야 할 목록 작성
		var tooltipColumnList = ["team_name", "view_cr_cd3", "cr_mm", "call_bdttm", "call_sdttm", "cust_id"];
		
		var option = et.createDataTableSettings(columns, self.path + "/getCallHistoryList", param, self.dataTableDrawCallback);
		option.autoWidth = false;
		option.pageLength = 8;
		option.rowCallback = function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) { // 툴팁 설정
			_.forEach(tooltipColumnList, function(item, index) {
				var idx = _.findIndex(columns, function(o) { return o.data == item; });
				$("td:eq("+idx+")", nRow).attr('title', aData[columns[idx].data]);
			});
		};
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
	    XLSX.writeFile(wb, "상담이력_"+new Date().getTime()+".xlsx", {compression:true});
	    
	    $(".bxProcess").hide();
	}
	
	
	/**
	 *  조회 버튼 클릭
	 */
	ctrl.btnSearchHandler = function() {
		var self = et.history;
		self.data = null;
		self.createDatatable("click");
	}
	
	/**
	 *  다운로드 버튼 클릭
	 */
	ctrl.btnDownLoadHandler = function() {
		var self = et.history;
		
		$(".bxProcess").show();
		var headers = [ {
				field_id : "rownum",
				field_nm : "순번"
			}, {
				field_id : "call_bdtm",
				field_nm : "통화일자"
			}, {
				field_id : "team_name",
				field_nm : "팀명"
			}, {
				field_id : "tmr_nm",
				field_nm : "상담원명"
			}, {
				field_id : "view_cust_nm",
				field_nm : "고객명"
			}, {
				field_id : "view_cr_cd1",
				field_nm : "상담유형(대)"
			}, {
				field_id : "view_cr_cd2",
				field_nm : "상담유형(중)"
			}, {
				field_id : "view_cr_cd3",
				field_nm : "상담유형(소)"
			}, 
//			{
//				field_id : "view_cr_cd4",
//				field_nm : "상담유형(상세)"
//			}, 
			{
				field_id : "cr_mm",
				field_nm : "상담메모"
			}, {
				field_id : "state_nm", // 통화유형
				field_nm : "통화결과"
			}, {
				field_id : "call_bdttm", // 콜시작시간
				field_nm : "콜시작시간"
			}, {
				field_id : "call_sdttm",  // 콜저장시간
				field_nm : "콜저장시간"
			}, {
				field_id : "save_tm",  // 후처리시간
				field_nm : "후처리시간"
			}, {
				field_id : "view_hand_tel",
				field_nm : "전화번호"
			}, {
				field_id : "cust_id",
				field_nm : "고객아이디"
			}, {
				field_id : "callform_cd",
				field_nm : "인아웃구분"
			}
		];
		
		//self.excelExport(headers, self.tables.data());
		var params = $(self.tables.settings())[0].ajax.data;
		params.tmr_id = et.home.data.tmr_info.tmr_id;
		params.log_tmr_nm = et.home.data.tmr_info.tmr_nm;
		
		var url = "/ITX_HUMAN_Admin"+"/history/download";
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
	 *  히스토리 상세 버튼 클릭
	 */
	ctrl.btnHistoryTableHandler = function() {
		var self = et.history;
		// 팝업 띄우기
		var tempPop = window.open(
							getContextPath()+"/pop.historyTablePopup.sp", 
							"popHistoryTable",
							"width="+screen.availWidth+",height="+screen.availHeight+", location=no, toolbars=no, status=no");
	}
	
	/**
	 *  미저장 등록 버튼 클릭
	 */
	ctrl.btnSaveInfoHandler = function() {
		var self = et.history;
		// 팝업 띄우기
		var tempPop = window.open(
							getContextPath()+"/pop.saveInfoPopup.sp", 
							"popSaveInfoPopup",
							"width="+screen.availWidth+",height="+screen.availHeight+", location=no, toolbars=no, status=no");
	}
	
	/**
	 * 데이터 테이블 출력 완료 후, 비활성화한 화면 컨트롤 활성화.
	 * 
	 * @param {} settings
	 */
	ctrl.dataTableDrawCallback = function(settings) {
		var self = et.history;
		
		//settings.ajax.data.totalCount = settings.json.recordsFiltered;
		self.tableSearching = false;
		$("#"+self.name+"_btnSearch").prop("disabled", false);
		if (0 < this.fnGetData().length) {
			$("#"+self.name+"_btnDownLoad").prop("disabled", false);
			if (!!self.data) {
				if (self.data.viewStatus === "reserve") {
					var returnFlag = true;
					$.each(this.fnGetData(), function (index,item){
						if (item.call_id === self.data.call_id) {
							$("#"+self.name+"_tbList tbody tr:eq("+index+") td:eq(0)").trigger("click");
							returnFlag = false;
							return;
						}
					});
					if (returnFlag) {
						$("#"+self.name+"_tbList tbody td:eq(0)").trigger("click");
					}
					
				} else {
					$("#"+self.name+"_tbList tbody td:eq(0)").trigger("click");
				}
			} else {
				$("#"+self.name+"_tbList tbody td:eq(0)").trigger("click");
			}
		} else {
			if (et.historyDetail != null && et.historyDetail != undefined) {
				et.historyDetail.sendData(null);
			}
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
		var self = et.history;
		var rowData = et.getRowData("#"+self.name+"_tbList", $target.closest("tr"));
		if (!$target.is("img")) {
			console.log(rowData);
			if (et.historyDetail != null && et.historyDetail != undefined) {
				et.historyDetail.sendData(rowData);
			}
		} else {
			var url = et.home.api_url+"/admin/?";
			var giid = rowData.global_id;
			var stepid = rowData.item_id
			stepid = stepid.replace(/-/g, '');
			var param = "giid="+giid+"&stepid="+stepid+"#QM_CDR_RESULTS:QM_REVIEW_VOICE";
			window.open(url+param,'ITX REVIEW VIOCE','height=' + screen.height + ',width=' + screen.width + 'fullscreen=yes');
		}
	};
	
	/**
	 * 팝업에서 보내주는 메시지 받는 이벤트
	 */
	ctrl.receiveMessage = function(e) {
		var self = et.history;
		if (e.origin !== window.location.protocol+"//"+window.location.host) {
			return;
		}
		var data = {};
		if (e.data.type === "historyTablePop") {
			data.contextPath = getContextPath();
			data.code = ITX_CODE;
			data.codeAll = ITX_ALL_CODE;
			data.teamList = ITX_TEAM;
			data.tmrInfo = et.home.data.tmr_info;
			data.api_url = et.home.api_url;
			data.type = e.data.type;
			data.teamCd = self.team_cd;
			if (et.home.data.manager_id === undefined || et.home.data.manager_id === null) {
				data.is_manager = false;
			} else {
				data.is_manager = true;
			}
			e.source.postMessage(data, e.origin);
		} else if (e.data.type === "saveInfoPop") {
			data.contextPath = getContextPath();
			data.code = ITX_CODE;
			data.codeAll = ITX_ALL_CODE;
			data.tmrInfo = et.home.data.tmr_info;
			data.teamList = ITX_TEAM;
			data.api_url = et.home.api_url;
			data.type = e.data.type;
			data.teamCd = self.team_cd;
			if (et.home.data.manager_id === undefined || et.home.data.manager_id === null) {
				data.is_manager = false;
			} else {
				data.is_manager = true;
			}
			e.source.postMessage(data, e.origin);
		}
	};
	
	return ctrl;
}));