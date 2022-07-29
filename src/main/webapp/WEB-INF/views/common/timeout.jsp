<!-- 
SESSIONERROR
/*****************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * Author : Hyungseok Kim
 * Create Date : 2018. 09. 13.
 * DESC : [관리자] 500 에러 화면
*****************************************************************/
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<% String cp = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<c:import url="/include.commonHeader.sp" charEncoding="UTF-8" />

<link rel="stylesheet" href="${cp }/resources/mvpReady/templates/landing/css/mvpready-landing.css">
<!-- JavaScript ================================================== -->
<c:import url="/include.commonPlugin.sp" charEncoding="UTF-8" />

<!-- App JS -->
<script src="${cp }/resources/mvpReady/templates/landing/js/mvpready-landing.js"></script>

<title>ITX_HUMAN</title>
</head>

<script type="text/javascript">
	function goMain() {
		var url = "";
		if (document.referrer === "") {
			url = '${cp }/login';
			location.href = url;
		} else {
			if (-1 < document.referrer.indexOf(".sp") && -1 < document.referrer.indexOf("pop.")) {
				alert("팝업창에서는 팝업창이 닫힙니다");
				self.close();
			} else {
				url = '${cp }/login';
				location.href = url;
			}
			
		}
		
		
	}
</script>


<body>
	<div id="bxLogout" class="bxBPLogoutWrap" style="display: flex">
		<div class="bxBPLogout">
			<img src="${cp }/resources/ecoletree/img/icon_lock.png" />
			<h6>보안상의 이유로 로그아웃 되었습니다.</h6>
			<h6>다시 로그인을 해주세요.</h6>
			<button class="marginT15 btnBlue btnBig"  onclick="javascript:goMain();">로그인 화면으로 이동</button>
		</div>
	</div>
</body>
</html>