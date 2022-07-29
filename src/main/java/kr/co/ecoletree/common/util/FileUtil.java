/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : aileen 
 * Create Date : 2018. 8. 2.
 * DESC : 
*****************************************************************/
package kr.co.ecoletree.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.ecoletree.common.exception.ETException;


public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 파일생성
	 * @param fileData
	 * @param request
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String createFile(MultipartFile fileData, MultipartHttpServletRequest request, String fileName) throws IOException, ETException {
		String folderPath = getTopFolder(request);
		File folder = new File(folderPath);
		
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		FileOutputStream fos = null;
		try {
			byte fileDataArr[] = fileData.getBytes();
			File newFile = new File(getCustomerFile(request, fileName));

			fos = new FileOutputStream(newFile);
			fos.write(fileDataArr);
		} catch (IOException e) {
			fileName = null;
        	logger.error(e.getMessage());
            e.printStackTrace();
            throw e;
		} finally {
			if (fos != null)
	        	fos.close();
		}
		
		
		return fileName;
	}
	
	/**
	 * 파일삭제
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(HttpServletRequest req, String fileName) {
		String folderPath = getTopFolder(req);
		
		File folder = new File(folderPath);
		
		if (!folder.exists()) {
			return false;
		}
		
		String filePath =folder.getPath() + File.separator + fileName;
		File delFile = new File(filePath);
		boolean isComplate = false;
		if(delFile.exists()) {
			if (delFile.isDirectory()) {
				File[] dirFile = delFile.listFiles();
				for (int i = 0; i < dirFile.length; i++) {
					File file = dirFile[i];
					deleteFile(req,getCustomerFile(req,file.getName()));
				}
				isComplate = delFile.delete();
			} else {
				isComplate = delFile.delete();
			}
		}
		
		return isComplate;
	}

	public static File[] listDir(HttpServletRequest req, String dirName) {
		final File dir = new File(getTopFolder(req) + File.separator + dirName);
		if (!dir.exists() || !dir.isDirectory())
			return null;

		return dir.listFiles();
	}
	
	public static String getCustomerFile(HttpServletRequest req, String fileName) {
		String folder = getTopFolder(req);
		String path = folder + File.separator + fileName;
		if (-1 < fileName.indexOf(File.separator)) {
			String parentFolder = fileName.substring(0,fileName.lastIndexOf(File.separator));
			File pFile = new File(folder+File.separator+parentFolder);
			if (!pFile.exists()) {
				pFile.mkdirs();
			}
		}
		return path;
	}
	
	@SuppressWarnings("deprecation")
	public static String getTopFolder(HttpServletRequest req) {
		String folderName = "itxCCCFile";
		String path = req.getRealPath("/").substring(0,req.getRealPath("/").lastIndexOf(File.separator,req.getRealPath("/").length()-2));
		path = path + File.separator + folderName;
		// 유저 아이디를 가지고 다시 만든다
		return path;
	}
}
