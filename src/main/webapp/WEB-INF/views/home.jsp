<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<head>
	<title>ITX_HUMAN</title>
	<!-- CSS ================================================== -->
	<c:import url="/include.commonHeader.sp" charEncoding="UTF-8" />
	
	<!-- JavaScript ================================================== -->
	<c:import url="/include.commonPlugin.sp" charEncoding="UTF-8" />
</head>
<!-- View Controller -->
<script type="text/javascript">
$(function (){
	$.getScript(getContextPath() + "/resources/service/js/home.js").done(function(script, textStatus) {
		if (!!ecoletree.home && ecoletree.home.name === "home") {
			ecoletree.home.init( ${initData},'${sessionScope.sessionVO.bp_url}' );
		} else {
			console.log("vc's name is not home : " + ecoletree.home.name);
		}
	}).fail(function(jqxhr, settings, exception) {
		console.log(arguments);
	});
})

</script>
<body>

<div class="content">
	
	<!-- 시작: 메인 -->
	<!-- 보일때 display: flex -->
	<div id="bxMain" class="bxBpMainWrap" style="display: flex">
	
		<div class="bxBpHeadWrap" >
			<div class="bxBpHead" >
				<div>
					<img src="${cp }/resources/ecoletree/img/icon_user.png" id="img_state"/><p><span id="home_spTmrId">${sessionScope.sessionVO.tmr_id}</span>(<span id="home_spTmrNm">${sessionScope.sessionVO.tmr_nm}</span>)</p>
					<img src="${cp }/resources/ecoletree/img/icon_info.png"/><p>마지막 로그인: <span>${sessionScope.sessionVO.login_dt}</span></p>
				</div>
				<div>
<!-- 					<a id="home_emergency"><span class="txtRed">긴급접수 하기</span></a> -->
<%-- 					<a id="home_specialTask" onclick="window.open('${cp }/pop.specialTaskForcePopup.sp','newwindow','width=1300px, height=900px, location=no, toolbars=no, status=no'); return false;"> --%>
<!-- 						<span >신종코로나 감염증 대책반</span> -->
<!-- 					</a> -->
<!-- 					<a id="home_healthCenter" ><span >전국 보건소</span></a> -->
<%-- 					<a id="home_satisfaction"><span ><img src="${cp }/resources/ecoletree/img/btn_research.png" />만족도 조사 문자</span></a> --%>
					<a id="home_imgTalk" ><img src="${cp }/resources/ecoletree/img/btn_sms.png" /><span >발송하기</span></a>
<%-- 					<c:if test="${initData.manager_id ne '' && initData.manager_id ne null}"> --%>
<%-- 						<a id="home_imgEmergency"><img src="${cp }/resources/ecoletree/img/btn_emergency.png" /><span id="home_spUCaseCnt">0</span></a> --%>
<%-- 					</c:if> --%>
					<a id="home_imgCallBack"><img src="${cp }/resources/ecoletree/img/btn_call.png" /><span id="home_spCallBackCnt"></span></a>
					<a id="home_imgNotice"><img src="${cp }/resources/ecoletree/img/btn_noti.png" /><span id="home_spNoticeCnt"></span></a>
					<button id="home_btnLogout"><img src="${cp }/resources/ecoletree/img/btn_logout.png" /><span>로그아웃</span></button>
				</div>
			</div>
		</div>
		
		<!-- 시작: 긴급알람 -->
		<!-- 보일때 display: block -->
<!-- 		<div class="bxEmergencyAlert" style="display: none;"> -->
<%-- 			<img src="${cp }/resources/ecoletree/img/icon_emergency2.png" /> --%>
<!-- 			<span><strong>긴급 접수건이 등록되었습니다.</strong></span> -->
<!-- 			<a class="pull-right" onclick="$('.bxEmergencyAlert').hide()">닫기</a> -->
<!-- 		</div> -->
		
		<!-- 시작: 공지알람 -->
		<!-- 보일때 display: block -->
		<div class="bxNoticeAlert" style="display: none;">
			<img src="${cp }/resources/ecoletree/img/icon_bell2.png" />
			<span><strong>공지사항이 등록되었습니다.</strong></span>
			<a class="pull-right" onclick="$('.bxNoticeAlert').hide()">닫기</a>
		</div>
		
		<!-- 시작: 민원알람 -->
		<!-- 보일때 display: block -->
