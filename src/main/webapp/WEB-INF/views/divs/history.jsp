<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 시작: 이력 테이블 -->
<div id="divHistory" class="bxBpHistory">
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
					<option value="search_tmr_nm">상담원명</option>
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
					<option value="search_tmr_nm">상담원명</option>
					<option value="team_cd">팀 명</option>
					<option value="search_hand_tel">전화번호</option>
					<option value="cr_mm">메모</option>
				</select>
				<input type="text" id="history_sel2_search" readonly="readonly">
				<select id="history_sel2_team" style="display : none"></select>
				<select id="history_sel2_remark" style="display : none"></select>
			</div>
		</div>
		<!-- 
		<div class="bxSearchForm">
			<p>통화결과</p>
			<div>
				<select id="history_sel3">
					<option value="">선택해주세요</option>
				</select>
			</div>
		</div>
		<div class="bxSearchForm">
			<p>상담유형(대)</p>
			<div>
				<select id="history_sel4">
					<option value="">선택해주세요</option>
				</select>
			</div>
		</div>
		<div class="bxSearchForm">
			<p>상담유형(중)</p>
			<div>
				<select id="history_sel5" disabled="disabled" >
					<option value="" >선택해주세요</option>
				</select>
			</div>
		</div>
		<div class="bxSearchForm">
			<p>상담유형(소)</p>
			<div>
				<select id="history_sel6" disabled="disabled" >
					<option value="" >선택해주세요</option>
				</select>
			</div>
		</div>
		<div class="bxSearchForm">
			<p>상담유형(상세)</p>
			<div>
				<select id="history_sel7" disabled="disabled" >
					<option value="" >선택해주세요</option>
				</select>
			</div>
		</div>
		 -->
		<div class="bxSearchForm">
			<button class="btnBlue btnSearch" id="history_btnSearch"><img src="${cp }/resources/ecoletree/img/btn_searchW.png" /> 조회</button>
		</div>
		<div class="bxSearchForm">
			<button class="btnBlue btnSearch" id="history_btnDownLoad" disabled="disabled"><img src="${cp }/resources/ecoletree/img/btn_excelW.png" /> 다운</button>
			<button class="btnBlue btnSearch" id="history_btnHistoryTable">히스토리 상세</button>
			<button class="btnGray btnSearch" id="history_btnSaveInfo">미저장 등록</button>
		</div>
	</div>
	<div class="historyTableWrap">
		<div class="dataTables_wrapper form-inline">
			<table id="history_tbList" class="ecloeTable ecloeTableFixed table table-hover dataTable">
				<thead>
					<tr>
						<th style="width: 50px; min-width:50px;"><div>순번</div></th>
						<th style="width: 135px; min-width:135px;"><div>통화일자</div></th>
						<th style="width: 70px; min-width:70px;"><div>팀명</div></th>
						<th style="width: 80px; min-width:80px;"><div>상담원명</div></th>
						<th style="width: 80px; min-width:80px;"><div>고객명</div></th>
						<th style="width: 140px; min-width:140px;"><div>상담유형(대)</div></th>
						<th style="width: 140px; min-width:140px;"><div>상담유형(중)</div></th>
						<th style="width: 140px; min-width:140px;"><div>상담유형(소)</div></th>
<!-- 						<th style="width: 140px; min-width:140px;"><div>상담유형(상세)</div></th> -->
						<th style="width: 150px; min-width:150px;"><div>상담메모</div></th>
						<th style="width: 100px; min-width:100px;"><div>통화결과</div></th>
 						<th style="width: 100px; min-width:100px;"><div>콜시작시간</div></th>
 						<th style="width: 100px; min-width:100px;"><div>콜저장시간</div></th>
 						<th style="width: 100px; min-width:100px;"><div>통화시간</div></th>
						<th style="width: 110px; min-width:110px;"><div>전화번호</div></th>
						<th style="width: 200px; min-width:200px;"><div>고객아이디</div></th>
						<th style="width: 100px; min-width:100px;"><div>인아웃구분</div></th>
						<th style="width: 50px; min-width:50px;"><div>듣기</div></th>
					</tr>
				</thead>
				<tbody class="cursorPoint">
				</tbody>
			</table>
		</div>
			
	</div>
	
	
	<button id="btnShowBig" class="btnHideHistory" onclick="{btnShowSmall.style.display='block';btnShowBig.style.display='none';divCallInfo.style.display='none';divHistory.style.marginTop='10px';}">히스토리 크게보기</button>
	<button id="btnShowSmall" class="btnHideHistory" style="display: none;" onclick="{btnShowSmall.style.display='none';btnShowBig.style.display='block';divCallInfo.style.display='flex';divHistory.style.marginTop='0px';}">히스토리 작게보기</button>
	
</div><!-- 끝: 이력 테이블 -->

	<!-- 작업중 -->
<div class="bxProcess" style="display: none;">
	<div>
		<img src="${cp }/resources/ecoletree/svg/SwingPreloader.svg" alt="loading">
		<h6>작업중입니다.</h6>
		<p>작업이 완료될 때까지 잠시만 기다려주세요.</p>
	</div>
</div>


<script type="text/javascript">
$.getScript(getContextPath() + "/resources/service/js/divs/history.js").done(function(script, textStatus) {
	if (!!ecoletree.history && ecoletree.history.name === "history") {
		ecoletree.history.init( ${initData}, '${sessionScope.sessionVO.tmr_info.team_cd}' );
	} else {
		console.log("vc's name is not home : " + ecoletree.history.name);
	}
}).fail(function(jqxhr, settings, exception) {
	console.log(arguments);
});
</script>