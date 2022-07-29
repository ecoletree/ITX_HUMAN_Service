/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : 장윤석 
 * Create Date : 2019. 12. 30.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.service.home.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.ecoletree.common.ETCommonConst;
import kr.co.ecoletree.common.ETCommonConst.ETFileConst;
import kr.co.ecoletree.common.auth.Auth;
import kr.co.ecoletree.common.auth.ETSessionManager;
import kr.co.ecoletree.common.base.web.ETBaseController;
import kr.co.ecoletree.common.exception.ETException;
import kr.co.ecoletree.common.helper.SessionHelper;
import kr.co.ecoletree.common.util.FileUtil;
import kr.co.ecoletree.common.util.PropertyUtil;
import kr.co.ecoletree.common.util.ResultUtil;
import kr.co.ecoletree.common.vo.ETSessionVO;
import kr.co.ecoletree.service.common.ServiceCommonConst;
import kr.co.ecoletree.service.home.service.HomeService;
import kr.co.ecoletree.service.login.service.LoginService;
import net.sf.json.JSONObject;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/")
public class HomeController extends ETBaseController{
	
	@Autowired
	HomeService service;
	
	@Autowired
	LoginService loginService;
	
	@Value("${API_ACCOUNT}")
	private String api_account;

	@Value("${API_PATH}")
	private String api_path;
	
	@Value("${SCHEMA_EN_KEY}")
	private String schema_en_key;
	
	/**
	 * home 화면 처음 진입
	 * @throws IOException 
	 */
	@RequestMapping("")
	public ModelAndView home(Locale locale, Model model, HttpServletRequest request) {
		String url = null;
		ModelAndView mv = new ModelAndView();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (SessionHelper.isLoginUser()) {
			url = "home";
			
			ETSessionVO sessionVO = SessionHelper.getSessionVO();
			if (sessionVO.getBiz_type().equals(ServiceCommonConst.BIZ_TYPE_OUTBOUND)) {
				url = "home_outbound";
			}

			PropertyUtil propertyUtil;
	        String msg = null;
			try {
				propertyUtil = PropertyUtil.getInstance();
				msg = propertyUtil.getProperties("manager.tmr.id");
				if (-1 < msg.indexOf(SessionHelper.getTmrId())) {
					resultMap.put("manager_id", msg);
				}
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("c_tmrlist", msg.split(","));
				map.put("schema_en_key", schema_en_key);
//				List<Map<String, Object>> clist =  service.selectCTMRList(map);
				List<Map<String, Object>> clist =  null;
				resultMap.put("manager_list", clist);
				
				Map<String,Object> tmrInfo =  (Map<String,Object>)sessionVO.getTmr_info();
				resultMap.put("tmr_info", tmrInfo);
				
			} catch (IOException e) {
				logError(e.getMessage(),e);
			}
			
			
			List<Map<String, Object>> list = service.selectScriptList(null);
			resultMap.put("script_list", list);
			
			mv.addObject(ServiceCommonConst.INIT_DATA, JSONObject.fromObject(resultMap));
		} else {
			List<Map<String, Object>> paramList = new ArrayList<>();
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("code", ServiceCommonConst.CODE_VALUE_BPURL);
			paramList.add(paramMap);
			
			Map<String, Object> tempMap = new HashMap<>(); 
			tempMap.put("code_list", paramList);
			Map<String, Object> returnMap = loginService.getCodeListByCode(tempMap);
			mv.addObject(ServiceCommonConst.INIT_DATA, JSONObject.fromObject(returnMap));
			url = ".login.login";
		}
		mv.setViewName(url);
		return mv;
	}
	
	
	/**
	 * 서약서 화면 열기
	 * @return
	 */
	@RequestMapping("/pledge")
	public ModelAndView openPledge(HttpServletRequest request) {
		Map<String, Object> params = getParamToMap(request);
		
		ETSessionVO sessionVO = SessionHelper.getSessionVO();
		sessionVO.setBp_url((String) params.get("bp_url"));
		sessionVO.setBiz_type((String)params.get("biz_type"));
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName(".pledge.pledge" );
		mv.addObject(ServiceCommonConst.INIT_DATA, JSONObject.fromObject(SessionHelper.getSessionVO()));
		return mv;
	}
	
