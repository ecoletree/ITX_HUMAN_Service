<!--
 *****************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * Author : 
 * Create Date : 2017. 11. 02
 * DESC : /src/main/webapp/WEB-INF/views/admin/adminMain.jsp
 *****************************************************************
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<head>
	<title>ITX_HUMAN</title>
	<!-- CSS ================================================== -->
	<c:import url="/include.commonHeader.sp" charEncoding="UTF-8" />
	
	<!-- JavaScript ================================================== -->
	<c:import url="/include.commonPlugin.sp" charEncoding="UTF-8" />
</head>
<body>
	<div id="wrapper">
		<div id="content">
			<tiles:insertAttribute name="loginBody" />
		</div>
		<!-- .content -->
		
	</div>
	<!-- /#wrapper -->
	
	<!-- alert 모달 -->
	<c:import url="/common.alert.sp" charEncoding="UTF-8" />
</body>
</html>