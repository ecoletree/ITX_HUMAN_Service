<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<head>
	<title>미저장 등록</title>
	<!-- CSS ================================================== -->
	<c:import url="/include.commonHeader.sp" charEncoding="UTF-8" />
	
	<!-- JavaScript ================================================== -->
	<c:import url="/include.commonPlugin.sp" charEncoding="UTF-8" />

</head>

<script type="text/javascript">

</script>



<body>
	<!-- 최초 width:650px height: 600px 사이즈 조정 가능함 -->
<div class="contentPop bxSaveInfoWrap" >
	<div class="bxBpHistory">
		<div class="wallHead">
			<div class="bxSearchForm">
				<p>조회일자</p>
				<div>
					<div class="bxEcoleDate">
						<input id="history_sdate" type="text" placeholder="" data-dateformat="yyyy.mm.dd" readyonly="readonly">
						<span id="history_btnSdate" class="input-group-addon"><img src="${cp }/resources/ecoletree/img/btn_cal.png" /></span>
					</div>
					<span> ~ </span>
					<div class="bxEcoleDate">
						<input id="history_edate" type="text" placeholder="" data-dateformat="yyyy.mm.dd" readyonly="readonly">
						<span id="history_btnEdate" class="input-group-addon"><img src="${cp }/resources/ecoletree/img/btn_cal.png" /></span>
					</div>
				</div>
			</div>
			<div class="bxSearchForm">
			
				<p>조회조건1</p>
				<div>
					<select id="history_sel1">
						<option value="">선택해주세요</option>
						<option value="cust_nm">고객명</option>
						<option value="search_tmr_nm">상담사명</option>
						<option value="team_cd">팀 명</option>
						<option value="search_hand_tel">전화번호</option>
						<option value="cr_mm">메모</option>
					</select>
					<input type="text" id="history_sel1_search" readonly="readonly">
					<select id="history_sel1_team" style="display : none"></select>
					<select id="history_sel1_remark" style="display : none"></select>
				</div>
			</div>
			<div class="bxSearchForm">
				<p>조회조건2</p>
				<div>
					<select id="history_sel2">
						<option value="">선택해주세요</option>
						<option value="cust_nm">고객명</option>
						<option value="search_tmr_nm">상담사명</option>
						<option value="team_cd">팀 명</option>
						<option value="search_hand_tel">전화번호</option>
						<option value="cr_mm">메모</option>
					</select>
					<input type="text" id="history_sel2_search" readonly="readonly">
					<select id="history_sel2_team" style="display : none"></select>
					<select id="history_sel2_remark" style="display : none"></select>
				</div>
			</div>
			<div class="bxSearchForm">
				<button class="btnBlue btnSearch" id="history_btnSearch"><img src="${cp }/resources/ecoletree/img/btn_searchW.png" /> 조회</button>
			</div>
			<div class="bxSearchForm">
				<button class="btnBlue btnSearch" id="history_btnDownLoad" disabled="disabled"><img src="${cp }/resources/ecoletree/img/btn_excelW.png" /> 다운</button>
			</div>
		</div>
	
		<div class="historyTableWrap">
			<div class="dataTables_wrapper form-inline">
				<table id="history_tbList" class="ecloeTable ecloeTableFixed table table-hover dataTable">
					<thead>
						<tr>
							<th style="width: 50px; min-width:50px;"><div>순번</div></th>
							<th style="width: 135px; min-width:135px;"><div>통화일자</div></th>
							<th style="width: 80px; min-width:80px;"><div>Call ID</div></th>
							<th style="width: 80px; min-width:80px;"><div>상담사ID</div></th>
							<th style="width: 80px; min-width:80px;"><div>상담사명</div></th>
							<th style="width: 200px; min-width:200px;"><div>고객아이디</div></th>
							<th style="width: 80px; min-width:80px;"><div>고객명</div></th>
							<th style="width: 110px; min-width:110px;"><div>전화번호</div></th>
							<th style="width: 50px; min-width:50px;"><div>듣기</div></th>
						</tr>
					</thead>
					<tbody class="cursorPoint">
					</tbody>
				</table>
			</div>
				
		</div>
	</div><!-- 끝: 이력 테이블 -->
	
	<div id="uiTest1" style="display: none;" class="bxShowHistoryDetail">
		<button class="btnShowHistoryDetail" onclick="{uiTest1.style.display='none';uiTest2.style.display='flex';}">등록하기 창열기</button>
	</div>
	<div id="uiTest2" class="bxBpHistoryDetail">
		<div>
		
			<div class="bxHistoryDetail marginB10">
				<div class="wallHead">
					<div><h6>미저장 등록하기</h6></div>
					<div>
