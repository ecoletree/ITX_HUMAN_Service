<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<head>
	<title>긴급접수</title>
	<!-- CSS ================================================== -->
	<c:import url="/include.commonHeader.sp" charEncoding="UTF-8" />
	
	<!-- JavaScript ================================================== -->
	<c:import url="/include.commonPlugin.sp" charEncoding="UTF-8" />
</head>
<script type="text/javascript">
var detailData = null;
var path = null;
var session_tmr_nm = "";
var cTmrList = [];
$(function (){
	var data = {};
	data.type = "emergencyPop";
	data.action = "init";
	window.addEventListener("message", receiveMessage, false);
	window.opener.postMessage(data, window.location.protocol+"//"+window.location.host+getContextPath());
	
	ecoletree.setNumberKeyPressEvent("#emergencyPopup_iptBirth");
	ecoletree.setNumberKeyPressEvent("#emergencyPopup_iptTel");

	function receiveMessage(e)
	{	
		console.log(window.location.protocol+"//"+window.location.host);
		if (e.origin !== window.location.protocol+"//"+window.location.host) {
			return;
		} 
		
		path = window.location.protocol+"//"+window.location.host+"/"+e.data.contextPath;
		detailData = e.data.detailData;
		ITX_CODE = e.data.code;
		cTmrList = e.data.cTmrList;
		session_tmr_nm = e.data.tmr_nm;
		setView(detailData);
	}
});

function setView(data) {
	
	var depDate = new Date();
	var arrDate = new Date();
	var symDate = new Date();
	if (!!data) {
		depDate = new Date(data.view_dep_dt);
		arrDate = new Date(data.view_arr_dt);
		symDate = new Date(data.view_sym_dt);
	}
	$("#emergencyPopup_dfDep_dt").datepicker({
		format: "yyyy.mm.dd",
		language: "kr",
		todayHighlight : true
	}).datepicker("setDate", depDate);
	$("#emergencyPopup_dfArr_dt").datepicker({
		format: "yyyy.mm.dd",
		language: "kr",
		todayHighlight : true
	}).datepicker("setDate", arrDate);
	$("#emergencyPopup_dfSym_dt").datepicker({
		format: "yyyy.mm.dd",
		language: "kr",
		todayHighlight : true
	}).datepicker("setDate", symDate);
	
	
	$("#emergencyPopup_btnDep_dt").on("click", function(e) {
		$("#emergencyPopup_dfDep_dt").datepicker("show",{
			format: "yyyy.mm.dd",
			language: "kr",
			todayHighlight : true,
			setDate : depDate
        }).on("changeDate", function (e) {
            $(this).datepicker("hide")
        });
	});
	$("#emergencyPopup_btnArr_dt").on("click", function(e) {
		$("#emergencyPopup_dfArr_dt").datepicker("show",{
			format: "yyyy.mm.dd",
			language: "kr",
			todayHighlight : true,
			setDate : arrDate
        }).on("changeDate", function (e) {
            $(this).datepicker("hide")
        });
	});
	$("#emergencyPopup_btnSym_dt").on("click", function(e) {
		$("#emergencyPopup_dfSym_dt").datepicker("show",{
			format: "yyyy.mm.dd",
			language: "kr",
			todayHighlight : true,
			setDate : symDate
        }).on("changeDate", function (e) {
            $(this).datepicker("hide")
        });
	});
	var locList = ecoletree.getCodeList ("091");
	var rCodeList = ecoletree.getCodeList ("092");
	ecoletree.makeSelectOption(locList, {value:"item1",text:"item_nm"},"#emergencyPopup_selLoc_gb");
	ecoletree.makeSelectOption(rCodeList, {value:"item1",text:"item_nm"},"#emergencyPopup_selR_code");
	
	ecoletree.makeSelectOption(cTmrList, {value:"tmr_id",text:"tmr_nm"},"#emergencyPopup_selC_tmr","선택해주세요");
	
	
	if (data === null || data === undefined) {
		$("#emergencyPopup_iptUC_Id").val("");
		$("#emergencyPopup_iptCust_name").val("");
		$("#emergencyPopup_iptBirth").val("");
		$("#emergencyPopup_iptCountry").val("");
		$("#emergencyPopup_iptR_name").val("");
		$("#emergencyPopup_iptTel").val("");
		$("#emergencyPopup_iptAddr").val("");
		$("#emergencyPopup_iptVc_name").val("");
		$("#emergencyPopup_iptVia_name").val("");
		$("#emergencyPopup_iptSym_desc").val("");
		$("#emergencyPopup_iptMedicine").val("");
		$("#emergencyPopup_iptEtc").val("");
		$("#emergencyPopup_iptR_tmr").val(session_tmr_nm);
		$("#emergencyPopup_iptCall_dt").val("");
		$("#emergencyPopup_iptCall_tm").val("");
		$("#emergencyPopup_selC_tmr").val("");
		
		$("#btnAdd").text("긴급접수");
	} else {
		$("#emergencyPopup_iptUC_Id").val(data.uc_id);
		$("#emergencyPopup_iptCust_name").val(data.cust_name);
		$("#emergencyPopup_iptBirth").val(data.birth);
		$("#emergencyPopup_iptCountry").val(data.country);
		$("#emergencyPopup_iptR_name").val(data.r_name);
		$("#emergencyPopup_iptTel").val(data.tel);
		$("#emergencyPopup_selLoc_gb").val(data.loc_gb);
		$("#emergencyPopup_iptAddr").val(data.addr);
		$("#emergencyPopup_iptVc_name").val(data.vc_name);
		$("#emergencyPopup_iptVia_name").val(data.via_name);
		$("#emergencyPopup_dfDep_dt").val(data.dep_dt);
		$("#emergencyPopup_dfArr_dt").val(data.arr_dt);
		$("#emergencyPopup_dfSym_dt").val(data.sym_dt);
		$("#emergencyPopup_iptSym_desc").val(data.sym_desc);
		$("#emergencyPopup_iptMedicine").val(data.medicine);
		$("#emergencyPopup_iptEtc").val(data.etc);
		$("#emergencyPopup_selR_code").val(data.r_code);
		$("#emergencyPopup_iptR_tmr").val(data.r_tmr);
		$("#emergencyPopup_iptCall_dt").val(data.call_dt);
		$("#emergencyPopup_iptCall_tm").val(data.call_tm);
		$("#emergencyPopup_selC_tmr").val(data.c_tmr);
		
		$("#btnAdd").text("수정");
	}
}

