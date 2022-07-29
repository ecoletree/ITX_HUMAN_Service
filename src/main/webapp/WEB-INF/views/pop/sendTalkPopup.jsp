<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<head>
	<title>발송하기</title>
	<!-- CSS ================================================== -->
	<c:import url="/include.commonHeader.sp" charEncoding="UTF-8" />
	
	<!-- JavaScript ================================================== -->
	<c:import url="/include.commonPlugin.sp" charEncoding="UTF-8" />
</head>
<script type="text/javascript">

var path = null;
var tmr_id = null;
var scriptList = [];
var scriptData = {};
var toList = [];

var resultMsg;

$(function (){
	var data = {};
	data.type = "talkPop";
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
		tmr_id = e.data.tmr_id;
		scriptList = e.data.scriptList;
		
		// 발신번호 리스트 데이터 생성
		if (!!e.data.to) {
			var obj = {
					label : e.data.to,
					value : (e.data.to).replace(/-/gi, ""),
					id : new Date().getTime().toString(),
			};
			toList.push(obj);
			createToList();
		}
		
		setView();
	}
});

function setView() {
	ecoletree.makeSelectOption(scriptList, {value:"gb_code",text:"gb_name"},"#talk_type");
	$("#talk_type").find("option:eq(0)").prop("selected", true);
	talk_type_changeEventListener();
}

function createToList() {
	$("#talk_toList").empty();
	
	var li, span, a;
	_.forEach(toList, function(item) {
		li = '<li id="li_'+item.id+'"></li>';
		$("#talk_toList").append(li);
		
		span = '<span id="span_'+item.id+'">'+item.label+'</span>';
		$("#li_"+item.id).append(span);
		
		a = '<a id="a_'+item.id+'" class="btnBlue btnSmall">삭제</a>';
		$("#li_"+item.id).append(a);
		$("#a_"+item.id).on("click", function(e) {
			var id = (e.currentTarget.id).replace("a_", "");
			_.remove(toList, function(tempItem) {
				return (id === tempItem.id)
			});
			createToList();
		});
	});
}

function talk_type_changeEventListener() {
	if ($("#talk_type").val() === "001") {
		$("#talk_messge").removeClass("tareaSms").addClass("tareaTalk");
	} else {
		$("#talk_messge").removeClass("tareaTalk").addClass("tareaSms");
	}
	
	var tempList = $("#talk_type").find("option:selected").data().children;
	ecoletree.makeSelectOption(tempList, {value:"sc_code",text:"sc_name"},"#talk_msgType");
	$("#talk_msgType").find("option:eq(0)").prop("selected", true);
	talk_msgType_changeEventListener();
}

function talk_msgType_changeEventListener() {
	scriptData = $("#talk_msgType").find("option:selected").data();
	var msg = scriptData.sentent_mm;
	$("#talk_messge").val(msg);
}

function talk_to_keyDownEventListener() {
	if (event.keyCode === 13) {
		talk_addTo_clickEventListener();
	}
}

function talk_addTo_clickEventListener() {
	var value = $("#talk_to").val().trim();
	if (value !== "") {
		var obj = {
				label : value,
				value : value.replace(/-/gi, ""),
				id : new Date().getTime().toString(),
		};
		toList.push(obj);
		$("#talk_to").val("");
		createToList();
	}
}

function send_clickEventListener() {
	
	if (toList.length === 0) {
		ecoletree.alert.show(ETCONST.ALERT_TYPE_INFO, "", "고객번호를 넣어주세요.");
		return;
	}
	
	var type = scriptData.type;
	var refKey = $.format.date(new Date(), 'yyyyMMddHHmmss') + tmr_id + $("#talk_type").val();
	var content = {};

	if (type === "at") {
		// 알림톡
		content[type] = {
			message : $("#talk_messge").val(),	
			senderkey : scriptData.template_key,
			templatecode : scriptData.template_code,
		};
	} else {
// 		if (type === "sms" && 44 < $("#talk_messge").val().length) type = "lms";
		if (44 < $("#talk_messge").val().length) {
			type = "lms";
		} else {
			type = "sms";
		}
		
		
		// sms
		content[type] = {
			message : $("#talk_messge").val(),
		}
	}
	
	var postData = {
		type : type,
		from : $("#talk_from").val(),
		content : content,
		refkey : refKey,
		toList : toList
	};
	
	$("#talk_send").attr("disabled", "disabled");

	$.ajax({
		url : path + "/sendMessge",
		method : "post",
		contentType : "application/json; charset=UTF-8",
		dataType : "text",
		async : true,
		data : JSON.stringify(postData)
	}).done(function(returnData) {
		$("#talk_send").removeAttr("disabled");

		var result = JSON.parse(returnData);
		ecoletree.alert.show(ETCONST.ALERT_TYPE_INFO, "", "발송되었습니다.");
// 		ecoletree.alert.show(ETCONST.ALERT_TYPE_INFO, "", result.resultMsg);
		resultMsg = result.resultMsg;
	});
	
}

function testClick() {
	ecoletree.alert.show(ETCONST.ALERT_TYPE_INFO, "", resultMsg);
}

</script>
<body style="height: unset">
<!-- 최초 width:290px height: 730px 사이즈 조정 가능함 -->
	<div class="popForm" >
		<div class="popFormHead"><h6><img src="${cp }/resources/ecoletree/img/icon_sms.png" /> 발송하기</div>
		<div class="popFormBody">
			<div >
				<p>유형</p>
				<select id="talk_type" onchange="talk_type_changeEventListener()">
				</select>
			</div>
			<div >
				<p onclick="testClick()">발신번호</p>
				<input id="talk_from" type="text" value="1339">
			</div>
			<div >
				<p>상담유형</p>
				<select id="talk_msgType" onchange="talk_msgType_changeEventListener()">
				</select>
			</div>
			
			<textarea id="talk_messge" class="tareaSms"></textarea>
			
			<div class="bxBpHistory">
				<div class="wallHead">
					<div class="bxSearchForm">
						<p>고객번호</p>
						<div>
							<input id="talk_to" type="text" 
								onkeyup="this.value=this.value.replace(/[^0-9]/g,'');"
								onkeydown="talk_to_keyDownEventListener()">
						</div>
					</div>
					
					<div class="bxSearchForm">
						<a id="talk_addTo" class="btnBlue btnSearch" onclick="talk_addTo_clickEventListener()">추가</a>
					</div>
				</div>
				
				<ul id="talk_toList">
				</ul>
			</div>
			
			<button id="talk_send" class="btnBlue" onclick="send_clickEventListener()">발송하기</button>
		</div>
	</div>
	<c:import url="/common.alert.sp" charEncoding="UTF-8" />
</body>
</html>
