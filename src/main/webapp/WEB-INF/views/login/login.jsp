<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<!-- 시작: 로그인 -->
<!-- 보일때 display: flex -->
<div id="bxLogin" class="bxBPLoginWrap" style="display: flex">
	<div class="bxBPLogin">
		<h1>보안접속</h1>
		<div class="wall">
			<p>아이디</p>
			<input id="login_iptID" type="text">
			<p class="marginT10">비밀번호</p>
			<input id="login_iptPW" type="password">
			<input id="login_hiddenType" type="hidden">
			<button id="login_btnLogin" class="marginT15 btnBlue btnBig" >로그인</button>
		</div>
	</div>
</div>
<!-- 끝: 로그인 -->

<!-- 시작:  BP URL-->
<div id="login_url" class="modal fade in" style="display: none;">
	<div class="modal-dialog popW350" >
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">BP URL 선택하기</h4>
			</div>
			
			<div class="modal-body">
				<p>접속할 BP URL을 선택해 주세요.</p>
				<div class="modalFormWrap">
					<div id="login_divUrl" class="modalForm">
					</div>
				</div>
<!-- 				<a id="login_btnSubmit" class="btnBlue" >접속하기</a> -->
				<a id="login_btnSubmit_I" class="btnBlue marginT10" >접속하기</a>
				<!-- <a id="login_btnSubmit_O" class="btnBlue marginT10" >아웃바운드로 접속하기</a> -->
			</div>
		</div>
	</div>
</div>
<!-- 끝: BP URL -->


<!-- 시작:  비밀번호 변경(최초접속/비번 초기화)-->
<div id="login_div1" class="modal fade in" style="display: none;">
	<div class="modal-dialog popW350" >
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">비밀번호 변경</h4>
			</div>
			
			<div class="modal-body">
				<p>보안을 위해 새로운 비밀번호로 설정해 주세요.</p>
				<div class="modalFormWrap">
					<div class="modalForm">
						<p>아이디</p>
						<input id="login_div1_iptID" type="text" disabled="disabled">
					</div>
					<div class="modalForm">
						<p>기존 비밀번호</p>
						<input id="login_div1_iptOldPW" type="password">
					</div>
					<div class="modalForm">
						<p>신규 비밀번호</p>
						<input id="login_div1_iptNewPW" type="password">
						<p class="note">*영문자와 숫자를 조합하여 10자리 이상으로 설정해 주세요.<br>*아이디, 기존 비밀번호와 같게 설정할 수 없습니다.</p>
					</div>
					<div class="modalForm">
						<p>신규 비밀번호 확인</p>
						<input id="login_div1_iptNewPWConfirm" type="password">
					</div>
				</div>
				<a class="btnBlue" id="login_div1_btnSave">비밀번호 변경하기</a>
			</div>
		</div>
	</div>
</div>
<!-- 끝: 비밀번호 변경 -->

<!-- 시작:  비밀번호 변경(30일 주기)-->
<div id="login_div2" class="modal fade in" style="display: none;">
	<div class="modal-dialog popW350" >
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">비밀번호 변경</h4>
			</div>
			
			<div class="modal-body">
				<p>비밀번호를 변경한지 30일이 지났습니다.<br>보안을 위해 새로운 비밀번호로 설정해 주세요.</p>
				<div class="modalFormWrap">
					<div class="modalForm">
						<p>아이디</p>
						<input id="login_div2_iptID" type="text" disabled="disabled">
					</div>
					<div class="modalForm">
						<p>기존 비밀번호</p>
						<input id="login_div2_iptOldPW" type="password">
					</div>
					<div class="modalForm">
						<p>신규 비밀번호</p>
						<input id="login_div2_iptNewPW" type="password">
						<p class="note">*영문자와 숫자를 조합하여 10자리 이상으로 설정해 주세요.<br>*아이디, 기존 비밀번호와 같게 설정할 수 없습니다.</p>
					</div>
					<div class="modalForm">
						<p>신규 비밀번호 확인</p>
						<input id="login_div2_iptNewPWConfirm" type="password">
					</div>
				</div>
				<a class="btnBlue" id="login_div2_btnSave">비밀번호 변경하기</a>
			</div>
		</div>
	</div>
</div>
<!-- 끝: 비밀번호 변경 -->

<!-- 시작:  잠금 해제하기-->
<div id="login_div3" class="modal fade in" style="display: none;">
	<div class="modal-dialog popW350" >
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">잠금 해제하기</h4>
			</div>
			
			<div class="modal-body">
				<p>일정기간 동안 접속이 없어 잠긴 계정입니다.<br>해제를 위해 비밀번호를 재설정해 주세요.</p>
				<div class="modalFormWrap">
					<div class="modalForm">
						<p>아이디</p>
						<input id="login_div3_iptID" type="text" disabled="disabled">
					</div>
					<div class="modalForm">
						<p>기존 비밀번호</p>
						<input id="login_div3_iptOldPW" type="password">
					</div>
					<div class="modalForm">
						<p>신규 비밀번호</p>
						<input id="login_div3_iptNewPW" type="password">
						<p class="note">*영문자와 숫자를 조합하여 10자리 이상으로 설정해 주세요.<br>*아이디, 기존 비밀번호와 같게 설정할 수 없습니다.</p>
					</div>
					<div class="modalForm">
						<p>신규 비밀번호 확인</p>
						<input id="login_div3_iptNewPWConfirm" type="password">
					</div>
				</div>
				<a class="btnBlue" id="login_div3_btnSave">잠금 해제하기</a>
			</div>
		</div>
	</div>
</div>
<!-- 끝: 비밀번호 변경 -->


<!-- View Controller -->
<script type="text/javascript">
$.getScript(getContextPath() + "/resources/service/js/login.js").done(function(script, textStatus) {
	if (!!ecoletree.vc && ecoletree.vc.name === "login") {
		ecoletree.vc.init( ${initData} );
	} else {
		console.log("vc's name is not login : " + ecoletree.login.name);
	}
}).fail(function(jqxhr, settings, exception) {
	console.log(arguments);
});
</script>