function btnAddClickListener() {
	var params = {};
	params.uc_id = $("#emergencyPopup_iptUC_Id").val();
	params.cust_name = $("#emergencyPopup_iptCust_name").val();
	params.birth = $("#emergencyPopup_iptBirth").val();
	params.country = $("#emergencyPopup_iptCountry").val();
	params.r_name = $("#emergencyPopup_iptR_name").val();
	params.tel = $("#emergencyPopup_iptTel").val();
	params.loc_gb = $("#emergencyPopup_selLoc_gb").val();
	params.addr = $("#emergencyPopup_iptAddr").val();
	params.vc_name = $("#emergencyPopup_iptVc_name").val();
	params.via_name = $("#emergencyPopup_iptVia_name").val();
	params.dep_dt = $("#emergencyPopup_dfDep_dt").val();
	params.arr_dt = $("#emergencyPopup_dfArr_dt").val();
	params.sym_dt = $("#emergencyPopup_dfSym_dt").val();
	params.sym_desc = $("#emergencyPopup_iptSym_desc").val();
	params.medicine = $("#emergencyPopup_iptMedicine").val();
	params.etc = $("#emergencyPopup_iptEtc").val();
	params.r_code = $("#emergencyPopup_selR_code").val();
	params.r_tmr = $("#emergencyPopup_iptR_tmr").val();
	params.call_tm = $("#emergencyPopup_iptCall_tm").val();
	params.c_tmr = $("#emergencyPopup_selC_tmr").val();
	if (params.c_tmr === "") {
		ecoletree.alert.show(ETCONST.ALERT_TYPE_INFO, "", "담당자를 선택해주세요");
		return;
	}
	
	if (validation(params)) {
		new ETService().setSuccessFunction(function (result){
			if (result.message === ETCONST.SUCCESS) {
				$("#emergencyPopup_iptUC_Id").val(result.data.uc_id);
				var data = {};
				data.type = "emergencyPop";
				data.action = "saveComplete";
				window.opener.postMessage(data, window.location.protocol+"//"+window.location.host+getContextPath());
				ecoletree.alert.show(ETCONST.ALERT_TYPE_INFO, "", "저장되었습니다.");
			} else {
				ecoletree.alert.show(ETCONST.ALERT_TYPE_INFO, "", "저장에 실패하였습니다.");
			}
		}).callService("/ucase/saveUCase", params);
	}
}

function validation(params){
	params.dep_dt = params.dep_dt.replace(/\./gi, ""); 
	params.arr_dt = params.arr_dt.replace(/\./gi, ""); 
	params.sym_dt = params.sym_dt.replace(/\./gi, ""); 
	return true;
}

