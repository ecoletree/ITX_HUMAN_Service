package kr.co.ecoletree.common.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ecoletree.common.ETCommonConst.ETFileConst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

@Component
public class ETImageView extends AbstractView {

	private static final Logger logger = LoggerFactory.getLogger(ETImageView.class);

	private String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "Firefox";
    }

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String browser = getBrowser(request);
		String orgName = (String)model.get(ETFileConst.OrgName);
		String newName = (String)model.get(ETFileConst.NewName);
		String path = (String)model.get(ETFileConst.Path);
		String encodedOrgName = null;

		// 한글처리
		if (browser.equals("MSIE")) {
			encodedOrgName = URLEncoder.encode(orgName, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
			encodedOrgName = "\"" + new String(orgName.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
			encodedOrgName = "\"" + new String(orgName.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
		    StringBuffer sb = new StringBuffer();
			for (int i = 0; i < orgName.length(); i++) {
				char c = orgName.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedOrgName = sb.toString();
		} else {
		    //throw new RuntimeException("Not supported browser");
		    throw new IOException("Not supported browser");
		}
		/*logger.debug("원본 파일명 : " + orgName);
		logger.debug("변환된 파일명 : " + encodedOrgName);*/

		// 확장자 추출
		String ext = orgName.substring(orgName.lastIndexOf(".")+1);
		String contentType = "image/jpg";
		if(ext.equalsIgnoreCase("gif")) contentType = "image/gif";
		else if(ext.equalsIgnoreCase("bmp")) contentType = "image/bmp";
		else if(ext.equalsIgnoreCase("png")) contentType = "image/png";

		response.setContentType(contentType);

		// 파일명 지정
		response.setHeader("Content-Disposition", "attachment; filename="+ encodedOrgName);

		File file = new File(path + "/" + newName);
		response.setContentLength((int)file.length());
		FileInputStream fis = null;
		OutputStream os = null;
		try {
			fis = new FileInputStream(file);
			os = response.getOutputStream();
			FileCopyUtils.copy(fis, os);
		} catch (FileNotFoundException e) {
			logger.debug("다음 파일을 찾을 수 없습니다 : " + path + "/" + newName);
		} catch (Exception e) {
			// 다운로드시 발생하는 오류 무시
			logger.debug(e.getMessage(), e);
		} finally {
			if(os != null)
				os.close();
			if(fis != null)
				fis.close();
		}

	}

}
