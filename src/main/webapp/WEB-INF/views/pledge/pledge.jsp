<div class="bxBpPledgeInfoWrap" >
	
	<div id="pledge_content" style="display: none;">
		<div class="bxBpPledgeInfo">
			<div class="wallHead">
				<div><h6>아래 내용을 직접 작성해주서야 서명처리됩니다.</h6></div>
				<div></div>
			</div>
			<div class="wallBody">
				<div class="bxPledgeInline">
					<span>나</span>
					<div id="pledge_div1" class="bxPledge">
						<p id="pledge_showName1">${sessionScope.sessionVO.tmr_nm}</p>	
						<input id="pledge_enterName1" type="text" class="inputPledge">
						<span id="pledge_error1" style="display: none;">└내용을 확인해주세요</span>
					</div>
					<span>은/는</span>
				</div>
				
				<p class="pPledge marginT10">
독립적인 근무환경에서 일하는 만큼
<br>업무와 관련한 모든 정보가 회사와 고객의 중요한 자산임을 인식하고,
<br> 
책임감을 갖고 업무에 임하겠습니다.
<br><br>
업무와 관련한 모든 사항에 대해서는
<br>
회사의 승인 없이 외부에 노출되지 않도록 철저히 주의하겠습니다.
<br><br><br>
<strong>업무와 관련한 기밀정보에 대한 유지를 위해 아래 사항을 철저히 준수하겠습니다.</strong>
</p>
<ul class="ulPledge">
	<li>업무와 무관한 제3자에게는 업무와 관련된  모든 정보를 절대로 공유하지 않겠습니다.</li>
	<li>최초 설치된 근무환경을 잘 관리하고, 업무 이외의 목적으로 회사의 허가 없이<br> 함부로 이용하지 않겠습니다.</li>
</ul>
				
				
				<div class="bxPledgeInline marginT10 txt24">
					<strong><span>상담전문가</span></strong>
					<div id="pledge_div2" class="bxPledge">
						<p id="pledge_showName2">${sessionScope.sessionVO.tmr_nm}</p>
						<input id="pledge_enterName2" type="text" class="inputPledge">
						<span id="pledge_error2" style="display: none;">└내용을 확인해주세요</span>
					</div>
					<strong><span id="pledge_date"></span></strong>
				</div>
				

				<a id="pledge_btnComplete" class="btnBlue" >작성 및 서명완료</a>
			</div><!-- ./wallBody -->
		</div><!-- ./bxBpCallUserInfo -->
	</div>
	<!-- .content -->
	
	<!-- 안내문 -->
	<div id="pledge_notice" class="modal fade in" style="display: block;">
			<div class="modal-dialog popW650" >
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title">안내</h4>
					</div>
					
					<div class="modal-body">
						<p>회사는 우리 직원들의 일과 삶의 균형을 위해 항상노력하고 있습니다.<br>도움이 필요할때는 언제든 회사와 상의해주세요.</p>
						<div class="modalBtnSet">
							<a id="pledge_btnAgree" class="btnBlue">확인</a>
						</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- /#wrapper -->

<!-- View Controller -->
<script type="text/javascript">
$.getScript(getContextPath() + "/resources/service/js/pledge.js").done(function(script, textStatus) {
	if (!!ecoletree.vc && ecoletree.vc.name === "pledge") {
		ecoletree.vc.init( ${initData} );
	} else {
		console.log("vc's name is not home : " + ecoletree.pledge.name);
	}
}).fail(function(jqxhr, settings, exception) {
	console.log(arguments);
});
</script>