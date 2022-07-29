<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<head>
	<title>전국 보건소 연락처</title>
	<!-- CSS ================================================== -->
	<c:import url="/include.commonHeader.sp" charEncoding="UTF-8" />
	
	<!-- JavaScript ================================================== -->
	<c:import url="/include.commonPlugin.sp" charEncoding="UTF-8" />

</head>

<script type="text/javascript">

var msgAlertTimer = null; 

$(function (){
	var data = {};
	data.type = "healthCenterPop";
	data.action = "init";
	ecoletree.setEnterKeyDownEvent("#ipxCityDesc", btnSearch_clickEventListener);
	window.addEventListener("message", receiveMessage, false);
	window.opener.postMessage(data, window.location.protocol+"//"+window.location.host+getContextPath());

	function receiveMessage(e)
	{	
		console.log(window.location.protocol+"//"+window.location.host);
		if (e.origin !== window.location.protocol+"//"+window.location.host) {
			return;
		} 
		
		path = window.location.protocol+"//"+window.location.host+"/"+e.data.contextPath;
	}
});

function btnSearch_clickEventListener () {
	var param = {};
	var search = $("#ipxCityDesc").val();
	param.city_desc = search;
	
	var columns = [ {
			className : "txtCenter",
			data : "tel_code"
		}, {
			data : "city_desc"
		}, {
			data : "division_desc",
		}, {
			data : "position_name",
		}, {
			data : "cust_name",
		}, {
			data : "office_tel",
			render : function(data, type, full, meta) {
				var str = "";
				if (data !== null) {
					var tempTel = "9" + String(data).replace(/-/gi, "");
					str = data+'<button data-clipboard-text="'+tempTel+'" class="btnCopy" ></button>';
				}
				return str;
			},
		}, {
			data : "hand_tel",
			render : function(data, type, full, meta) {
				var str = "";
				if (data !== null) {
					var tempTel = "9" + String(data).replace(/-/gi, "");
					str = data+'<button data-clipboard-text="'+tempTel+'" class="btnCopy" ></button>';
				}
				return str;
			},
		}, {
			data : "fax_tel",
		}, {
			data : "exhand_tel",
			render : function(data, type, full, meta) {
				var str = "";
				if (data !== null) {
					var tempTel = "9" + String(data).replace(/-/gi, "");
					str = data+'<button data-clipboard-text="'+tempTel+'" class="btnCopy" ></button>';
				}
				return str;
			},
		}, {
			data : "email",
		}, {
			data : "ex_memo",
		}
	];
	var i =0;
	var clipboard = new ClipboardJS('.btnCopy');
	clipboard.on('success', _.debounce(function(e) {
		$(".bxCopyOk").show();
		if (!!msgAlertTimer) {
			clearTimeout(msgAlertTimer);
		}
		msgAlertTimer = null;
		msgAlertTimer = window.setTimeout(function() {
			$(".bxCopyOk").hide();
		}, 3*1000);
		i++;
	},500));
	$("#healthCenter_tbList").DataTable(ecoletree.createDataTableSettings(columns, "/healthCenter/getHealthCenterList", param, dataTableDrawCallback,"200px",false));
}

function dataTableDrawCallback(settings) {
	
}

</script>



<body>
	<!-- 최초 width:650px height: 600px 사이즈 조정 가능함 -->
	<span id=""></span>
<div class="contentPop bxHealthCenterWrap" >
	<div class="bxBpHistory">
		<div class="wallHead">
			<div class="bxSearchForm">
				<p>시구군</p>
				<div>
					<input id="ipxCityDesc" type="text">
				</div>
			</div>
			
			<div class="bxSearchForm">
				<a class="btnBlue btnSearch" id="healthCenter_btnSearch" onclick="btnSearch_clickEventListener()"><img src="${cp }/resources/ecoletree/img/btn_searchW.png" /> 조회</a>
			</div>
		</div>
		<div class="historyTableWrap">
			<div class="dataTables_wrapper form-inline">
				<table id="healthCenter_tbList" class="ecloeTable table table-hover dataTable">
					<thead>
						<tr>
							<th style="width: 50px; min-width:50px;"><div>순번</div></th>
							<th style="width: 135px; min-width:135px;"><div>시구군</div></th>
							<th style="width: 70px; min-width:70px;"><div>부서명</div></th>
							<th style="width: 80px; min-width:80px;"><div>직위</div></th>
							<th style="width: 70px; min-width:70px;"><div>성명</div></th>
							<th style="width: 140px; min-width:140px;"><div>사무실</div></th>
							<th style="width: 140px; min-width:140px;"><div>휴대폰</div></th>
							<th style="width: 140px; min-width:140px;"><div>FAX</div></th>
							<th style="width: 140px; min-width:140px;"><div>비상 휴대폰</div></th>
							<th style="width: 150px; min-width:150px;"><div>이메일</div></th>
							<th style="width: 150px; min-width:150px;"><div>비고</div></th>
						</tr>
					</thead>
					<tbody>
				</table>
			</div>
				
		</div>
	</div><!-- 끝: 이력 테이블 -->
</div>
<div class="bxCopyOk" style="display: none;">복사되었습니다. Ctrl+V를 눌러 붙여넣어 사용하세요.</div>
</body>
</html>
