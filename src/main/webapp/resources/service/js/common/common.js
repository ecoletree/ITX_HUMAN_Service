// ******************************************************************************
// * Copyright (c) 2017 Ecoletree. All Rights Reserved.
// * 
// * Author : sgKim
// * Create Date : 2016. 6. 24.
// * DESC : 프로젝트 최상위 객체 정의
// ******************************************************************************
/* 공통적으로 사용되는 상수 모음 */
var ETCONST = {
	PROJECT_NAME : "Ecoletree_ITX_HUMAN_Service",	
	// alert type
	ALERT_TYPE_INFO : "info",
	ALERT_TYPE_CONFIRM : "confirm",
	PAGING_LENGTH : 10,
	// 상세페이지 모드 설정
	DETAIL_MODE_ADD : "add",
	DETAIL_MODE_UPDATE : "update",
	SUCCESS : "success",
	ERROR : "error",
	DUPLICATE : "duplicate",
	ERROR_PLACEMENT : ".showErrorMessage",
};

/* 공통적으로 사용되는 문자열형태 정규식 모음 */
var ETREGEXP = {
	USER_ID : "^[a-zA-Z0-9]+$", // 영문 대소문자 및 숫자
	PASSWORD : "^[a-zA-Z0-9@#*_\\-,.]+$", // password rule
	CODE_ID : "^[a-zA-Z0-9]+$", // 영문 대소문자 및 숫자
	//EMAIL : "^.{1,36}@[\w-]+(\.[a-z]+){1,3}$",
};

/* BP API 상태값 가져오기 */
var BP_STATE = {
		READY : "ready",
		RINGING : "ringing",
		BUSY : "busy",
		AFTER_CALL_WORK : "after_call_work",
		NOT_READY : "not_ready"
};

/* BP API Handler command 값 고정 */
var BP_COMMAND = {
		GET_STATE : "GET_STATE",
		STATE_CHANGED : "STATE_CHANGED",
		INTERACTION_RENDERED : "INTERACTION_RENDERED",
		INTERACTION_COMPLETED : "INTERACTION_COMPLETED",
		I_CALL_OFFERED : "_I_CALL_OFFERED",
		I_CALL_ANSWERED : "_I_CALL_ANSWERED",
		O_CALL_DIALING : "_O_CALL_DIALING",
		O_CALL_CONNECTED : "_O_CALL_CONNECTED",
		CALL_ENDED : "_CALL_ENDED",
		CALL_RECORDING_STATUS : "call_recording_status",
		LOGIN_CHANGED : "LOGIN_CHANGED"
};

//DataTables Default
var lang_eng = {
    "decimal" : "",
    "emptyTable" : "No data available in table",
    "info" : "Showing _START_ to _END_ of _TOTAL_ entries",
    "infoEmpty" : "Showing 0 to 0 of 0 entries",
    "infoFiltered" : "(filtered from _MAX_ total entries)",
    "infoPostFix" : "",
    "thousands" : ",",
    "lengthMenu" : "Show _MENU_ entries",
    "loadingRecords" : "Loading...",
    "processing" : "Processing...",
    "search" : "Search : ",
    "zeroRecords" : "No matching records found",
    "paginate" : {
        "first" : "First",
        "last" : "Last",
        "next" : "Next",
        "previous" : "Previous"
    },
    "aria" : {
        "sortAscending" : " :  activate to sort column ascending",
        "sortDescending" : " :  activate to sort column descending"
    }
};

// Korean
var lang_kor = {
    "decimal" : "",
    "emptyTable" : "데이터가 없습니다.",
    "info" : "_START_ - _END_ (총 _TOTAL_ 건)",
    "infoEmpty" : "",
    "infoFiltered" : "(전체 _MAX_ 건 중 검색결과)",
    "infoPostFix" : "",
    "thousands" : ",",
    "lengthMenu" : "_MENU_ 개씩 보기",
    "loadingRecords" : "로딩중...",
    "processing" : "처리중...",
    "search" : "검색 : ",
    "zeroRecords" : "검색된 데이터가 없습니다.",
    "paginate" : {
        "first" : "첫 페이지",
        "last" : "마지막 페이지",
        "next" : "다음",
        "previous" : "이전"
    },
    "aria" : {
        "sortAscending" : " :  오름차순 정렬",
        "sortDescending" : " :  내림차순 정렬"
    }
};

