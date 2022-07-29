<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<head>
	<title>공지 사항</title>
	<!-- CSS ================================================== -->
	<c:import url="/include.commonHeader.sp" charEncoding="UTF-8" />
	
	<!-- JavaScript ================================================== -->
	<c:import url="/include.commonPlugin.sp" charEncoding="UTF-8" />
</head>
<script type="text/javascript">
$(function (){
	var data = {};
	data.type = "notice";
	window.addEventListener("message", receiveMessage, false);
	window.opener.postMessage(data, window.location.protocol+"//"+window.location.host+getContextPath());

	function receiveMessage(e)
	{	
		if (e.origin !== window.location.protocol+"//"+window.location.host) {
			return;
		}
		
		setView(e.data);
	}
	
	function setView(data) {
		var path = getContextPath() + '/noticeDownload?file_name=' + data.file_name + '&file_path='+data.file_path;
		path = encodeURI(path);
		$(".contentPop h6").html(data.subject);
		$(".contentPop a").html(data.file_name);
		$(".contentPop a").prop("href",path);
		$(".contentPop p").html(data.content);
	}
})
</script>
<body>
	<!-- 최초 width:350px height: 450px 사이즈 조정 가능함 -->
	<div class="contentPop bxNoticeDetailWrap">
		<h6></h6>
		<a></a>
		<p></p>
	</div>
</body>
</html>
