<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Core JS -->
<script src="${cp }/resources/mvpReady/bower_components/jquery/dist/jquery.js"></script>
<script src="${cp }/resources/mvpReady/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="${cp }/resources/mvpReady/bower_components/slimscroll/jquery.slimscroll.min.js"></script>
<script src="${cp }/resources/ecoletree/js/plugin/lodash/lodash.min.js"></script>

<!-- Plugin JS : bootstrap datepicker -->
<script src="${cp }/resources/mvpReady/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${cp }/resources/mvpReady/bower_components/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<!-- <script src="${cp }/resources/mvpReady/bower_components/bootstrap-datepicker/js/locales/bootstrap-datepicker.kr.js"></script> -->

<!-- Plugin JS : dataTables -->
<script src="${cp }/resources/mvpReady/bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${cp }/resources/mvpReady/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.js"></script>
<%-- <script src="${cp }/resources/mvpReady/bower_components/datatables-helper/js/datatables-helper.js"></script> --%>

<!-- Plugin JS : jQuery validation -->
<script src="${cp }/resources/ecoletree/js/plugin/jquery-validation-1.15.0/dist/jquery.validate.js"></script>
<script src="${cp }/resources/ecoletree/js/plugin/jquery-validation-1.15.0/dist/additional-methods.js"></script>
<script src="${cp }/resources/ecoletree/js/plugin/jquery-validation-1.15.0/dist/localization/messages_ko.min.js"></script>

<!-- Plugin JS : jQuery Base64 -->
<script src="${cp }/resources/ecoletree/js/plugin/jquery-base64/jquery.base64.min.js"></script>

<!-- Plugin JS : Multi Language -->
<script src="${cp }/resources/ecoletree/js/plugin/jquery-i18n/jquery.i18n.properties-1.0.9.js"></script>

<!-- Plugin JS : fileInut -->
<script src="${cp }/resources/mvpReady/bower_components/bootstrap-jasny/js/fileinput.js"></script>

<!-- App JS -->
<script src="${cp }/resources/mvpReady/global/js/mvpready-core.js"></script>
<script src="${cp }/resources/mvpReady/global/js/mvpready-helpers.js"></script>

<!-- Plugin JS : magnific-popup-->
<script src="${cp }/resources/mvpReady/bower_components/magnific-popup/dist/jquery.magnific-popup.min.js"></script>

<!-- EcoleTree JS FILE -->
<script src="${cp }/resources/ecoletree/js/util/serviceUtil.js"></script>
<script src="${cp }/resources/ecoletree/js/util/validateUtil.js"></script>

<!-- App VIEW Controller -->
<script src="${cp }/resources/service/js/common/common.js?r=1"></script>
<script src="${cp }/resources/service/js/common/aesUtil.js"></script>

<script src="${cp }/resources/ecoletree/js/plugin/crypto-js/crypto-js.js"></script>

<script src="${cp }/resources/ecoletree/js/plugin/datepicker/bootstrap-datepicker.kr.js"></script>

<script src="${cp }/resources/ecoletree/js/plugin/jquery-ui-1.12.1/jquery-ui.min.js"></script>

<!-- Plugin JS : jquery-dateFormat-master -->
<script src="${cp }/resources/ecoletree/js/plugin/jquery-dateFormat-master/dateFormat.min.js"></script>
<script src="${cp }/resources/ecoletree/js/plugin/jquery-dateFormat-master/jquery-dateformat.min.js"></script>

<!-- clipboard -->
<script src="${cp }/resources/ecoletree/js/plugin/clipboard/clipboard.js"></script>

<!-- BP API -->
<!--  효성 리얼 서버 -->
<!--<script type="text/javascript" src="https://sp2.hsitx-ccc.com/agentdesktop/libs/servicepattern-sdk-v1.js"></script>-->
<!--  효성 테스트 서버 -->
<!-- <script type="text/javascript" src="https://hsitx-ccc.ga/agentdesktop/libs/servicepattern-sdk-v1.js"></script> -->

<!-- <script type="text/javascript" src="https://dev1.hsitx-ccc.com/agentdesktop/libs/servicepattern-sdk-v1.js"></script>  -->
<!--  이콜트리 테스트 서버 -->
<!-- <script type="text/javascript" src="https://hsitx-app.ga/agentdesktop/libs/servicepattern-sdk-v1.js"></script> -->

<c:if test="${sessionScope.sessionVO ne null}">
	<script type="text/javascript" src="${sessionScope.sessionVO.bp_url}/agentdesktop/libs/servicepattern-sdk-v1.js"></script>
</c:if>

<!-- Excel Download -->
<script src="${cp }/resources/ecoletree/js/plugin/sheetjs-master/xlsx.full.min.js"></script>

<script type="text/javascript">

	/**
	 * 컨텍스트 패스 획득
	 */
	function getContextPath() {
		var cp = '${cp}';
		if (cp == "" || cp == '') {
			cp = "/";
		}
		return '${cp}';
	};
	
</script>