<!-- 		<div id="home_divBlackList" class="bxaddNoteAlert" style="display: none;"> -->
<%-- 			<img src="${cp }/resources/ecoletree/img/icon_addNote.png" /> --%>
<!-- 			<span><strong>악성 민원인이 등록되었습니다.</strong></span> -->
<!-- 			<a class="pull-right" onclick="$('#home_divBlackList').hide()">닫기</a> -->
<!-- 		</div> -->
				
		<!-- 시작: 인입정보 -->
		<c:import url="/divs.callInfo.sp" charEncoding="UTF-8" />
		<!-- 끝: 인입정보 -->
		
		<!-- 시작: 이력 테이블 -->
		<c:import url="/divs.history.sp" charEncoding="UTF-8" />
		<!-- 끝: 이력 테이블 -->
		
		
		<!-- 시작: 이력 상세 -->
		<c:import url="/divs.historyDetail.sp" charEncoding="UTF-8"  />
		<!-- 끝: 이력 상세 -->
		
		
		<!-- 긴급접수 -->
		<!-- display: block; -->
		<div id="home_divEmergency" class="bxMainPopWrap" style="display: none;">
			<div class="popupHead">
				<div><img src="${cp }/resources/ecoletree/img/icon_emergency.png" /> 긴급접수</div>
				<a onclick="$('#home_divEmergency').hide()">x</a>
			</div>
			<div class="popupBody pH380">
				<div class="marginB10">
					<p>최근 업데이트: <span id="home_spEmergencyUpdate"></span> <a><img id="home_EmergencyRefresh"  src="${cp }/resources/ecoletree/img/btn_refresh.png" /></a></p>
				</div>
				<div class="ecloeScrollTableContainer">
					<div class="ecloeScrollTableHeader"></div>
					<div class="ecloeScrollTableWrap dataTables_wrapper form-inline dt-bootstrap">
						<table id="home_emergency_tbList" class="ecloeScrollTable ecloeTableSmall table table-hover ui-datatable dataTable dataTable-helper" style="width: 100%">
							<thead>
								<tr>
									<th style="width:40px; min-width:45px;"><div>순번</div></th>
									<th style="width:143px; min-width: 100px;"><div>일시</div> </th>
									<th style="width:100px; min-width:100px;"><div>처리상태</div></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>	
			</div><!-- ./popupBody -->
		</div>

		
		<!-- 콜백 -->
		<!-- display: block; -->
		<div id="home_divCallBack" class="bxMainPopWrap5" style="display: none;">
			<div class="popupHead">
				<div><img src="${cp }/resources/ecoletree/img/icon_call.png" /> 콜백</div>
				<a onclick="$('#home_divCallBack').hide()">x</a>
			</div>
			<div class="popupBody pH380">
				<div class="marginB10">
					<p>최근 업데이트: <span id="home_spCallBackUpdate"></span> <a><img id="home_CBRefresh" src="${cp }/resources/ecoletree/img/btn_refresh.png" /></a></p>
				</div>
				<div class="ecloeScrollTableContainer">
					<div class="ecloeScrollTableHeader"></div>
					<div class="ecloeScrollTableWrap dataTables_wrapper form-inline dt-bootstrap">
						<table id="home_cb_tbList" class="ecloeScrollTable ecloeTableSmall table table-hover ui-datatable dataTable dataTable-helper" style="width: 100%">
							<thead>
								<tr>
									<th style="width:40px; min-width:40px;"><div>순번</div></th>
									<th style="width:115px; min-width: 115px;"><div>인입번호</div> </th>
									<th style="width:115px; min-width: 115px;"><div>입력번호</div> </th>
									<th style="width:140px; min-width: 140px;"><div>지능망번호</div> </th>
									<th style="width:130px; min-width: 130px;"><div>등록일시</div></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>	
			</div><!-- ./popupBody -->
		</div>
		
		<!-- 공지사항 -->
		<!-- display: block; -->
		<div id="home_divNotice" class="bxMainPopWrap" style="display: none;">
			<div class="popupHead">
				<div><img src="${cp }/resources/ecoletree/img/icon_bell.png" /> 공지사항</div>
				<a onclick="$('#home_divNotice').hide()">x</a>
			</div>
			<div class="popupBody">
				<div class="marginB10">
					<p>최근 업데이트: <span id="home_spNoticeUpdate"></span> <a><img id="home_NotiRefresh" src="${cp }/resources/ecoletree/img/btn_refresh.png" /></a></p>
				</div>
				<div class="marginB5 bxNoticeSearch">
					<input id="home_noti_searchData" type="text">
					<a><img id="home_noti_search" src="${cp }/resources/ecoletree/img/btn_search.png" /></a></a>
				</div>
				<div class="dataTables_wrapper form-inline dt-bootstrap bxPopScroll">
				<!-- 테이블 10줄 한페이지로 // 페이징 버튼은 개발 후 css작업 예정 -->
					<table id="home_noti_tbList" class="ecloeTable ecloeTableSmall table table-hover ui-datatable dataTable dataTable-helper" style="width: 100%">
						<thead>
							<tr>
								<th style="width:40px; min-width:45px;">순번</th>
								<th >제목 </th>
							</tr>
						</thead>
						<tbody  class="cursorPoint">
						</tbody>
					</table>
				</div>	
			</div><!-- ./popupBody -->
		</div>
		
		<!-- 사리지는 알람 -->
		<!-- 보일때 display: block; -->
		<div id="home_divMessageAlert" class="bxNotifications" style="display: none">
			<p></p>
		</div>
		
		<div style="display: none">
		<form id="home_frm"  action="https://force.hyosungitx.com/message/request/websurvey/send.ajax" method="POST" target="iframe">
			<input type="hidden" name="campaignSeq"        value="d9056570-11f2-4038-a0ab-52ab53542204">
			<input type="hidden" name="surveySeq"          value="66b4cae0-499f-4151-844f-0c4e4c8b8b4c">
			<input type="hidden" name="relationSurveySeq"  value="cd795bb0-512b-4b35-b8dc-0833c5631dd0">
			<input type="hidden" id="home_attachedData" name="attachedData"       >
		</form>
		<iframe name="iframe" style="width:1px; height:1px; border:0; visibility:hidden;"></iframe>
		</div>
	</div>
	<!-- 끝: 메인 -->
	
</div>

	<!-- alert 모달 -->
	<c:import url="/common.alert.sp" charEncoding="UTF-8" />
</body>
</html>
