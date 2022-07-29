<div class="popupHead">
	<div><img src="${cp }/resources/ecoletree/img/icon_dial.png" /> 아웃바운드</div>
	<a id="outbound_btnCloseModal" >x</a>
</div>
<div class="popupBody">
	<div class="bxBpHistory">
		<div class="wallHead">
			<div class="bxSearchForm">
				<div class="bxSearchFormRow">
					<div class="bxSearchForm">
						<p>DB상태</p>
						<div>
							<select id="outbound_selState1">
								<option value="">선택해주세요</option>
							</select>
							<select id="outbound_selState2">
								<option value="">선택해주세요</option>
							</select>
						</div>
					</div>
					<div class="bxSearchForm">
						<p>분배일자</p>
						<div>
							<div class="bxEcoleDate">
								<input id="outbound_dfShareFrom" type="text" placeholder="" data-dateformat="yyyy.mm.dd" readyonly="readonly" name="sdate" >
								<span id="outbound_btnShareFrom" class="input-group-addon"><img src="${cp }/resources/ecoletree/img/btn_cal.png" /></span>
							</div>
							<span> ~ </span>
							<div class="bxEcoleDate">
								<input id="outbound_dfShareTo" type="text" placeholder="" data-dateformat="yyyy.mm.dd" readyonly="readonly" name="edate" >
								<span id="outbound_btnShareTo" class="input-group-addon"><img src="${cp }/resources/ecoletree/img/btn_cal.png" /></span>
							</div>
						</div>
					</div>
					<div class="bxSearchForm">
						<p>캠페인</p>
						<div>
							<select id="outbound_selCampaign">
							</select>
						</div>
					</div>
					<div class="bxSearchForm">
						<p>쏘팅</p>
						<div>
							<select id="outbound_selSorting" name="order_type">
								<option value="share_desc">분배일자 최신순</option>
								<option value="share_asc">분배일자 오래된순</option>
								<option value="champnm_asc">캠페인명 오름차순</option>
								<option value="champnm_desc">캠페인명 내림차순</option>
								<option value="custnm_asc">고객명 오름차순</option>
								<option value="custnm_desc">고객명 내림차순</option>
								<option value="contact_desc">최종접촉 최신순</option>
								<option value="contact_asc">최종접촉 오래된순</option>
							</select>
						</div>
					</div>
				</div>
				<div id="outbound_divSearchCondition" class="bxSearchFormRow">
				</div>
			</div>
			<div class="bxSearchForm">
				<a class="btnBlue btnSearch" id="outbound_btnSearch"><img src="${cp }/resources/ecoletree/img/btn_searchW.png" /> 조회</a>
			</div>
		</div><!-- ./wallHead -->
		<div class="historyTableWrap">
			<div class="dataTables_wrapper form-inline dt-bootstrap">
				<!-- 테이블 10줄 한페이지로 // 페이징 버튼은 개발 후 css작업 예정 -->
				<table id="outbound_tbList" class="ecloeTable ecloeTableSmall table table-hover ui-datatable dataTable dataTable-helper" style="width: 100%">
					<thead>
						<tr>
							<th style="width:40px; min-width:45px;">순번</th>
							<th style="width:110px; min-width:110px;" >분배일자</th>
							<th style="width:80px; min-width:80px;" >캠페인명</th>
							<th style="width:80px; min-width:80px;" >고객명</th>
							<th style="width:50px; min-width:50px;" >상태</th>
							<th style="width:150px; min-width:150px;" >최종접속</th>
							<th style="width:40px; min-width:45px;" ></th>
						</tr>
					</thead>
					<tbody  class="cursorPoint">
	<!-- 					<tr> -->
	<!-- 						<td class="txtCenter">1</td> -->
	<!-- 						<td class="txtCenter">2020.10.10</td> -->
	<!-- 						<td>캠페인명자리입니다</td> -->
	<!-- 						<td>홍*동</td> -->
	<!-- 						<td>상태값보임</td> -->
	<!-- 						<td class="txtCenter">2020.10.10 05:00</td> -->
	<%-- 						<td class="txtCenter"><a class="btnBlue btnSmall"><img src="${cp }/resources/ecoletree/img/icon_dial.png" /></a></td> --%>
	<!-- 					</tr> -->
					</tbody>
				</table>
			</div>	
				
		</div><!-- ./historyTableWrap -->
	
	</div>
</div><!-- ./popupBody -->

<!-- View Controller -->
<script type="text/javascript">
$.getScript(getContextPath() + "/resources/service/js/pop/outboundPopup.js").done(function(script, textStatus) {
	if (!!ecoletree.outbound && ecoletree.outbound.name === "outbound") {
		ecoletree.outbound.init( ${initData} );
	} else {
		console.log("vc's name is not home : " + ecoletree.outbound.name);
	}
}).fail(function(jqxhr, settings, exception) {
	console.log(arguments);
});

</script>