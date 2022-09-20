<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<head>
	<title>상세보기</title>
	<!-- CSS ================================================== -->
	<c:import url="/include.commonHeader.sp" charEncoding="UTF-8" />
	
	<!-- JavaScript ================================================== -->
	<c:import url="/include.commonPlugin.sp" charEncoding="UTF-8" />

</head>

<script type="text/javascript">

var detailData = null;
var session_tmr_id = null;

var path = null;

$(function (){
	var data = {};
	data.type = "historyPop";
	data.action = "init";
	window.addEventListener("message", receiveMessage, false);
	window.opener.postMessage(data, window.location.protocol+"//"+window.location.host+getContextPath());

	function receiveMessage(e)
	{	
		console.log(window.location.protocol+"//"+window.location.host);
		if (e.origin !== window.location.protocol+"//"+window.location.host) {
			return;
		} 
		
		path = window.location.protocol+"//"+window.location.host+"/"+e.data.contextPath;
		
		detailData = e.data.detailData;
		ITX_ALL_CODE = e.data.code;
		
		setView(detailData);
	}
});

function setView(data) {
	$("#historyPopup_iptTmr_nm").val(data.tmr_nm);
	$("#historyPopup_iptTeam_name").val(data.team_name);
	$("#historyPopup_iptCall_id").val(data.call_id);
	$("#historyPopup_iptCall_bdt").val(data.call_bdtm);
// 	$("#historyPopup_iptCall_etm").val(data.call_etm);
// 	$("#historyPopup_iptCall_tm").val(data.call_tm);
	$("#historyPopup_iptCall_btm").val(data.call_bdttm);
	$("#historyPopup_iptCall_stm").val(data.call_sdttm);
	$("#historyPopup_iptSave_tm").val(data.save_tm);

	$("#historyPopup_iptCust_num").val(data.cust_id);
	$("#historyPopup_iptCust_nm").val(data.cust_nm);
	$("#historyPopup_iptHand_tel").val(data.view_hand_tel);
	$("#historyPopup_iptClass_1").val(data.class_1);
	$("#historyPopup_iptClass_2").val(data.class_2);
	$("#historyPopup_iptClass_3").val(data.class_3);
	
	var memo = data.cr_mm === null ? "" : (data.cr_mm).replace(/<br\s*\/?>/mg,"\n");
	var memo1 = data.cr_mm_add === null ? "" : (data.cr_mm_add).replace(/<br\s*\/?>/mg,"\n");
	$("#historyPopup_taCr_mm").val(memo);
	$("#historyPopup_taCr_mm_add").val(memo1);
	
	appendOption("#historyPopup_selCrCd1", data.cr_cd1, data.view_cr_cd1);
	appendOption("#historyPopup_selCrCd2", data.cr_cd2, data.view_cr_cd2);
	appendOption("#historyPopup_selCrCd3", data.cr_cd3, data.view_cr_cd3);
// 	appendOption("#historyPopup_selCrCd4", data.cr_cd4, data.view_cr_cd4);
	appendOption("#historyPopup_selExec_state", data.state_cd, data.state_nm);
}

/**
 * 수정버튼 클릭
 */
