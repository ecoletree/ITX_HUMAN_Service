<!-- 
/*****************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * Author :  
 * Create Date : 2018. 06. 12.
 * DESC : [관리자웹] 헤더 
*****************************************************************/
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="container">
	<div class="navbar-header">
		<button class="navbar-toggle" type="button" data-toggle="collapse" data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span>
			<i class="fa fa-cog"></i>
		</button>

		<a href="${cp }/admin/user/openMemberMgtList" class="navbar-brand navbar-brand-img">
			<img src="${cp }/resources/ecoletree/img/logo.png" alt="애자일소다 기술지원">
		</a>
	</div>
	<!-- /.navbar-header -->
	
	<div class="menu">
		<div>
			<a href="${cp }/admin/user/openMemberMgtList" class="btnMenu">
				회원관리
			</a>
			<a href="${cp }/admin/notice/openNoticeList" class="btnMenu">
				게시물관리
			</a>
			<a href="${cp }/admin/qna/openQnaList" class="btnMenu">
				Q&amp;A
			</a>
			<c:if test="${sessionScope.adminSessionVO.is_super_admin == 'Y'}">
			<a href="${cp }/admin/category/openCategoryList" class="btnMenu">
				카테고리관리
			</a>
			</c:if>
			<div class='pull-right'>
				<a href="http://www.agilesoda.market" target="_blank">
					<i class="fa fa-share"></i> 홈페이지로
				</a>
				<a data-toggle="modal" href="#popMyInfo">
					<i class="fa fa-user"></i> 내정보수정
				</a>
				<a href="${cp }/admin/logout">
					<i class="fa fa-sign-out"></i> 로그아웃
				</a>
			</div>
		</div>
	</div>

</div>
<!-- /.container -->