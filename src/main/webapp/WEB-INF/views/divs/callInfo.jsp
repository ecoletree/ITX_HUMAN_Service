<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 시작: 인입정보 -->
<div id="divCallInfo" class="bxBpCallInfo">
	<div class="bxBpCallUserInfo">
		<div class="wallHead">
			<div><h6>고객정보</h6></div>
			<div>
<!-- 				<a id="callInfo_btnBlackList" class="btnBlue btnSmall">악성민원인 등록</a> -->
				<a id="callInfo_btnSaveCustInfo" class="btnBlue btnSmall">고객정보 저장</a>
			</div>
		</div>
		<div class="wallBody">
			<div class="flexForm">
				<div class="flexFormItem">
					<p>고객 아이디</p>
					<input id="callInfo_iptCust_id" type="text" disabled="disabled">
				</div>
			</div>
			<div class="flexForm">
				<div class="flexFormItem">
					<p>고객명</p>
					<input id="callInfo_iptCust_nm" type="text" maxlength="200">
				</div>
			</div>
			<div class="flexForm">
				<div class="flexFormItem">
					<p>인입 전화번호</p>
					<input id="callInfo_iptHand_tel" type="text" disabled="disabled">
				</div>
			</div>
		</div><!-- ./wallBody -->
		
		
		
		<!-- 악성 민원인 -->
		<!-- display: block; -->
		<div id="divRegiste" class="bxMainPopWrap4" style="display: none;">
			<div class="popupHead">
				<div>악성 민원인 등록</div>
				<a onclick="$('#divRegiste').hide()">x</a>
			</div>
			<div class="popupBody">
				<p class="marginB10">악성 민원인 정보를 입력해주세요.</p>
				<div class="flexForm">
					<div class="flexFormItem">
						<p>전화번호</p>
						<input id="callInfo_iptBlackListHandTel" type="text" maxlength="20">
					</div>
				</div>
				<div class="flexForm marginT10">
					<div class="flexFormItem">
						<p>유형</p>
						<div id="callInfo_selBlackCode">
							<label class="ecoleRadio"><input type="radio" name="callInfo_raBlack" checked="checked" value="001"><i></i>성희롱</label>
							<label class="ecoleRadio"><input type="radio" name="callInfo_raBlack" value="002"><i></i>욕설</label>
						</div>
					</div>

				</div>

				<a id="callInfo_btnBlackListSave" class="marginT10 btnBlue btnSmall btn-block text-center" >저장하기</a>
			</div><!-- ./popupBody -->
		</div>
		
		
	</div><!-- ./bxBpCallUserInfo -->
	
	<div class="bxBpCallMemoInfo">
		<div class="wallHead">
			<div><h6>상담저장</h6></div>
			<div>
				<label class="ecoleCheck"><input id="callInfo_cbCustInfoSave" type="checkbox" checked="checked" ><i></i>고객정보 저장</label>
				<a id="callInfo_btnClearCallInfo" class="btnGray btnSmall">초기화</a>
				<a id="callInfo_btnSaveCallInfo" class="btnBlue btnSmall">상담 저장</a>
			</div>
		</div>
		<div class="wallBody">
			<div class="bxBpCallMemoWrap">
				<div class="row">
					<div class="col-xs-10">
						<div class="flexForm">
							<div class="flexFormItem">
								<p>상담유형(대)</p>
								<div class="ListCodeWrap">
									<ul id="callInfo_ulCrCd1" class="listCode">
									</ul>
								</div>
							</div>
							<div class="flexFormItem">
								<p>상담유형(중)</p>
								<div class="ListCodeWrap">
									<ul id="callInfo_ulCrCd2" class="listCode">
									</ul>
								</div>
							</div>
							<div class="flexFormItem">
								<p>상담유형(소)</p>
								<div class="ListCodeWrap">
									<ul id="callInfo_ulCrCd3" class="listCode">
									</ul>
								</div>
							</div>
<!-- 							<div class="flexFormItem"> -->
<!-- 								<p>상담유형(상세)</p> -->
<!-- 								<div class="ListCodeWrap"> -->
<!-- 									<ul id="callInfo_ulCrCd4" class="listCode"> -->
<!-- 									</ul> -->
<!-- 								</div> -->
<!-- 							</div> -->
							
						</div>
					</div><!-- ./col-xs-9 -->
					
					<div id="callInfo_divRemarkList" class="col-xs-2 bxMH107">
						<p>통화결과</p>
						<div class="ListCodeWrap">
							<ul id="callInfo_selExec_state" class="listCode">
							</ul>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-6">
						<div class="bxMemo">
							<span>민원인 상담내용 |</span>
							<textarea id="callInfo_taCr_mm" rows="" cols=""></textarea>
						</div>
					</div>
					<div class="col-xs-6">
						<div class="bxMemo">
							<span>상담사 답변 |</span>
							<textarea id="callInfo_taCr_mm_add" rows="" cols=""></textarea>
						</div>
					</div>
				</div>
			</div>
		</div><!-- ./wallBody -->
	</div><!-- ./bxBpCallMemoInfo -->
</div><!-- 끝: 인입정보 -->

<!-- View Controller -->
<script type="text/javascript">
$.getScript(getContextPath() + "/resources/service/js/divs/callInfo.js").done(function(script, textStatus) {
	if (!!ecoletree.callInfo && ecoletree.callInfo.name === "callInfo") {
		ecoletree.callInfo.init( ${initData} );
	} else {
		console.log("vc's name is not home : " + ecoletree.callInfo.name);
	}
}).fail(function(jqxhr, settings, exception) {
	console.log(arguments);
});

</script>