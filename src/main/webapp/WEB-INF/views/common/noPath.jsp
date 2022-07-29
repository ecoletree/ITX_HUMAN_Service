<!-- 
404ERROR
/*****************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * Author : Hyungseok Kim
 * Create Date : 2018. 09. 13.
 * DESC : [관리자] 404 에러 화면
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
			url = '${cp }';
		} else {
			url = document.referrer;
		}
		
		location.href = url;
	}
</script>
<body>
	<div class="bxBPMsgWrap" style="display: flex">
		<div class="bxBPMsg">
			<img src="${cp }/resources/ecoletree/img/icon_etc.png" >
		<h1 >404 ERROR</h1>
		<h6>요청하신 페이지를 찾을 수 없습니다.</h6>
		<p>찾으시려는 페이지의 주소 입력을 잘못하셨거나<br/>
페이지 주소의 삭제나 변경 등의 이유로 찾을 수 없습니다.<br/>
입력하신 주소와 경로를 다시 한번 확인 후 이용하시기 바랍니다.<br/><br/>
잘못된 주소가 아님에도 지속적으로 이 메시지가 보이신다면<br/>
관리자에게 문의 부탁드립니다.</p>
			<button class="marginT15 btnBlue btnBig" onclick="javascript:goMain();">메인으로 이동</button>
		</div>
	</div>
	

</body>
</html>