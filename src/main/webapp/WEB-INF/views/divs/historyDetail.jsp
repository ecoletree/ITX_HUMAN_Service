<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 시작: 이력 상세 -->
<div id="uiTest1" class="bxShowHistoryDetail">
	<button class="btnShowHistoryDetail" onclick="{uiTest1.style.display='none';uiTest2.style.display='flex';}">이전 상담 이력 창열기</button>
</div>

<div id="uiTest2" class="bxBpHistoryDetail" style="display: none;">
	<div class="bxBpHistoryUserInfo">
		<div class="wallHead">
			<div><h6>이전 상담 이력</h6></div>
		</div>
		<div class="wallBody">
			<div class="flexForm">
				<div class="flexFormItem">
					<p>고객 아이디</p>
					<input id="historyDetail_iptCust_id" type="text" disabled="disabled">
				</div>
			</div>
			<div class="flexForm">
				<div class="flexFormItem">
					<p>고객명</p>
					<input id="historyDetail_iptCust_nm" type="text" disabled="disabled">
				</div>
			</div>
			<div class="flexForm">
				<div class="flexFormItem">
					<p>인입 전화번호</p>
					<input id="historyDetail_iptHand_tel" type="text" disabled="disabled">
					<button class="btnDial"><img src="${cp }/resources/ecoletree/img/btn_dial.png"></button>
				</div>
			</div>
		</div><!-- ./wallBody -->
	</div>
	<div class="bxBpHistoryMemoInfo">
		<div class="wallHead">
			<div>
				<div id="historyDetail_divBtn1" style="display: block">
					<a id="historyDetail_btnShowDetail" class="btnGray btnSmall">상세보기</a>
					<a id="historyDetail_btnUpdate" class="btnBlue btnSmall">수정하기</a>
				</div>
				<div id="historyDetail_divBtn2" style="display: none">
					<a id="historyDetail_btnCancel" class="btnGray btnSmall">취소</a>
					<a id="historyDetail_btnSave" class="btnBlue btnSmall">저장하기</a>
				</div>
			</div>
		</div>
		<div class="wallBody">
			<div class="row bxMH112">
				<div class="col-xs-3">
					<p>상담유형(대)</p>
					<select id="historyDetail_selCrCd1" disabled="disabled"><option>선택해주세요</option></select>
				</div>
				<div class="col-xs-3">
					<p>상담유형(중)</p>
					<select id="historyDetail_selCrCd2" disabled="disabled"><option>선택해주세요</option></select>
				</div>
				<div class="col-xs-3">
					<p>상담유형(소)</p>
					<select id="historyDetail_selCrCd3" disabled="disabled"><option>선택해주세요</option></select>
				</div>
<!-- 				<div class="col-xs-3"> -->
<!-- 					<p>상담유형(상세)</p> -->
<!-- 					<select id="historyDetail_selCrCd4" disabled="disabled"><option>선택해주세요</option></select> -->
<!-- 				</div> -->
				<div class="col-xs-3">
					<p>통화결과</p>
					<select id="historyDetail_selExec_state" disabled="disabled"><option>선택해주세요</option></select>
				</div>
			</div>
			<div class="bxMemo">
				<span>상담메모 |</span>
				<textarea id="historyDetail_taCr_mm" rows="" cols="" disabled="disabled"></textarea>
				<textarea id="historyDetail_taCr_mm_add" rows="" cols="" disabled="disabled"></textarea>
			</div>
			
		</div><!-- ./wallBody -->
	</div>
	<button class="btnHideHistoryDetail" onclick="{uiTest1.style.display='block';uiTest2.style.display='none';}">이전 상담이력 창닫기</button>
</div><!-- 끝: 이력 상세 -->

<script type="text/javascript">
$.getScript(getContextPath() + "/resources/service/js/divs/historyDetail.js").done(function(script, textStatus) {
	if (!!ecoletree.historyDetail && ecoletree.historyDetail.name === "historyDetail") {
		ecoletree.historyDetail.init( ${initData} );
	} else {
		console.log("vc's name is not home : " + ecoletree.historyDetail.name);
	}
}).fail(function(jqxhr, settings, exception) {
	console.log(arguments);
});
</script>