</script>
<body style="height: unset">
<!-- 최초 width:770px height: 770px 사이즈 조정 가능함 -->
	<div class="popForm" >
	<input type="hidden" id="emergencyPopup_iptUC_Id">
		<div class="popFormHead"><h6><img src="${cp }/resources/ecoletree/img/icon_emergency.png" /> 긴급접수</div>
		<div class="popFormBody">
			<div class="row">
				<div class="col-sm-4 col-xs-12">
					<p>이름</p>
					<input id="emergencyPopup_iptCust_name" type="text" maxlength="100">
				</div>
				<div class="col-sm-4 col-xs-12">
					<p>생년월일</p>
					<input id="emergencyPopup_iptBirth"  type="text" maxlength="8" autocomplete="off"  style='IME-MODE: disabled' >
				</div>
				<div class="col-sm-4 col-xs-12">
					<p>연락처</p>
					<input id="emergencyPopup_iptTel"  type="text" maxlength="20">
				</div>
			
			</div>
			<div class="row">
				<div class="col-sm-4 col-xs-12">
					<p>국적</p>
					<input id="emergencyPopup_iptCountry"  type="text" maxlength="50">
				</div>
				<div class="col-sm-4 col-xs-12">
					<p>신고자</p>
					<input id="emergencyPopup_iptR_name"  type="text" maxlength="100">
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4 col-xs-12">
					<p>현재 위치 구분</p>
					<select id="emergencyPopup_selLoc_gb" >
					</select>
				</div>
				<div class="col-sm-8 col-xs-12">
					<p>현재 위치 주소</p>
					<input id="emergencyPopup_iptAddr"  type="text" maxlength="100">
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4 col-xs-12">
					<p>방문국가</p>
					<input id="emergencyPopup_iptVc_name"  type="text"  maxlength="30">
				</div>
				<div class="col-sm-4 col-xs-12">
					<p>경유국가</p>
					<input id="emergencyPopup_iptVia_name"  type="text" maxlength="30">
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4 col-xs-12">
					<p>출국일</p>
					<div class="bxEcoleDate">
						<input id="emergencyPopup_dfDep_dt" type="text" placeholder="" data-dateformat="yyyy.mm.dd" readyonly="readonly">
						<span id="emergencyPopup_btnDep_dt" class="input-group-addon"><img src="${cp }/resources/ecoletree/img/btn_cal.png" /></span>
					</div>
				</div>
				<div class="col-sm-4 col-xs-12">
					<p>입국일</p>
					<div class="bxEcoleDate">
						<input id="emergencyPopup_dfArr_dt" type="text" placeholder="" data-dateformat="yyyy.mm.dd" readyonly="readonly">
						<span id="emergencyPopup_btnArr_dt" class="input-group-addon"><img src="${cp }/resources/ecoletree/img/btn_cal.png" /></span>
					</div>
				</div>
				<div class="col-sm-4 col-xs-12">
					<p>최초 증상 발현일</p>
					<div class="bxEcoleDate">
						<input id="emergencyPopup_dfSym_dt"  type="text" placeholder="" data-dateformat="yyyy.mm.dd" readyonly="readonly">
						<span id="emergencyPopup_btnSym_dt"  class="input-group-addon"><img src="${cp }/resources/ecoletree/img/btn_cal.png" /></span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 col-xs-12">
					<p>현재 증상</p>
					<input id="emergencyPopup_iptSym_desc"  type="text" maxlength="400">
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4 col-xs-12">
					<p>약복용</p>
					<input id="emergencyPopup_iptMedicine"  type="text" maxlength="20">
				</div>
				<div class="col-sm-8 col-xs-12">
					<p>추가 확인 상태</p>
					<input id="emergencyPopup_iptEtc"  type="text" maxlength="400">
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4 col-xs-12">
					<p>처리상태</p>
					<select id="emergencyPopup_selR_code" >
					</select>
				</div>
				<div class="col-sm-4 col-xs-12">
					<p>접수자</p>
					<input id="emergencyPopup_iptR_tmr"  type="text" maxlength="10">
				</div>
				<div class="col-sm-4 col-xs-12">
					<p>담당자</p>
					<select id="emergencyPopup_selC_tmr" >
					</select>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4 col-xs-12">
					<p>처리시간</p>
					<input id="emergencyPopup_iptCall_dt"  type="text" disabled="disabled" >
				</div>
				<div class="col-sm-4 col-xs-12">
					<p>통화시간</p>
					<input id="emergencyPopup_iptCall_tm"  type="text" maxlength="20">
				</div>
			</div>
			
			
			<button id="btnAdd" class="btnBlue" onclick="btnAddClickListener()"></button>
		</div>
	</div>
	<c:import url="/common.alert.sp" charEncoding="UTF-8" />
</body>
</html>