	/**
	 * 로그아웃 화면 열기
	 * @return
	 */
	@RequestMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ETException {
		ETSessionManager.getInstance().removeSession(SessionHelper.getTmrId());
		SessionHelper.logout(request);
		try {
			response.sendRedirect(servletContext.getContextPath() + "/openLogout");
		} catch (IOException e) {
			logError(e.getMessage());
			throw new ETException();
		}
	}
	
	@RequestMapping("/openLogout")
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName(".login.logout" );
		return mv;
	}
	
	@Auth
	@RequestMapping("/getCodeList")
	public @ResponseBody Map<String, Object> getCodeList(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		Map<String, Object> map = service.selectCodeList(null);
		return ResultUtil.getResultMap(true, map, ETCommonConst.SUCCESS);
	}
	
	/**
	 * 전화를 받았을때 고객 정보, 콜정보 등록
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/setCustCallInfo")
	public @ResponseBody Map<String, Object> setCustCallInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("tmr_id", SessionHelper.getTmrId());
		params.put("tmr_nm", SessionHelper.getSessionVO().getTmr_nm());
		params.put("schema_en_key", schema_en_key);
		Map<String, Object> map = service.setCustCallInfo(params);
		return ResultUtil.getResultMap(true, map, ETCommonConst.SUCCESS);
	}
	
	/**
	 * 콜백 신 공지사항 카운트 가져오기
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/getCallBackNoticeCount")
	public @ResponseBody Map<String, Object> getCallBackNoticeCount(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("tmr_id", SessionHelper.getTmrId());
		params.put("team_cd", ((Map<String,Object>)SessionHelper.getSessionVO().getTmr_info()).get("team_cd"));
		params.put("schema_en_key", schema_en_key);
		Map<String, Object> map = service.selectCallBackNoticeCount(params);
		return ResultUtil.getResultMap(true, map, ETCommonConst.SUCCESS);
	}
	
	/**
	 * 공지사항 리스트 가져오기
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/getNoticeList")
	public @ResponseBody Map<String, Object> getNoticeList(HttpServletRequest request) throws Exception {
		Map<String, Object> params = getParamToMap(request);
		params.put("tmr_id", SessionHelper.getTmrId());
		params.put("team_cd", ((Map<String,Object>)SessionHelper.getSessionVO().getTmr_info()).get("team_cd"));
		Map<String, Object> map = service.selectNoticeList(params);
		return map;
	}
	
	/**
	 * 콜백 리스트 가져오기
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/getCallBackList")
	public @ResponseBody Map<String, Object> getCallBackList(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("tmr_id", SessionHelper.getTmrId());
		params.put("schema_en_key", schema_en_key);
		Map<String, Object> map = service.selectCallBackList(params);
		return ResultUtil.getResultMap(true, map, ETCommonConst.SUCCESS);
	}
	
	/**
	 * 공지사항 읽기
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/setNoticeRead")
	public @ResponseBody Map<String, Object> setNoticeRead(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("tmr_id", SessionHelper.getTmrId());
		boolean isRead = service.insertNoticeRead(params);
		return ResultUtil.getResultMap(true, isRead, ETCommonConst.SUCCESS);
	}
	
	/**
	 * 공지사항 파일 다운로드
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/noticeDownload")
	public ModelAndView noticeDownload(HttpServletRequest request) throws Exception {
		String fileName = request.getParameter("file_name");
		String filePath = request.getParameter("file_path");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(ETFileConst.OrgName, fileName);
		map.put(ETFileConst.NewName,  filePath);
		map.put(ETFileConst.Path, FileUtil.getTopFolder(request));
		
		
		return new ModelAndView(ETFileConst.FiledownloadView, map);
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/sendMessge")
	public @ResponseBody Map<String, Object> sendMessge(@RequestBody Map<String, Object> param, HttpServletRequest request) {
		String returnMsg = ETCommonConst.SUCCESS;
		
		String input = null;
	    StringBuffer result = new StringBuffer();
	    URL url = null;

	    try {
	    	List<Map<String, Object>> toList = (List<Map<String, Object>>)param.get("toList");
	    	for(Map<String, Object> item : toList) {
	    		JSONObject obj = new JSONObject();
				obj.put("account", api_account);
				obj.put("type", (String)param.get("type"));
				obj.put("from", (String)param.get("from"));
				obj.put("content", (Map<String,Object>)param.get("content"));
				obj.put("refkey", (String)param.get("refkey"));
				obj.put("to", (String)item.get("value"));
		    
		    	/** Connection 설정 **/
		    	url = new URL(api_path);
		    	HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		    	connection.setRequestMethod("POST");
		    	connection.addRequestProperty("Content-Type", "application/json");
		    	connection.addRequestProperty("Accept-Charset", "UTF-8");
		    	connection.setDoInput(true);
		    	connection.setDoOutput(true);
		    	connection.setUseCaches(false);
		    	connection.setConnectTimeout(15000);
		       
				/** Request **/
				OutputStream os = connection.getOutputStream();
				os.write(obj.toString().getBytes("UTF-8"));
				os.flush();
		    
				/** Response **/
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				while ((input = in.readLine()) != null) {
					result.append(input);
				}
		    
				connection.disconnect();
	    	}
     	} catch (MalformedURLException e) {
     		e.printStackTrace();
     		logError(e.getMessage());
     		returnMsg = ETCommonConst.FAILED;
     	} catch (ProtocolException e) {
     		logError(e.getMessage());
     		e.printStackTrace();
     		returnMsg = ETCommonConst.FAILED;
     	} catch (IOException e) {
     		logError(e.getMessage());
     		e.printStackTrace();
     		returnMsg = ETCommonConst.FAILED;
     	}
	    
	    if (!result.toString().equals("")) {
	    	returnMsg = result.toString();
	    }
	    
		return ResultUtil.getResultMap(true, param, returnMsg);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/sendMessgeTest")
	public @ResponseBody Map<String, Object> sendMessgeTest(@RequestBody Map<String, Object> param, HttpServletRequest request) {
		String returnMsg = ETCommonConst.SUCCESS;
		
		String input = null;
		StringBuffer result = new StringBuffer();
		URL url = null;
		
		try {
			List<Map<String, Object>> toList = (List<Map<String, Object>>)param.get("toList");
			for(Map<String, Object> item : toList) {
				JSONObject obj = new JSONObject();
				obj.put("account", api_account);
				obj.put("type", (String)param.get("type"));
				obj.put("from", (String)param.get("from"));
				obj.put("content", (Map<String,Object>)param.get("content"));
				obj.put("refkey", (String)param.get("refkey"));
				obj.put("to", (String)item.get("value"));
				
				System.out.println(obj.toString());
				
				/** SSL 인증서 무시 : 비즈뿌리오 API 운영을 접속하는 경우 해당 코드 필요 없음 **/
				TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() { return null; }
					public void checkClientTrusted(X509Certificate[] chain, String authType) { }
					public void checkServerTrusted(X509Certificate[] chain, String authType) { } } };
				
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				
				/** Connection 설정 **/
				/** 운영 : https://api.bizppurio.com, 개발 : https://dev-api.bizppurio.com:10443 **/
				url = new URL(api_path);
				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.addRequestProperty("Content-Type", "application/json");
				connection.addRequestProperty("Accept-Charset", "UTF-8");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setConnectTimeout(15000);
				
				/** Request **/
				OutputStream os = connection.getOutputStream();
				os.write(obj.toString().getBytes("UTF-8"));
				os.flush();
				
				/** Response **/
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				while ((input = in.readLine()) != null) {
					result.append(input);
				}
				
				connection.disconnect();
				System.out.println("Response : " + result.toString());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logError(e.getMessage());
			returnMsg = ETCommonConst.FAILED;
		} catch (ProtocolException e) {
			logError(e.getMessage());
			e.printStackTrace();
			returnMsg = ETCommonConst.FAILED;
		} catch (IOException e) {
			logError(e.getMessage());
			e.printStackTrace();
			returnMsg = ETCommonConst.FAILED;
		} catch (NoSuchAlgorithmException e) {
			logError(e.getMessage());
			logError(e.getMessage());
			e.printStackTrace();
			returnMsg = ETCommonConst.FAILED;
		} catch (KeyManagementException e) {
			logError(e.getMessage());
			e.printStackTrace();
			returnMsg = ETCommonConst.FAILED;
		}
		
		returnMsg = result.toString();
		
		return ResultUtil.getResultMap(true, param, returnMsg);
	}
	
	/**
	 * DataBase Date 를 가져온다
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/getDBDate")
	public @ResponseBody Map<String, Object> getDBDate(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		return ResultUtil.getResultMap(true, service.selectDBDate(), ETCommonConst.SUCCESS);
	}
	
	/**
	 * 사용자 상태가 변경되면 상태를 저장한다
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/setTMRStatus")
	public @ResponseBody Map<String, Object> setTMRStatus(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("tmr_id", SessionHelper.getTmrId());
		boolean b = service.setTMRStatus(params);
		return ResultUtil.getResultMap(b, b, b ? ETCommonConst.SUCCESS: ETCommonConst.FAILED);
	}
	
	/**
	 * 자신이 속한 캠페인의 리스트를 검색
	 * @param params
	 * @return
	 */
	@Auth
	@RequestMapping("/outbound/getMyCampaignList")
	public @ResponseBody Map<String, Object> getMyCampaignList(@RequestBody Map<String, Object> params) {
		params.put("tmr_id", SessionHelper.getTmrId());
		return ResultUtil.getResultMap(true, service.getCodeAndMyCamp(params), ETCommonConst.SUCCESS);
	}
	
	/**
	 * 선택한 캠페인에 해당하는 DB 레이아웃 검색
	 * @param params
	 * @return
	 */
	@Auth
	@RequestMapping("/outbound/getDBLayoutByCompaign")
	public @ResponseBody Map<String, Object> getDBLayoutByCompaign(@RequestBody Map<String, Object> params) {
		return ResultUtil.getResultMap(true, service.getDBLayoutByCompaign(params), ETCommonConst.SUCCESS);
	}
	
	/**
	 * 자신에게 분배된 DB 리스트 검색
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Auth
	@RequestMapping("/outbound/getMyDBList")
	public @ResponseBody Map<String, Object> getMyDBList(HttpServletRequest request) throws Exception {
		Map<String, Object> params = getParamToMap(request);
		params.put("tmr_id", SessionHelper.getTmrId());
		params.put("log_tmr_nm", SessionHelper.getSessionVO().getTmr_nm());
		params.put("schema_en_key", schema_en_key);
		Map<String, Object> map = service.getMyDBList(params);
		return map;
	}
	
	/**
	 * 선택한 DB의 상세 정보를 검색
	 * @param params
	 * @return
	 */
	@Auth
	@RequestMapping("/outbound/getDBDetailInfo")
	public @ResponseBody Map<String, Object> getDBDetailInfo(@RequestBody Map<String, Object> params) { 
		params.put("schema_en_key", schema_en_key);
		return ResultUtil.getResultMap(true, service.getDBDetailInfo(params), ETCommonConst.SUCCESS); 
	}
}
