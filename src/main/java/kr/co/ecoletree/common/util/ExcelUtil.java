package kr.co.ecoletree.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * Class Name : ExcelUtil.java<br>
 * Description : ExcelUtil
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
public class ExcelUtil {
	
	/**
	 * @param column : 컬럼 번호
	 * @param record : 줄 번호
	 * @param name : 타이틀명
	 * @param size : 셀의 가로크기
	 * @param fontsize : 폰트사이즈
	 * @param sheet : sheet Object
	 * @param color : 폰트 색상
	 * @param bgcolor : 셀의 배경색상
	 * @return
	 */
	public static WritableSheet getFormatTitle(int column, int record, String name, int size, int fontsize, WritableSheet sheet, Colour color, Colour bgcolor) {
		
		// 제목컬럼 폰트, 색상설정 : 폰트, 사이즈, 글씨굵기, 이택릭체, 언더라인, 색상
		WritableFont font = new WritableFont(WritableFont.createFont("맑은 고딕"), fontsize, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, color != null ? color : Colour.WHITE);
		// 제목컬럼 생성
		WritableCellFormat cell = new WritableCellFormat(font);
		// 라벨생성
		Label label = null;
		
		try {
			// 가로정렬지정(기본값:가운데정렬)
			cell.setAlignment(Alignment.CENTRE);
			// 세로정렬지정(가운데정렬)
			cell.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 테두리지정
			cell.setBorder(Border.ALL, BorderLineStyle.THIN);
			// 배경색상(기본값:WHITE)
			cell.setBackground(bgcolor != null ? bgcolor : Colour.GRAY_50);
			// 제목으로 사용할 라벨을 생성함.
			label = new Label(column, record, name, cell);
			// 컬럼사이즈 조정 (0번째)의 넓이 설정
			sheet.setColumnView(column, size);
			// 컬럼에 라벨을 설정함.
			sheet.addCell(label);
			
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
	/**
	 * @param column : 컬럼순번
	 * @param record : 레코드순번
	 * @param data : 데이터
	 * @param alignment 가로정렬
	 * @param bgcolor : 배경색상
	 * @param wrap : 개행문자 표시여부
	 * @param size : 폰트사이즈
	 * @return
	 */
	public static Label getFormatCell(int column, int record, String data, Alignment alignment, Colour bgcolor, boolean wrap, int size) {
		
		// 제목컬럼 폰트, 색상설정 : 폰트, 사이즈, 글씨굵기, 이택릭체, 언더라인, 색상
		WritableFont font = new WritableFont(WritableFont.createFont("맑은 고딕"), size, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREY_80_PERCENT);
		// 제목컬럼 생성
		WritableCellFormat cell = new WritableCellFormat(font);
		// 라벨생성
		Label label = null;
		
		try {
			// 가로정렬지정(기본값:가운데정렬)
			cell.setAlignment(alignment != null ? alignment : Alignment.CENTRE);
			// 세로정렬지정(가운데정렬)
			cell.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 배경색상(기본값:WHITE)
			cell.setBackground(bgcolor != null ? bgcolor : Colour.WHITE);
			// 테두리지정
			cell.setBorder(Border.ALL, BorderLineStyle.THIN);
			// 개행문자처리
			cell.setWrap(wrap);
			// 데이터를 라벨을 생성함.
			label = new Label(column, record, data, cell);
			
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return label;
	}
	
	/**
	 * @param sheet
	 * @param titleStr
	 * @return
	 */
	public static WritableSheet getSheetTitle(WritableSheet sheet, String titleStr) {
		
		// 제목컬럼 폰트, 색상설정 : 폰트, 사이즈, 글씨굵기, 이택릭체, 언더라인, 색상
		WritableFont font = new WritableFont(WritableFont.createFont("맑은 고딕"), 16, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREY_80_PERCENT);
		// 제목컬럼 생성
		WritableCellFormat cell = new WritableCellFormat(font);
		// 라벨생성
		Label label = null;
		
		try {
			// 가로정렬지정(기본값:가운데정렬)
			cell.setAlignment(Alignment.LEFT);
			// 세로정렬지정(가운데정렬)
			cell.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 테두리지정
			cell.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
			// 배경색상(기본값:WHITE)
			cell.setBackground(Colour.WHITE);
			// 제목으로 사용할 라벨을 생성함.
			label = new Label(0, 0, titleStr, cell);
			// 컬럼사이즈 조정 (0번째)의 넓이 설정
			// sheet.setColumnView(column, size);
			// 컬럼에 라벨을 설정함.
			sheet.addCell(label);
			// 타이틀셀병합
			sheet.mergeCells(0, 0, 8, 0);
			
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
	/**
	 * @param file
	 */
	public static void deleteFile(String file) {
		try {
			File f = new File(file);
			if (f.isFile())
				f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param sheet
	 * @param titleStr
	 * @param hap : 0 부터 시작해서 병합할 셀의 갯수
	 * @return
	 */
	public static WritableSheet getSheetTitlehap(WritableSheet sheet, String titleStr, int hap) {
		// 제목컬럼 폰트, 색상설정 : 폰트, 사이즈, 글씨굵기, 이택릭체, 언더라인, 색상
		WritableFont font = new WritableFont(WritableFont.createFont("맑은 고딕"), 16, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREY_80_PERCENT);
		// 제목컬럼 생성
		WritableCellFormat cell = new WritableCellFormat(font);
		// 라벨생성
		Label label = null;
		try {
			// 가로정렬지정(기본값:가운데정렬)
			cell.setAlignment(Alignment.CENTRE);
			// 세로정렬지정(가운데정렬)
			cell.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 테두리지정
			// cell.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
			// 배경색상(기본값:WHITE)
			cell.setBackground(Colour.WHITE);
			// 제목으로 사용할 라벨을 생성함.
			label = new Label(0, 0, titleStr, cell);
			// 컬럼사이즈 조정 (0번째)의 넓이 설정
			// sheet.setColumnView(column, size);
			// 컬럼에 라벨을 설정함.
			sheet.addCell(label);
			// 타이틀셀병합
			sheet.mergeCells(0, 0, hap, 0);
			
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
	/**
	 * @param sheet
	 * @param hap
	 * @param record
	 * @return
	 */
	public static WritableSheet getSheetDatePrint(WritableSheet sheet, int hap, int record) {
		// 제목컬럼 폰트, 색상설정 : 폰트, 사이즈, 글씨굵기, 이택릭체, 언더라인, 색상
		WritableFont font = new WritableFont(WritableFont.createFont("맑은 고딕"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREY_80_PERCENT);
		// 제목컬럼 생성
		WritableCellFormat cell = new WritableCellFormat(font);
		// 라벨생성
		Label label = null;
		
		try {
			String datePrint = "CREATE DATE: " + DateUtil.changeDateFormat(DateUtil.getCurrentDateByFormat("yyyyMMddHHmmss"), "yyyyMMddHHmmss", "yyyy.MM.dd HH:mm:ss");
			
			// 가로정렬지정(기본값:가운데정렬)
			cell.setAlignment(Alignment.LEFT);
			// 세로정렬지정(가운데정렬)
			cell.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 테두리지정
			// cell.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
			// 배경색상(기본값:WHITE)
			cell.setBackground(Colour.WHITE);
			// 제목으로 사용할 라벨을 생성함.
			label = new Label(0, record, datePrint, cell);
			// 컬럼사이즈 조정 (column번째)의 넓이 설정
			// sheet.setColumnView(column, size);
			// 컬럼에 라벨을 설정함.
			sheet.addCell(label);
			sheet.mergeCells(0, record, hap, record);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
	/**
	 * @param startDate
	 * @param endDate
	 * @param sheet
	 * @param hap
	 * @param record
	 * @return
	 */
	public static WritableSheet getSheetDatePrint(String startDate, String endDate, WritableSheet sheet, int hap, int record) {
		// 제목컬럼 폰트, 색상설정 : 폰트, 사이즈, 글씨굵기, 이택릭체, 언더라인, 색상
		WritableFont font = new WritableFont(WritableFont.createFont("맑은 고딕"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREY_80_PERCENT);
		// 제목컬럼 생성
		WritableCellFormat cell = new WritableCellFormat(font);
		// 라벨생성
		Label label = null;
		
		try {
			String datePrint = "PERIOD : " + DateUtil.changeDateFormat(startDate, "yyyyMMdd", "yyyy.MM.dd") + " ~ " + DateUtil.changeDateFormat(endDate, "yyyyMMdd", "yyyy.MM.dd");
			
			// 가로정렬지정(기본값:가운데정렬)
			cell.setAlignment(Alignment.LEFT);
			// 세로정렬지정(가운데정렬)
			cell.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 테두리지정
			// cell.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
			// 배경색상(기본값:WHITE)
			cell.setBackground(Colour.WHITE);
			// 제목으로 사용할 라벨을 생성함.
			label = new Label(0, record, datePrint, cell);
			// 컬럼사이즈 조정 (column번째)의 넓이 설정
			// sheet.setColumnView(column, size);
			// 컬럼에 라벨을 설정함.
			sheet.addCell(label);
			sheet.mergeCells(0, record, hap, record);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
	/**
	 * @param sheet
	 * @param hap
	 * @param record
	 * @param data
	 * @return
	 */
	public static WritableSheet getSheetDescriptionPrint(WritableSheet sheet, int hap, int record, String data) {
		// 제목컬럼 폰트, 색상설정 : 폰트, 사이즈, 글씨굵기, 이택릭체, 언더라인, 색상
		WritableFont font = new WritableFont(WritableFont.createFont("맑은 고딕"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREY_80_PERCENT);
		// 제목컬럼 생성
		WritableCellFormat cell = new WritableCellFormat(font);
		// 라벨생성
		Label label = null;
		
		try {
			String datePrint = "CREATE DATE: " + DateUtil.changeDateFormat(DateUtil.getCurrentDateByFormat("yyyyMMddHHmmss"), "yyyyMMddHHmmss", "yyyy.MM.dd HH:mm:ss") + "\r\nDescription: " + data;
			
			cell.setWrap(true);
			// 가로정렬지정(기본값:가운데정렬)
			cell.setAlignment(Alignment.LEFT);
			// 세로정렬지정(가운데정렬)
			cell.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 테두리지정
			// cell.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
			// 배경색상(기본값:WHITE)
			cell.setBackground(Colour.WHITE);
			// 제목으로 사용할 라벨을 생성함.
			label = new Label(0, record, datePrint, cell);
			// 컬럼사이즈 조정 (column번째)의 넓이 설정
			// sheet.setColumnView(column, size);
			// 컬럼에 라벨을 설정함.
			sheet.addCell(label);
			sheet.mergeCells(0, record, hap, record);
			sheet.setRowView(record, 50 * 20);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
	/**
	 * @param startDate
	 * @param endDate
	 * @param sheet
	 * @param hap
	 * @param record
	 * @param data
	 * @return
	 */
	public static WritableSheet getSheetDescriptionPrint(String startDate, String endDate, WritableSheet sheet, int hap, int record, String data) {
		// 제목컬럼 폰트, 색상설정 : 폰트, 사이즈, 글씨굵기, 이택릭체, 언더라인, 색상
		WritableFont font = new WritableFont(WritableFont.createFont("맑은 고딕"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREY_80_PERCENT);
		// 제목컬럼 생성
		WritableCellFormat cell = new WritableCellFormat(font);
		// 라벨생성
		Label label = null;
		
		try {
			String datePrint = "PERIOD : " + DateUtil.changeDateFormat(startDate, "yyyyMMdd", "yyyy.MM.dd") + " ~ " + DateUtil.changeDateFormat(endDate, "yyyyMMdd", "yyyy.MM.dd") + "\r\nDescription: " + data;
			
			cell.setWrap(true);
			// 가로정렬지정(기본값:가운데정렬)
			cell.setAlignment(Alignment.LEFT);
			// 세로정렬지정(가운데정렬)
			cell.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 테두리지정
			// cell.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
			// 배경색상(기본값:WHITE)
			cell.setBackground(Colour.WHITE);
			// 제목으로 사용할 라벨을 생성함.
			label = new Label(0, record, datePrint, cell);
			// 컬럼사이즈 조정 (column번째)의 넓이 설정
			// sheet.setColumnView(column, size);
			// 컬럼에 라벨을 설정함.
			sheet.addCell(label);
			sheet.mergeCells(0, record, hap, record);
			sheet.setRowView(record, 50 * 20);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
	/**
	 * @param column : 컬럼순번
	 * @param record : 줄순번
	 * @param data : 데이터 ( INT
	 * @param alignment : 가로정렬
	 * @param bgcolor : 배경색상
	 * @param wrap : 개행문자 표시여부 (자동 줄바꿈)
	 * @return
	 */
	public static Number getFormatCellInt(int column, int record, int data, Alignment alignment, Colour bgcolor, boolean wrap) {
		
		// Cell Date put
		Number num = new Number(column, record, data);
		
		try {
			// basic font format (폰트, 색상설정 : 폰트, 사이즈, 글씨굵기, 이택릭체, 언더라인, 색상)
			WritableFont font = new WritableFont(WritableFont.createFont("맑은 고딕"), 9, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREY_80_PERCENT);
			// Cell number format
			
			// NumberFormats exp4 = new NumberFormats();
			// WritableCellFormat cell = new WritableCellFormat(font, exp4.FORMAT1);
			WritableCellFormat cell = new WritableCellFormat(font, NumberFormats.FORMAT1);
			// 가로정렬지정(기본값:가운데정렬)
			cell.setAlignment(alignment != null ? alignment : Alignment.CENTRE);
			// 세로정렬지정(가운데정렬)
			cell.setVerticalAlignment(VerticalAlignment.CENTRE);
			// 배경색상(기본값:WHITE)
			cell.setBackground(bgcolor != null ? bgcolor : Colour.WHITE);
			// 테두리지정
			cell.setBorder(Border.ALL, BorderLineStyle.THIN);
			// 개행문자처리
			cell.setWrap(wrap);
			// Cell Format insert
			num.setCellFormat(cell);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	/**
	 * @param startRowNum
	 * @param headerName
	 * @param excelFile
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static List<Map<String, String>> getExcelDataList(int startRowNum, LinkedHashMap<String, String> headerName, MultipartFile excelFile) throws Exception {
		String fileName = excelFile.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		
		List<Object> keyList = new ArrayList<Object>(headerName.keySet());
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		if (fileType.equals("xls")) {
			POIFSFileSystem fileSystem = new POIFSFileSystem(excelFile.getInputStream());
			HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
			
			HSSFSheet sheet = workbook.getSheetAt(0);
			int totalRow = sheet.getPhysicalNumberOfRows();
			
			for (int i = startRowNum; i < totalRow; i++) {
				HSSFRow row = sheet.getRow(i);
				Map<String, String> map = new HashMap<String, String>();
				
				for (int j = 0; j < keyList.size(); j++) {
					HSSFCell cell = row.getCell(j);
					String data = "";
					
					if (cell != null) {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						data = cell.getStringCellValue();
					} else {
						data = "";
					}
					map.put(String.valueOf(keyList.get(j)), data);
				}
				
				list.add(map);
			}
		} else if (fileType.equals("xlsx")) {
			XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
			
			XSSFSheet sheet = workbook.getSheetAt(0);
			int totalRow = sheet.getPhysicalNumberOfRows();
			
			for (int i = startRowNum; i < totalRow; i++) {
				XSSFRow row = sheet.getRow(i);
				Map<String, String> map = new HashMap<String, String>();
				
				for (int j = 0; j < keyList.size(); j++) {
					XSSFCell cell = row.getCell(j);
					String data = "";
					
					if (cell != null) {
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						data = cell.getStringCellValue();
					} else {
						data = "";
					}
					map.put(String.valueOf(keyList.get(j)), data);
				}
				
				list.add(map);
			}
		}
		
		return list;
	}
	
}
