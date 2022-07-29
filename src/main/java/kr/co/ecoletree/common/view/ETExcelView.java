package kr.co.ecoletree.common.view;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.format.Colour;
import jxl.format.Pattern;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import kr.co.ecoletree.common.ETCommonConst.ETExcelConst;
import kr.co.ecoletree.common.util.ExcelUtil;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * Class Name : ExcelView.java<br>
 * Description : ExcelView<br>
 * 엑셀 뷰 공통 컴포넌트<br>
 * 사용예 : <br>
 *
 *
 * @author wan
 * @since 2014. 2. 3.
 * @version 1.0
 * @see
 * 
 *      <pre>
 *  Modification Information (개정이력)
 *  수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *
 *      </pre>
 */
@SuppressWarnings("unchecked")
@Component
public class ETExcelView extends AbstractXlsView { // extends AbstractJExcelView
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> excelData = (Map<String, Object>) model.get(ETExcelConst.ExcelData);
		
		String FileName = (String) excelData.get(ETExcelConst.ExcelFileName);
		String createFileNm = "";
		
		Colour columColor = Colour.WHITE;
		int cellWidth = 20;
		
		if (excelData.containsKey(ETExcelConst.ExcelCellColor)) {
			columColor = (Colour) excelData.get(ETExcelConst.ExcelCellColor);
		}
		
		if (excelData.containsKey(ETExcelConst.ExcelCellWidth)) {
			cellWidth = (Integer) excelData.get(ETExcelConst.ExcelCellWidth);
		}
		
		logger.debug("FileName:" + FileName);
		
		if (!"".equals(FileName)) {
			createFileNm = createFileName(FileName);
			setFileNameToResponse(request, response, createFileNm);
		} else {
			FileName = ETExcelConst.ExcelData;
		}
		
		// WritableSheet sheet = workbook.createSheet(FileName, 0);
		WritableSheet sheet = (WritableSheet) workbook.createSheet(FileName);
		// sheet = DmExcelUtil.getSheetTitle(sheet, FileName);
		
		// 컬럼세팅
		LinkedHashMap<String, String> columList = null;
		if (excelData.containsKey(ETExcelConst.ColumnNameList)) {
			columList = (LinkedHashMap<String, String>) excelData.get(ETExcelConst.ColumnNameList);
		}
		List<String> columnMethod = new ArrayList<String>();
		int cnt = 0;
		if (columList != null) {
			
			Iterator<String> keys = columList.keySet().iterator();
			
			while (keys.hasNext()) {
				String columMethod = keys.next();
				columnMethod.add(columMethod);
				sheet.setColumnView(cnt, cellWidth);
				Label columnData = ExcelUtil.getFormatCell(cnt, 2, columList.get(columMethod), null, columColor, true, 12);
				sheet.addCell(columnData);
				cnt++;
			}
			sheet = ExcelUtil.getSheetTitlehap(sheet, FileName, cnt - 1);
			sheet = ExcelUtil.getSheetDatePrint(sheet, cnt - 1, 1);
		}
		
		List<Object> objList = null;
		if (excelData.containsKey(ETExcelConst.CellDataList)) {
			objList = (List<Object>) excelData.get(ETExcelConst.CellDataList);
			int row = 3;
			
			for (int i = 0; i < objList.size(); i++) {
				Object obj = objList.get(i);
				
				for (int j = 0; j < columnMethod.size(); j++) {
					sheet.setColumnView(j, cellWidth);
					String data = null;
					if (obj instanceof Map) {
						data = ((Map<String, String>) obj).get(columnMethod.get(j));
					} else {
						data = getMethodValue(obj, columnMethod.get(j));
					}
					Label celData = ExcelUtil.getFormatCell(j, row, data, null, null, true, 10);
					sheet.addCell(celData);
				}
				
				row++;
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static WritableCellFormat getCellFormat(Colour colour, Pattern pattern) throws WriteException {
		WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
		
		WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
		cellFormat.setBackground(colour, pattern);
		return cellFormat;
	}
	
	private String getMethodValue(Object obj, String methodNm) throws Exception {
		Method[] methods = obj.getClass().getMethods();
		String res = "";
		for (int i = 0; i < methods.length; i++) {
			String _methodName = methods[i].getName();
			String _methodHead = _methodName.substring(0, 3);
			if ("get".equals(_methodHead)) {
				Method _method = obj.getClass().getMethod(_methodName, new Class<?>[] {});
				
				if (_method.getReturnType() == java.lang.String.class || _method.getReturnType() == java.lang.Integer.class) {
					Object retVal = _method.invoke(obj, new Object[] {});
					String strVal = "";
					if (retVal != null)
						strVal = retVal.toString();
					if (methodNm.equalsIgnoreCase(_methodName.substring(3))) {
						res = strVal;
						break;
					}
				} else if (_method.getReturnType() == java.util.Date.class || _method.getReturnType() == java.sql.Date.class) {
					Date date = (Date) _method.invoke(obj, new Object[] {});
					if (date != null) {
						if (methodNm.equalsIgnoreCase(_methodName.substring(3))) {
							res = String.format("%1$tY.%1$tm.%1$td", date);
							break;
						}
					}
				}
				
			}
		}
		return res;
	}
	
	private void setFileNameToResponse(HttpServletRequest request, HttpServletResponse response, String fileName) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.indexOf("MSIE 5.5") >= 0) {
			response.setContentType("doesn/matter");
			response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
		} else {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		}
		response.setHeader("Content-Transfer-Encoding", "binary");
		
	}
	
	private String createFileName(String fname) {
		SimpleDateFormat fileFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String rs = new StringBuilder(fname).append("_").append(fileFormat.format(new Date())).append(".xls").toString();
		
		try {
			rs = new String(rs.getBytes("euc-kr"), "8859_1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
}