function btnUpdate_clickEventListener() {
	$("#historyPopup_divBtn1").hide();
	$("#historyPopup_divBtn2").show();
	
	$("#historyPopup_iptCust_nm").removeAttr("disabled");
	$("#historyPopup_iptClass_1").removeAttr("disabled");
	$("#historyPopup_iptClass_2").removeAttr("disabled");
	$("#historyPopup_iptClass_3").removeAttr("disabled");
	
	$("#historyPopup_selCrCd1").removeAttr("disabled");
	$("#historyPopup_selCrCd2").removeAttr("disabled");
	$("#historyPopup_selCrCd3").removeAttr("disabled");
// 	$("#historyPopup_selCrCd4").removeAttr("disabled");
	$("#historyPopup_selExec_state").removeAttr("disabled");
	$("#historyPopup_taCr_mm").removeAttr("disabled");
	$("#historyPopup_taCr_mm_add").removeAttr("disabled");

	// 대분류 코드
	codeList = ecoletree.getCodeAllList("010");
	ecoletree.makeSelectOption(codeList, {value:"item1",text:"item_nm"},"#historyPopup_selCrCd1","선택해주세요");
	$("#historyPopup_selCrCd1").val(detailData.cr_cd1);
	// 중분류 코드
	codeList = ecoletree.getCodeAllList("010", detailData.cr_cd1);
	ecoletree.makeSelectOption(codeList, {value:"item2",text:"item_nm"},"#historyPopup_selCrCd2","선택해주세요");
	$("#historyPopup_selCrCd2").val(detailData.cr_cd2);
	// 소분류 코드
	codeList = ecoletree.getCodeAllList("010", detailData.cr_cd1, detailData.cr_cd2);
	ecoletree.makeSelectOption(codeList, {value:"item3",text:"item_nm"},"#historyPopup_selCrCd3","선택해주세요");
	$("#historyPopup_selCrCd3").val(detailData.cr_cd3);
	// 소분류 코드
// 	codeList = ecoletree.getCodeAllList("010", detailData.cr_cd1, detailData.cr_cd2, detailData.cr_cd3);
// 	ecoletree.makeSelectOption(codeList, {value:"item4",text:"item_nm"},"#historyPopup_selCrCd4","선택해주세요");
// 	$("#historyPopup_selCrCd4").val(detailData.cr_cd4);
	// 처리코드
	codeList = ecoletree.getCodeAllList("011");
	ecoletree.makeSelectOption(codeList, {value:"item1",text:"item_nm"},"#historyPopup_selExec_state","선택해주세요");
	$("#historyPopup_selExec_state").val(detailData.state_cd);
}

/**
 * 취소버튼 클릭
 */
function btnCancel_clickEventListener() {
	$("#historyPopup_divBtn1").show();
	$("#historyPopup_divBtn2").hide();
	
	$("#historyPopup_iptCust_nm").attr("disabled", "disabled");
	$("#historyPopup_iptClass_1").attr("disabled", "disabled");
	$("#historyPopup_iptClass_2").attr("disabled", "disabled");
	$("#historyPopup_iptClass_3").attr("disabled", "disabled");
	
	$("#historyPopup_selCrCd1").attr("disabled", "disabled");
	$("#historyPopup_selCrCd2").attr("disabled", "disabled");
	$("#historyPopup_selCrCd3").attr("disabled", "disabled");
// 	$("#historyPopup_selCrCd4").attr("disabled", "disabled");
	$("#historyPopup_selExec_state").attr("disabled", "disabled");
	$("#historyPopup_taCr_mm").attr("disabled", "disabled");
	$("#historyPopup_taCr_mm_add").attr("disabled", "disabled");
	
	setView(detailData);
}

/**
 * 저장버튼 클릭
 */
function btnSave_clickEventListener() {
	var postData = {};
	
	postData.session_tmr_id = session_tmr_id;
	postData.call_id = detailData.call_id;
	postData.global_id = detailData.global_id;
	postData.item_id = detailData.item_id;
	postData.cr_sno = detailData.cr_sno;
	postData.io_flag = detailData.io_flag;
	postData.cust_id = $("#historyPopup_iptCust_num").val();
	postData.cust_nm = $("#historyPopup_iptCust_nm").val();
	postData.class_1 = $("#historyPopup_iptClass_1").val();
	postData.class_2 = $("#historyPopup_iptClass_2").val();
	postData.class_3 = $("#historyPopup_iptClass_3").val();
	
	postData.cr_cd1 = $("#historyPopup_selCrCd1").val();
	postData.cr_cd2 = $("#historyPopup_selCrCd2").val();
	postData.cr_cd3 = $("#historyPopup_selCrCd3").val();
// 	postData.cr_cd4 = $("#historyPopup_selCrCd4").val();
	postData.cr_mm = $("#historyPopup_taCr_mm").val();
	postData.cr_mm_add = $("#historyPopup_taCr_mm_add").val();
	postData.state_cd = $("#historyPopup_selExec_state").val();
	
// 	var optionData = $("#historyPopup_selCrCd4").find("option:selected").data();
// 	postData.o_state_cd = optionData.value1;
// 	postData.o_state_scd = optionData.value2;
	postData.o_state_cd = null;
	postData.o_state_scd = null;
	
	$.ajax({
		url : path + "/historyDetail/saveHistoryDetail",
		method : "post",
		contentType : "application/json; charset=UTF-8",
		dataType : "text",
		async : true,
		data : JSON.stringify(postData)
	}).done(function(returnData) {
		var result = JSON.parse(returnData);
		var msg = result.resultMsg;
		
		if (msg === ETCONST.SUCCESS) {
			var data = {};
			data.type = "historyPop";
			data.action = "saveComplete";
			window.opener.postMessage(data, window.location.protocol+"//"+window.location.host+getContextPath());
			
			ecoletree.alert.show(ETCONST.ALERT_TYPE_INFO, "", "이전 상담이력을 수정하였습니다.");
		} else {
			ecoletree.alert.show(ETCONST.ALERT_TYPE_INFO, "", "수정에 실패하였습니다.");
		}
	});
}

