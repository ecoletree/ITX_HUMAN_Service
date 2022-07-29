/*******************************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * @Author : aileen
 * @CreateDate : 2021. 04. 08.
 * @DESC : 홈 _ 메인
 ******************************************************************************/
(function(et, ctrl) {
	if (_.isObject(et) && et.name === ETCONST.PROJECT_NAME) {
		if (!et.outbound || et.outbound.name !== "outbound") {
			et.outbound = ctrl(et);
		}
	} else {
		console.error("ecoletree OR ETCONST is not valid. please check that common.js file was imported.");
	}
}(this.ecoletree, function(et) {
	
	"use strict";
	
	var ctrl = {};
	
	ctrl.name = "outbound";
	ctrl.path = "/outbound";
	
	ctrl.dbLayout = null;
	ctrl.dbStatusList = null;
	// ============================== 화면 컨트롤 ==============================
	/**
	 * init VIEW
	 */
	ctrl.init = function(initData) {
		var self = et.outbound;
		
		// set view
		// 분배일자 from
		$("#outbound_dfShareFrom").datepicker({
			format: "yyyy.mm.dd",
			language: "kr",
			todayHighlight : true,
		});
		$("#outbound_btnShareFrom").on("click", function(e) {
			if ($("#outbound_dfShareFrom").is( ":disabled" )) return;
			$("#outbound_dfShareFrom").datepicker("show",{
				format: "yyyy.mm.dd",
				language: "kr",
				todayHighlight : true,
            }).on("changeDate", function (e) {
                $(this).datepicker("hide");
            });
		});
		// 분배일자 to
		$("#outbound_dfShareTo").datepicker({
			format: "yyyy.mm.dd",
			language: "kr",
			todayHighlight : true,
		});
		$("#outbound_btnShareTo").on("click", function(e) {
			if ($("#outbound_dfShareTo").is( ":disabled" )) return;
			$("#outbound_dfShareTo").datepicker("show",{
				format: "yyyy.mm.dd",
				language: "kr",
				todayHighlight : true,
			}).on("changeDate", function (e) {
				$(this).datepicker("hide");
			});
		});
		
		// set Event
		$("#outbound_btnCloseModal").click(self.outbound_btnCloseModal_clickEventListener);
		$("#outbound_selState1").on("change", self.outbound_selState1_changeEventListener);
		$("#outbound_selCampaign").on("change", self.outbound_selCampaign_changeEventListener);
		$("#outbound_btnSearch").on("click", self.outbound_btnSearch_clickEventListener);
		
		et.setDataTableRowSelection("#outbound_tbList", self.outbound_tbListRowSelect);
	};
	
	/**
	 * dataTable을 생성
	 */
	ctrl.outbound_createDatatable = _.debounce(function() {
		var self = et.outbound;
		var params = {};
		
		$("#outbound_btnSearch").attr("disabled", "disabled");
		
		params.state_cd = $("#outbound_selState1").val();
		params.state_scd = $("#outbound_selState2").val();
		params.sdate = $("#outbound_dfShareFrom").val();
		params.edate = $("#outbound_dfShareTo").val();
		params.campaign_id = $("#outbound_selCampaign").val();
		params.order_type = $("#outbound_selSorting").val();
		
		var searchList = [];
		$("#outbound_divSearchCondition").find("input").each(function(index) { 
			var $this = $(this);
			var _oData = $this.data();
			
			if ($this.val() !== "") {
				_oData.search_value = $this.val();
				searchList.push(JSON.stringify(_oData));
			} else {
				_oData.search_value = null;
			}
		});
		params.search_list = JSON.stringify(searchList);
		
		var columns = [ {
				className : "txtCenter",
				data : "rownum",
			}, {
				className : "txtCenter",
				data : "view_share_dt"
			}, {
				data : "campaign_name"
			}, {
				data : "cust_nm",
			}, {
				data : "view_state_scd",
			}, {
				className : "txtCenter",
				data : "view_state_dt",
			}, {
				className : "txtCenter",
				data : "cust_id",
				render : function(data, type, full, meta) {
					return '<a class="btnBlue btnSmall"><img src="'+getContextPath()+'/resources/ecoletree/img/icon_dial.png" /></a>';
				},
			}
		];
		
		var option = et.createDataTableSettings(columns, self.path + "/getMyDBList", params, self.outbound_dataTableDrawCallback);
		option.autoWidth = false;
		option.pageLength = 10;
		$("#outbound_tbList").DataTable(option);
		
	},500);
	
	// ============================== 동작 컨트롤 ==============================
	
	// ============================== 동작 컨트롤 : 외부 등록 ==============================
	ctrl.showOutboundModal = function() {
		var self = et.outbound;
		
		// 화면 초기화
		$("#outbound_dfShareFrom").datepicker("setDate", new Date());
		$("#outbound_dfShareTo").datepicker("setDate", new Date());
		
		// 캠페인 리스트 조회
		new ETService().setSuccessFunction(self.getMyCampaignListCallSucceed).callService(self.path + "/getMyCampaignList", {});
		
	};
	ctrl.getMyCampaignListCallSucceed = function(returnData) {
		var self = et.outbound;
		var msg = returnData.resultMsg;
		var data = returnData.data;
		var codeList;
		
		if (msg === ETCONST.SUCCESS) {
			self.dbStatusList = data.codeList;
			// DB상태
			et.makeSelectOption(self.getDBStateCodeList(), {value:"item1",text:"item_nm"},"#outbound_selState1","선택해주세요");
			
			// 캠페인 리스트
			et.makeSelectOption(data.campaignList, {value:"campaign_id",text:"campaign_name"},"#outbound_selCampaign",null);
			$("#outbound_selCampaign").find("option:eq(0)").prop("selected", true).trigger("change");
		}
	};
	
	// ============================== 이벤트 리스너 ==============================
	/**
	 * 모달 닫기 버튼 클릭시
	 */
	ctrl.outbound_btnCloseModal_clickEventListener = function() {
		var self = et.outbound;
		
		// 테이블 초기화
		$("#outbound_tbList").DataTable().clear().destroy();

		// 화면 초기화
		$("#outbound_selSorting").find("option:eq(0)").prop("selected", true).trigger("change");
		
		$("#home_divOutbound").hide();
	};
	
	/**
	 * DB상태 변경시
	 */
	ctrl.outbound_selState1_changeEventListener = function() {
		var self = et.outbound;
		var item1 = $("#outbound_selState1").val();
		
		if (item1 === "") {
			$("#outbound_selState2").empty();
			et.makeSelectOption(null, {value:"item2",text:"item_nm"},"#outbound_selState2","선택해주세요");
		} else {
			et.makeSelectOption(self.getDBStateCodeList(item1), {value:"item2",text:"item_nm"},"#outbound_selState2","선택해주세요");
		}
	};

	/**
	 * 캠페인 변경시
	 */
	ctrl.outbound_selCampaign_changeEventListener = function() {
		var self = et.outbound;
		var params = $("#outbound_selCampaign option:selected").data();
		
		
		// 캠페인 선택에 따른 db 레이아웃 조회
		new ETService().setSuccessFunction(self.getDBLayoutByCompaignCallSucceed).callService(self.path + "/getDBLayoutByCompaign", params);
	};
	ctrl.getDBLayoutByCompaignCallSucceed = function(returnData) {
		var self = et.outbound;
		var msg = returnData.resultMsg;
		var data = returnData.data;
		
		if (msg === ETCONST.SUCCESS) {
			if (!!data) { // 캠페인으로 가져온 DB 레이아웃에 검색조건으로 설정된 필드를 화면에 그림
				self.dbLayout = data;
				$("#outbound_divSearchCondition").empty();
				_.forEach(data, function(item) {
					if (item.is_search === "Y") {
						var dom = '<div class="bxSearchForm"><p>'+item.header_name+'</p><div><input type="text" name="'+item.layout_cd+'"></div></div>';
						$("#outbound_divSearchCondition").append(dom);
						
						$("#outbound_divSearchCondition").find("input").each(function(index) {
							var $this = $(this);
							var _sName = $this.attr("name");
							if (_sName === item.layout_cd) {
								$this.data(item);
								return false;
							}
						});
					}
				});
			}
		}
	};
	
	/**
	 * 조회버튼 클릭시
	 */
	ctrl.outbound_btnSearch_clickEventListener = function() {
		var self = et.outbound;
		
		self.outbound_createDatatable();
	};
	
	/**
	 * 데이터 테이블 출력 완료 후, 비활성화한 화면 컨트롤 활성화.
	 * 
	 * @param {} settings
	 */
	ctrl.outbound_dataTableDrawCallback = function(settings) {
		var self = et.outbound;
		
		$("#outbound_btnSearch").removeAttr("disabled");
	};
	
	/**
	 * 테이블 row 선택시, 상세화면으로 이동
	 * 
	 * @param {jQuery} $target 클릭한 대상
	 * @param {} row 행 인덱스
	 * @param {} col 열 인덱스
	 */
	ctrl.outbound_tbListRowSelect = function($target, row, col) {
		var self = et.outbound;
		var rowData = et.getRowData("#outbound_tbList", $target.closest("tr"));
		
		if ($target.is("img")) {
			console.log(rowData);
			rowData.dbLayout = self.dbLayout;
			new ETService().setSuccessFunction(self.getDBDetailInfoCallSucceed).callService(self.path + "/getDBDetailInfo", rowData);
		}
	};
	ctrl.getDBDetailInfoCallSucceed = function(returnData) {
		var self = et.outbound;
		var msg = returnData.resultMsg;
		var data = returnData.data;
		
		if (msg === ETCONST.SUCCESS) {
			et.home.setOutboundData(self.dbLayout, data);
			$("#outbound_btnCloseModal").trigger("click");
		}
	};
	
	// ============================== 유틸 ==============================
	/**
	 * 코드 데이터를 가져온다
	 * item1 : 안넣으면  1분류 값을 가져온다.
	 * item2 : item1을 넣으면 2분류 값을 가져온다
	 * item3 : item1, item2 를 넣으며 3분류 값을 가져온다  
	 * item4 : item1, item2, item3를 넣으며 4분류 값을 가져온다
	 */
	ctrl.getDBStateCodeList = function (item1,item2,item3,item4) {
		var self = et.outbound;
		var code = '100';
		var list = [];
		var prefix = "***";
		$.each(self.dbStatusList, function (index, item) {
			if (item1 === null || item1 === undefined) {
				if (item.code === code && item.item1 !== prefix && item.item2 === prefix && item.item3 === prefix && item.item4 === prefix) {
					list.push(item);
				}
			} else if (item2 === null || item2 === undefined) {
				if (item.code === code && item.item1 === item1 && item.item2 !== prefix && item.item3 === prefix && item.item4 === prefix) {
					list.push(item);
				}
			} else if (item3 === null || item3 === undefined) {
				if (item.code === code && item.item1 === item1 && item.item2 === item2 && item.item3 !== prefix && item.item4 === prefix) {
					list.push(item);
				}
			} else if (item4 === null || item4 === undefined) {
				if (item.code === code && item.item1 === item1 && item.item2 === item2 && item.item3 === item3 && item.item4 !== prefix) {
					list.push(item);
				}
			}
		});
		
		return list;
	};
	
	return ctrl;
}));