<!-- 						<a class="btnGray btnSmall">취소하기</a> -->
						<a id="historyPopup_btnSave" class="btnBlue btnSmall">저장하기</a>
					</div>
				</div>
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
					
					<div class="row">
					
						<div class="col-xs-3">
							<p>통화시간</p>
							<div class="time">
								<select id="historyPopup_selHour"></select>
								<span>:</span>
								<select id="historyPopup_selMin"></select>
								<span>:</span>
								<select id="historyPopup_selSec"></select>
							</div>
						</div>
						
					</div>
					<div class="row">
						<div class="col-xs-3">
							<p>상담유형(대)</p>
							<select id="historyPopup_selCrCd1" ><option>선택해주세요</option></select>
						</div>
						<div class="col-xs-3">
							<p>상담유형(중)</p>
							<select id="historyPopup_selCrCd2" ><option>선택해주세요</option></select>
						</div>
						<div class="col-xs-3">
							<p>상담유형(소)</p>
							<select id="historyPopup_selCrCd3" ><option>선택해주세요</option></select>
						</div>
<!-- 						<div class="col-xs-3"> -->
<!-- 							<p>상담유형(상세)</p> -->
<!-- 							<select id="historyPopup_selCrCd4" ><option>선택해주세요</option></select> -->
<!-- 						</div> -->
						<div class="col-xs-3">
							<p>통화결과</p>
							<select id="historyPopup_selExec_state" ><option>선택해주세요</option></select>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-6">
							<div class="bxMemo">
								<span>민원인 상담내용 |</span>
								<textarea id="historyPopup_taCr_mm" rows="" cols="" ></textarea>
							</div>
						</div>
						<div class="col-xs-6">
							<div class="bxMemo">
								<span>상담사 답변 |</span>
								<textarea id="historyPopup_taCr_mm_add" rows="" cols="" ></textarea>
							</div>
						</div>
					</div>
				</div><!-- ./wallBody -->
			</div>
			<button class="btnHideHistoryDetail" onclick="{uiTest1.style.display='block';uiTest2.style.display='none';}">등록하기 창닫기</button>
		</div>
	</div>
	
</div>

<!-- 작업중 -->
<div class="bxProcess" style="display: none;">
	<div>
		<img src="${cp }/resources/ecoletree/svg/SwingPreloader.svg" alt="loading">
		<h6>작업중입니다.</h6>
		<p>작업이 완료될 때까지 잠시만 기다려주세요.</p>
	</div>
</div>

<!-- alert 모달 -->
<c:import url="/common.alert.sp" charEncoding="UTF-8" />

<script type="text/javascript">
$.getScript(getContextPath() + "/resources/service/js/pop/saveInfoPopup.js").done(function(script, textStatus) {
	if (!!ecoletree.saveInfoPopup && ecoletree.saveInfoPopup.name === "history") {
		ecoletree.saveInfoPopup.init('${sessionScope.sessionVO.tmr_info.team_cd}' );
	} else {
		console.log("vc's name is not home : " + ecoletree.saveInfoPopup.name);
	}
}).fail(function(jqxhr, settings, exception) {
	console.log(arguments);
});
</script>
</body>
</html>