/**
 * 분류 코드 변경
 */
function selCrCd_changeEventListener(id) {
	var num = id.replace("historyPopup_selCrCd","");
	var selCd1, selCd2, selCd3, codeList;
	
	selCd1 = $("#historyPopup_selCrCd1").val();
	selCd2 = $("#historyPopup_selCrCd2").val();
	selCd3 = $("#historyPopup_selCrCd3").val();
	
	if (num === "1") {
		codeList = ecoletree.getCodeAllList("010", selCd1);
		ecoletree.makeSelectOption(null, {value:"item3",text:"item_nm"},"#historyPopup_selCrCd3","선택해주세요");
		$("#historyPopup_selCrCd3").find("option:eq(0)").prop("selected", true);
// 		ecoletree.makeSelectOption(null, {value:"item4",text:"item_nm"},"#historyPopup_selCrCd4","선택해주세요");
// 		$("#historyPopup_selCrCd4").find("option:eq(0)").prop("selected", true);
		if (!!codeList) {
			ecoletree.makeSelectOption(codeList, {value:"item2",text:"item_nm"},"#historyPopup_selCrCd2","선택해주세요");
			$("#historyPopup_selCrCd2").find("option:eq(0)").prop("selected", true);
			selCrCd_changeEventListener("historyPopup_selCrCd2");
		} else {
			ecoletree.makeSelectOption(null, {value:"item2",text:"item_nm"},"#historyPopup_selCrCd2","선택해주세요");
			$("#historyPopup_selCrCd2").find("option:eq(0)").prop("selected", true);
		}
	} else if (num === "2") {
		codeList = ecoletree.getCodeAllList("010", selCd1, selCd2);
// 		ecoletree.makeSelectOption(null, {value:"item4",text:"item_nm"},"#historyPopup_selCrCd4","선택해주세요");
// 		$("#historyPopup_selCrCd4").find("option:eq(0)").prop("selected", true);
		if (!!codeList) {
			ecoletree.makeSelectOption(codeList, {value:"item3",text:"item_nm"},"#historyPopup_selCrCd3","선택해주세요");
			$("#historyPopup_selCrCd3").find("option:eq(0)").prop("selected", true);
			selCrCd_changeEventListener("historyPopup_selCrCd3");
		} else {
			ecoletree.makeSelectOption(null, {value:"item3",text:"item_nm"},"#historyPopup_selCrCd3","선택해주세요");
			$("#historyPopup_selCrCd3").find("option:eq(0)").prop("selected", true);
		}
	} else if (num === "3") {
		codeList = ecoletree.getCodeAllList("010", selCd1, selCd2, selCd3);
		if (!!codeList) {
// 			ecoletree.makeSelectOption(codeList, {value:"item4",text:"item_nm"},"#historyPopup_selCrCd4","선택해주세요");
		} else {
// 			ecoletree.makeSelectOption(null, {value:"item4",text:"item_nm"},"#historyPopup_selCrCd4","선택해주세요");
		}
// 		$("#historyPopup_selCrCd4").find("option:eq(0)").prop("selected", true);
	}
}

/**
 * append select option
 */
function appendOption(sId, sValue, sLabel) {
	$(sId).empty();
	
	if (!!sValue) {
		$(sId).append("<option value='"+sValue+"'>"+ sLabel +"</option>");
	} else {
		$(sId).append("<option value=''>선택해주세요</option>");
	}
}
</script>

<body>
	<!-- 최초 width:650px height: 600px 사이즈 조정 가능함 -->