/** 코드 관련 데이터 **/
var ITX_CODE = [];
var ITX_ALL_CODE = [];

/** 팀 관련 데이터 **/
var ITX_TEAM = [];

/* common utils */
(function(g, ctrl) {
	var errorMsg = "";
	
	if (!$) { // check jQuery Plugin
		errorMsg = "jQuery is null or jQuery's namespace is crashed. This Util need jQuery plugin.";
	} else if (!_) { // check lodash
		errorMsg = "lodash util plugin is null or '_' overwrite other value. This Util need lodash plugin.";
	} else if (!g.ecoletree) { // [OK] create ecoletree object
		Object.defineProperty(g, "ecoletree", {
			value : ctrl(g)
		});
	} else if (g.ecoletree.name !== ETCONST.PROJECT_NAME) { // check
		// pre-existing
		// ecoletree's name
		errorMsg = "ecoletree Crash namespace!!!!!!";
	}
	
	if (!!errorMsg) {
		console.error(errorMsg);
		Object.defineProperty(g, "ecoletree", {
			value : {}
		});
	}
}(this, function(g) {
	
	"use strict";
	
	var ctrl = {}; // g.ecoletree
	var __c = ctrl.__fn = {}; // private ctrls.
	
	
	ctrl.name = ETCONST.PROJECT_NAME;
	
	ctrl.vc = null; // 현재 보고있는 화면의 VC 객체 저장
	ctrl.alert = null; // alert 팝업창의 VC 객체 저장
	
	/**
	 * messages.properties 메시지 획득
	 * 
	 * @param {string} key messages.properties에 선언한 메시지 key
	 * @param {string} param 메시지에 전달할 파라미터 n개. (ex. msg.test={0}은 {1}이다.
	 * et.getMsg("msg.test", "사과", "과일") > "사과은 과일이다"
	 * @return messages.properties 메시지
	 */
	ctrl.getMsg = jQuery.i18n.prop;
	
	/**
	 * element 활성화 / 비활성화
	 * 
	 * @param {String} element 요소 id. "#id" required.
	 * @param {boolean} isAble 활성화 true. 비활성화 false. 기본값 false
	 */
	ctrl.toggleElement = function(element, isAble) {
		var $elem = $(element);
		var disabled = isAble !== true;
		
		if ($elem.is("a")) {
			if (disabled) {
				$elem.addClass("disabled");
			} else {
				$elem.removeClass("disabled");
			}
		} else if ($elem.is("label")) {
			if (disabled) {
				$elem.addClass("state-disabled");
			} else {
				$elem.removeClass("state-disabled");
			}
		} else {
			$elem.prop("disabled", disabled);
		}
	};
	
	/**
	 * form 내부 input, select, button을 전부 활성화 / 비활성화
	 * 
	 * @param {string} formId
	 * @param {boolean} isAble 활성화 true. 비활성화 false. 기본값 false
	 */
	ctrl.toggleFormElements = function(formId, isAble) {
		$(formId).find("input,select,textarea,button,a").each(function(index) {
			var $this = $(this);
			if (isAble === true) {
				if ($(this).data("activeElement")) {
					ecoletree.toggleElement(this, true);
				}
			} else {
				$this.data("activeElement", !$this.prop("disabled"));
				ecoletree.toggleElement(this, false);
			}
		});
	};
	
	/**
	 * 에러 메시지 표시할 dom을 생성하거나 탐색하여 해당 dom에 메시지를 출력합니다.
	 * 
	 * @param {string} message 출력할 메시지.
	 * @param {string|jQuery} targetElem dom id. 에러 메시지를 출력할 자리의 기준이 될 element의
	 * id. 지정한 dom 다음에 메시지가 위치하게 된다.
	 */
	ctrl.showMessage = function(message, targetElem) {
		var self = this;
		var $pDiv, $errorDiv;
		
		// 부모 div 탐색
		if (ETValidate.isString(targetElem) && targetElem.charAt(0) !== "#") {
			targetElem = "#" + targetElem;
		}
		$pDiv = $(targetElem).closest(".input-group");
		$pDiv = ($pDiv.length === 0) ? $(targetElem).closest("div") : $pDiv.parent("div");
		
		// 에러메시지를 추가하기 위한 div를 탐색 또는 생성.
		$errorDiv = $pDiv.find(ETCONST.ERROR_PLACEMENT);
		if ($errorDiv.length === 0) {
			$errorDiv = $("<div />").addClass(ETCONST.ERROR_PLACEMENT.replace(".", "")).addClass("error").appendTo($pDiv);
		}
		
		// 에러메시지 세팅
		$errorDiv.html(message);
	};
	
	/**
	 * 화면의 모든 메시지를 제거한다
	 */
	ctrl.removeMessage = function() {
		$(ETCONST.ERROR_PLACEMENT).html("");
	};
	
	/**
	 * 체크박스에 Y/N 값으로 체크상태 변경
	 * 
	 * @param $cb 체크박스
	 * @param value Y/N 값
	 */
	ctrl.setCheckboxYN = function($cb, value) {
		$($cb).prop("checked", value === "Y");
	};
	
	/**
	 * 체크박스 체크 여부에 따라 Y/N 획득
	 * 
	 * @param $cb 체크박스
	 * @return 체크상태일경우 "Y", 아니면 "N"
	 */
	ctrl.getCheckboxYN = function($cb) {
		return $($cb).prop("checked") ? "Y" : "N";
	};
	
	/**
	 * base64Image를 $img에 설정
	 * 
	 * @param $img image dom. 없으면 신규 dom 생성
	 * @param base64Image base64 image. 없으면 no image 표현
	 * @param isHTMLTxt {boolean} true 일 경우 반환값이 html text
	 * @return $img
	 */
	ctrl.setBase64Image = function($img, base64Image, isHTMLTxt) {
		var imgSrc;
		$img = $($img);
		if (!$img || $img.length === 0) {
			$img = $("<img />");
		}
		
		if (!!base64Image) {
			imgSrc = "data:image/png;base64," + base64Image;
		} else {
			imgSrc = getContextPath() + "/resources/ecoletree/img/imgNoImage.png";
		}
		
		$img.prop("src", imgSrc);
		return (isHTMLTxt !== true) ? $img : $img[0].outerHTML;
	};
	
	/**
	 * 엔터 키 입력시 등록된 함수가 동작하도록 이벤트를 bind.
	 * 
	 * @param {String} target 타겟 셀렉트 정보. "#id" 등. required.
	 * @param {Function} runFn 엔터를 입력하면 동작할 함수. required.
	 * @param {Object} eventData 이벤트에 전달할 데이터
	 * @param {String} eventName bind할 이벤트. 기본값은 "keydown"
	 */
	ctrl.setEnterKeyDownEvent = function(target, runFn, eventData, eventName) {
		$(target).on((ETValidate.isString(eventName)) ? eventName : "keydown", eventData, function(e) {
			if (e.keyCode === 13) {
				runFn(e.data);
			}
		});
	};
	
	/**
	 * 넘버만 입력 가능 하도록 이벤트를 bind.
	 * 
	 * @param {String} target 타겟 셀렉트 정보. "#id" 등. required.
	 * @param {Object} eventData 이벤트에 전달할 데이터
	 * @param {String} eventName bind할 이벤트. 기본값은 "keydown"
	 */
	ctrl.setNumberKeyPressEvent = function(target, runFn, eventData, eventName) {
		$(target).on((ETValidate.isString(eventName)) ? eventName : "keyup", eventData, function(e) {
			this.value = this.value.replace(/[^0-9]/g,"");
		});
	};
	
	// ==============================DataTables Util==============================
	/**
	 * DataTable에 설정할 옵션 생성
	 * 
	 * @param {Array|object} columns 컬럼정보.
	 * @param {string} url
	 * @param {object} seearchParam 검색 데이터
	 * @param {function(settings)} drawCallback 데이터 출력 완료 후 호출될 함수를 등록
	 * @param {boolean|String} typeValue 페이징 or 스크롤. 문자열이면 스크롤, 이외의 값은 페이징.<br>
	 * 문자열 적용시 스크롤 사용시 고정될 데이터 테이블 높이로 설정
	 */
	ctrl.createDataTableSettings = function(columns, url, searchParam, drawCallback, typeValue, info, dataSet) {
		var option, ajax;
		
		option = {};
		
		// 공통 기본설정
		if (info == undefined) {
			option.info = true;
		} else {
			option.info = info;
		}
		option.ordering = false;
		option.lengthChange = false;
		option.searching = false;
		option.processing = true;
		option.destroy = true;
		// option.scrollCollapse = true; // 보여줄 행 수가 제한될 때 테이블 높이를 줄일 것인지 여부?
		// default : false
		
		option.serverSide = !!url;
		option.language = lang_kor;
		option.select= {
		      toggleable: false
		};
		
		if (typeof typeValue !== "string") { // 페이징 처리
			option.paging = typeValue !== false;
			option.pageLength = ETCONST.PAGING_LENGTH;
		} else { // 문자열일 경우 스크롤 테이블을 생성하고 테이블 높이로 문자열 대입
			option.paging = false;
			//option.scrollY = typeValue;
			//option.scrollCollapse = false;
		}
		
		if (_.isArray(columns)) {
			option.columns = columns;
		} else if (_.isObject(columns)) {
			option.columns = _.toArray(columns);
		}
		
		if (!!url) {
			ajax = {};
			ajax.url = getContextPath() + url;
			ajax.type = "post";
			ajax.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
			ajax.dataType = "JSON";
			ajax.error = function(xhr, ajaxOptions, thrownError) {
				//console.error(xhr.responseText);
				if (xhr.responseText.indexOf("404ERROR") > -1) { // send html document string
					location.href = getContextPath()+"/open404Error";
				} else if (xhr.responseText.indexOf("SESSIONERROR") > -1) {
					location.href = getContextPath()+"/sessionTimeout";
				} else if (xhr.responseText.indexOf("500ERROR") > -1) {
					location.href = getContextPath()+"/open500Error";
				} else {
					location.href = getContextPath()+"/open500Error";
				}
			};
			if (_.isObject(searchParam)) {
				searchParam.dataLength = ETCONST.PAGING_LENGTH;
			} else {
				searchParam = {
					dataLength : ETCONST.PAGING_LENGTH,
				};
			}
			ajax.data = searchParam;
			
			option.ajax = ajax;
		} else {
			option.data = dataSet;
		}
		
		if (_.isFunction(drawCallback)) {
			option.drawCallback = drawCallback;
		}
		
		return option;
	};
	
	/**
	 * DataTable Row 선택 (row 클릭시 선택 css를 주기 위한 클래스 추가)
	 * 
	 * @param {string} tableID "#id". required
	 * @param {function($target,row,col,columnVisible)} rowClickAction 테이블의 클릭시
	 * 동작할 함수를 등록합니다.
	 */
	ctrl.setDataTableRowSelection = function(tableId, rowClickAction) {
		$(tableId + " tbody").on("click", "tr", function(e) {
			var $this = $(this);
			var $target = $(e.target);
			var dataTable = $(tableId).DataTable();
			var cell;
			
			if ($this.hasClass("selected")) { // 선택 취소
				if (dataTable.init().select !== undefined) {
					if (dataTable.init().select.toggleable === true) {
						$this.removeClass("selected");
					}
				}
			} else {
				dataTable.$("tr.selected").removeClass("selected");
				$this.addClass("selected");
			}
			
			if (_.isFunction(rowClickAction)) { // 실행할 함수가 등록되어 있을 경우 함수를 실행한다.
				cell = dataTable.cell($target.closest("td")).index();
				if (!!cell) {
					rowClickAction($target, cell.row, cell.column, cell.columnVisible);
				}
			}
		});
	};
	
	/**
	 * 데이터테이블 row의 전체 데이터를 반환. 값이 없으면 null 반환
	 * 
	 * @param {string} tableId "#id"
	 * @param {} row
	 * @returns {object} rowdata 또는 null.
	 */
	ctrl.getRowData = function(tableId, row) {
		var $row = $(row);
		if ($row.length !== 0) {
			return $(tableId).DataTable().row($row).data();
		} else {
			return null;
		}
	};
	
	/**
	 * 데이터테이블 하단 페이징 컴포넌트 비활성화
	 * 
	 * @param {string} tableId tableId "#id"
	 */
	ctrl.disableDataTablePaging = function(tableId) {
		$(tableId + "_paginate>ul>li").each(function(index) {
			$(this).addClass("disabled");
		});
	};
	
	/**
	 * DataTable 데이터 반환
	 * 
	 * @param {string} tableID "#id". required
	 * @returns 데이터테이블의 데이터 목록
	 */
	ctrl.getDataTableData = function(tableId) {
		var apidata = $(tableId).DataTable().data();
		var data = [];
		var i, len;
		
		for (i = 0, len = apidata.count(); i < len; i++) {
			data.push(apidata[i]);
		}
		
		return data;
	};
	
	/**
	 * 선택된 row 데이터를 반환. 선택된 데이터가 없을 경우 null을 반환
	 * 
	 * @param {string} tableId "#id"
	 * @returns {object} rowdata 또는 null.
	 */
	ctrl.getSelectedRowDataInDataTable = function(tableId) {
		var $row = $(tableId + " tbody").find("tr.selected:eq(0)");
		return ecoletree.getRowData(tableId, $row);
	};
	
	/**
	 * 날짜 랜더러 공통함수
	 * 
	 * @param {} data The data for the cell
	 * @param {string} type The type call data requested - this will be
	 * 'filter', 'display', 'type' or 'sort'.
	 * @param {object} row row의 전체 데이터
	 * @param {object} meta row, col index 및 settings 정보가 들어있다.
	 */
	ctrl.setDateRender = function(data, type, row, meta) {
		var strDate = "";
		
		// 한글자면 두글자로 바꿔주는 내부함수
		var twoWords = function(value) {
			return value < 10 ? "0" + value : value;
		};
		
		if (data == null || data == undefined) {
			strDate = "-";
		} else {
			var date = new Date(parseFloat(data));
			strDate = date.getFullYear() + "-" + twoWords((date.getMonth() + 1)) + "-" + twoWords(date.getDate());
		}
		return strDate;
	};
	
	// ==============================ETValidator Util==============================
	/**
	 * validator의 에러 메시지 표시 공통함수
	 * 
	 * @param error 에러 메시지? 인가?
	 * @param element 에러가 발생한 대상 element 인듯...
	 */
	ctrl.setErrorPlacement = function(error, element) {
		ecoletree.showMessage(error, element);
	};
	
	/**
	 * 클래스명으로 에러 표시할 위치를 찾아 에러메시지를 표시<br>
	 * 에러 발생 대상의 name이 "user_name"이라면 탐색되는 클래스는 ".erroruser_name"
	 * 
	 * @param message 메시지
	 * @param targetElem 에러가 발생한 대상 element
	 */
	ctrl.setErrorPlacementFindClass = function(message, targetElem) {
		var $target = $(targetElem);
		var $pForm = $target.closest("fieldset");
		var elemName = $target.attr("name");
		var $errorDiv;
		
		if (!elemName) {
			console.log("element hasn't name!");
			return;
		}
		
		$errorDiv = $pForm.find(".error" + elemName);
		if (!!$errorDiv && $errorDiv.length !== 0) {
			$errorDiv.show();
			$errorDiv.html(message);
		} else {
			console.log("error placement is not exist!");
		}
	};
	
	// TODO 이하 불필요한거 함수 제거 필요
	
	/**
	 * input에 readonly(like disabled) 설정
	 * 
	 * @param {} element required.
	 * @param {boolean} isAble 활성화 true. 비활성화 false. 기본값 false
	 */
	ctrl.toggleInputReadonly = function(element, isAble) {
		var $elem = $(element);
		var disabled = isAble !== true;
		
		$elem.prop("readonly", disabled);
		if (disabled) {
			$elem.on("focus", function(e) {
				this.blur();
			}).css("cursor", "not-allowed");
		} else {
			$elem.off("focus").css("cursor", "default");
		}
	};
	
	// ==============================ETService Util==============================
	/**
	 * 저장에 실패할 경우 메시지 & 콘솔을 출력하는 공통함수
	 * 
	 * @param {object} data
	 */
	ctrl.errorSave = function(data) {
		if (data.error === "DUPLICATE_ERROR") {
			ecoletree.showAlert(ETCONST.ALERT_TYPE_INFO, "", data.errorMessage + ETMESSAGE.FAILED_DUPLICATE);
		} else {
			ecoletree.showAlert(ETCONST.ALERT_TYPE_INFO, "", ETMESSAGE.FAILED_SAVE);
		}
		ecoletree.toggleFormElements("form", true);
		console.error(data);
	};
	
	/**
	 * 삭제에 실패할 경우 메시지 & 콘솔을 출력하는 공통함수.
	 * 
	 * @param {object} data
	 */
	ctrl.errorDelete = function(data) {
		ecoletree.showAlert(ETCONST.ALERT_TYPE_INFO, "", ETMESSAGE.FAILED_DELETE);
		console.error(data);
	};
	
	// ==============================Date Util==============================
	
	/**
	 * date를 문자열로 변경해 리턴
	 */
	ctrl.getDateStr = function(str) {
		return (str.getFullYear() + '-' + (str.getMonth() + 1) + '-' + str.getDate());
	};
	
	/**
	 * 오늘 날짜를 문자열로 리턴
	 */
	ctrl.getToday = function() {
		var date = new Date();
		return ecoletree.getDateStr(date);
	};
	
	/**
	 * 한달 전 날짜를 리턴
	 */
	ctrl.getLastMonth = function() {
		var date = new Date();
		var monthOfYear = date.getMonth();
		date.setMonth(monthOfYear - 1);
		return ecoletree.getDateStr(date);
	};
	
	// ==============================Select Util==============================
	
	/**
	 * input select에 동적으로 option을 만들어줌
	 * 
	 * @param {array} aDataList option으로 보여질 데이터 리스트
	 * @param {object} oOptionKey option에 value와 text 값에 대응하는 필드명을 담고 있음, value와 text로 고정되어 있음
	 * @param {string} sTargetID option이 들어가게 될 select의 id 값, #selectID 형태
	 * @param {string} sFirstOptionText option 맨 위에 디폴트 값이 있는 경우 보여질 text를 넘김
	 */
	ctrl.makeSelectOption = function(aDataList, oOptionKey, sTargetID, sFirstOptionText) {
		if (!sTargetID) {
			return;
		}
		
		$(sTargetID).empty();
		
		if (!!aDataList) {
			var option;

			if (!!sFirstOptionText && sFirstOptionText != "") {
				option = $('<option>', {
					value : "",
					text : sFirstOptionText,
					selected : ""
				});
				$(sTargetID).append(option);
			}
			
			for (var i = 0; i < aDataList.length; i++) {
				option = $('<option>', {
					value : aDataList[i][oOptionKey.value],
					text : aDataList[i][oOptionKey.text]
				});
				option.data(aDataList[i]);
				$(sTargetID).append(option);
			}
		} else {
			if (!!sFirstOptionText) {
				var option = $('<option>', {
					value : "",
					text : sFirstOptionText,
					selected : ""
				});
				$(sTargetID).append(option);
			} 
		}
	};
	
	// ==============================sessionStorage Util==============================
	/**
	 * sessionStorage에서 값 추출
	 * 
	 * @param {string} sKey sessionStorage에 저장된 데이터를 가져오기 위한 key값
	 */
	ctrl.getSessionStorageItem = function(sKey) {
		var value = sessionStorage.getItem(sKey);
		if(!value) value = "";
		else value = JSON.parse(value);
		return value;
	};
	
	// ==============================암호화 Util==============================
	/**
	 * 숫자에 맞춰 랜덤한 영,숫자 가져오기
	 */
	ctrl.randomStr = function(param){
		var text1="";
		var alphabet = "abcdefghijklmnopqrstuvwxyz";
	    var num = "0123456789";
	    for (var i=0; i < (param/2) ; i++) {
	    	text1 += alphabet.charAt(Math.floor(Math.random() * alphabet.length));
	    	text1 += num.charAt(Math.floor(Math.random() * num.length));
	    }
	    return text1;
	};
	
	// ==============================Code Util==============================
	/**
	 * 데이터를 AES256으로 암호화 한다
	 */
	ctrl.cryptoAES = function (param) {
		var rndstr = ecoletree.randomStr(16);
        var secret = rndstr;
       	var jsonObj = JSON.stringify(param);
    	var iv = CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
        var salt = CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
        var aesUtil = new AesUtil(256, 1000);
        var ciphertext = aesUtil.encrypt(salt, iv, secret, jsonObj);
        var aesParamData = secret+"§"+iv + "§" + salt + "§" + ciphertext;
        return aesParamData;
	};
	
	/**
	 * 코드 데이터를 가져온다
	 * code : 코드값 필수
	 * item1 : 안넣으면  1분류 값을 가져온다.
	 * item2 : item1을 넣으면 2분류 값을 가져온다
	 * item3 : item1, item2 를 넣으며 3분류 값을 가져온다  
	 * item4 : item1, item2, item3를 넣으며 4분류 값을 가져온다  
	 */
	ctrl.getCodeList = function (code,item1,item2,item3,item4) {
		var list = [];
		var prefix = "***";
		$.each(ITX_CODE, function (index, item) {
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
	
	/**
	 * 삭제된 코드를 포함하여 코드 데이터를 가져온다
	 * code : 코드값 필수
	 * item1 : 안넣으면  1분류 값을 가져온다.
	 * item2 : item1을 넣으면 2분류 값을 가져온다
	 * item3 : item1, item2 를 넣으며 3분류 값을 가져온다  
	 * item4 : item1, item2, item3를 넣으며 4분류 값을 가져온다
	 */
	ctrl.getCodeAllList = function (code,item1,item2,item3,item4) {
		var list = [];
		var prefix = "***";
		$.each(ITX_ALL_CODE, function (index, item) {
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
	
	/**
	 * 모바일인지 아닌지 체크 
	 * 
	 */
	ctrl.isMobile = function () {
		var filter = "win16|win32|win64|mac";
		var bool = false;
		if(navigator.platform){

			if(0 > filter.indexOf(navigator.platform.toLowerCase())) {
				bool = true;
			} else {
				bool = false;
			}
		}
		
		return bool;
	}
	
	/**
	 * SCS 에 접속한다
	 */
	ctrl.scsLogin = function (tmrInfo, loginType) {
		var url = "cti://phone?";
		var passwd = encodeURI(tmrInfo.bp_pw);
		if (loginType === "login") {
			url += "command="+loginType;
			url += "&name="+tmrInfo.extension;
			url += "&domain="+tmrInfo.domain;
			url += "&proxy="+tmrInfo.proxy;
			url += "&id="+tmrInfo.bp_id;
			url += "&passwd="+passwd;
		} else {
			url = "cti://phone?command=logout";
		}
		window.location.href = url;
	} 
	
	return ctrl;
}));
