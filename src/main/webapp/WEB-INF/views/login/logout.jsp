<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
	<!-- 보일때 display: flex -->
	<div id="bxLogout" class="bxBPLogoutWrap" style="display: flex">
		<div class="bxBPLogout">
			<img src="${cp }/resources/ecoletree/img/icon_lock.png" />
			<h6>보안상의 이유로 로그아웃되었습니다.</h6>
			<h6>다시 로그인해 주세요.</h6>
			<button class="marginT15 btnBlue btnBig"  onclick="location.href='${cp }/login'">로그인 화면으로 이동</button>
		</div>
	</div>
	<!-- 끝: 로그아웃 -->
	
</div>