<div class="contentPop bxHistoryDetailWrap" >

	<div class="bxHistoryDetail marginB10">
		<input id="historyPopup_id" type="hidden"> 
		<div class="wallBody">
			<div class="row">
				<div class="col-xs-3">
					<p>상담사명</p>
					<input id="historyPopup_iptTmr_nm" type="text" disabled="disabled">
				</div>
				<div class="col-xs-3">
					<p>팀</p>
					<input id="historyPopup_iptTeam_name" type="text" disabled="disabled">
				</div>
				<div class="col-xs-3">
					<p>통화일자</p>
					<input id="historyPopup_iptCall_bdt" type="text" disabled="disabled">
				</div>
				<div class="col-xs-3">
					<p>콜 시작시간</p>
					<input id="historyPopup_iptCall_btm" type="text" disabled="disabled">
				</div>
				<div class="col-xs-3">
					<p>콜 저장시간</p>
					<input id="historyPopup_iptCall_stm" type="text" disabled="disabled">
				</div>
				<div class="col-xs-3">
					<p>후처리시간</p>
					<input id="historyPopup_iptSave_tm" type="text" disabled="disabled">
				</div>
				<!-- 
				<div class="col-xs-3">
					<p>종료시간</p>
					<input id="historyPopup_iptCall_etm" type="text" disabled="disabled">
				</div>
				<div class="col-xs-3">
					<p>상담시간</p>
					<input id="historyPopup_iptCall_tm" type="text" disabled="disabled">
				</div>-->
			</div>
		</div>
	</div>
	
	<div class="bxHistoryDetail marginB10">
		<div class="wallBody">
			<div class="row">
				<div class="col-xs-3">
					<p>고객 아이디</p>
					<input id="historyPopup_iptCust_num" type="text" disabled="disabled">
				</div>
				<div class="col-xs-3">
					<p>고객명</p>
					<input id="historyPopup_iptCust_nm" type="text" disabled="disabled">
				</div>
				<div class="col-xs-3">
					<p>인입 전화번호</p>
					<input id="historyPopup_iptHand_tel" type="text" disabled="disabled">
				</div>
			</div>
		</div><!-- ./wallBody -->
	</div>
		
	<div class="bxHistoryDetail">
		<div class="wallHead">
			<div><h6>이전 상담 이력</h6></div>
			<div>
				<div id="historyPopup_divBtn1" style="display: block">
					<a id="historyPopup_btnUpdate" class="btnBlue btnSmall" onclick="btnUpdate_clickEventListener()">수정하기</a>
				</div>
				<div id="historyPopup_divBtn2" style="display: none">
					<a id="historyPopup_btnCancel" class="btnGray btnSmall" onclick="btnCancel_clickEventListener()">취소 </a>
					<a id="historyPopup_btnSave" class="btnBlue btnSmall" onclick="btnSave_clickEventListener()">저장하기</a>
				</div>
			</div>
		</div>
		<div class="wallBody">
			<div id="historyPopup_divConsulting" class="row">
				<div class="col-xs-3">
					<p>상담유형(대)</p>
					<select id="historyPopup_selCrCd1" disabled="disabled" onchange="selCrCd_changeEventListener('historyPopup_selCrCd1')"><option>선택해주세요</option></select>
				</div>
				<div class="col-xs-3">
					<p>상담유형(중)</p>
					<select id="historyPopup_selCrCd2" disabled="disabled" onchange="selCrCd_changeEventListener('historyPopup_selCrCd2')"><option>선택해주세요</option></select>
				</div>
				<div class="col-xs-3">
					<p>상담유형(소)</p>
					<select id="historyPopup_selCrCd3" disabled="disabled" onchange="selCrCd_changeEventListener('historyPopup_selCrCd3')"><option>선택해주세요</option></select>
				</div>
<!-- 				<div class="col-xs-3"> -->
<!-- 					<p>상담유형(상세)</p> -->
<!-- 					<select id="historyPopup_selCrCd4" disabled="disabled" onchange="selCrCd_changeEventListener('historyPopup_selCrCd4')"><option>선택해주세요</option></select> -->
<!-- 				</div> -->
				<div class="col-xs-3">
					<p>통화결과</p>
					<select id="historyPopup_selExec_state" disabled="disabled"><option>선택해주세요</option></select>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6">
					<div class="bxMemo">
						<span>민원인 상담내용 |</span>
						<textarea id="historyPopup_taCr_mm" rows="" cols="" disabled="disabled"></textarea>
					</div>
				</div>
				<div class="col-xs-6">
					<div class="bxMemo">
						<span>상담사 답변 |</span>
						<textarea id="historyPopup_taCr_mm_add" rows="" cols="" disabled="disabled"></textarea>
					</div>
				</div>
			</div>
		</div><!-- ./wallBody -->
	</div>
</div>
<c:import url="/common.alert.sp" charEncoding="UTF-8" />
</body>
</html>
