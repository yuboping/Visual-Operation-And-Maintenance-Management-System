package com.asiainfo.lcims.omc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ExcelUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelUtil.class);

    public static final String DEFAULT_DATE_PATTERN = "yyyy年MM月dd日";// 默认日期格式
    public static final int DEFAULT_COLOUMN_WIDTH = 17;

    /**
     * 导出Excel 2007 OOXML (.xlsx)格式
     * 
     * @param title
     *            标题行
     * @param querytime
     *            查询时间
     * @param fields
     *            属性-列头
     * @param datePattern
     *            日期格式，传null值则默认 年月日
     * @param colWidth
     *            列宽 默认 至少17个字节
     * @param out
     *            输出流
     * @param datas
     *            Excel表格里要填入的内容
     */
    public static void exportExcelX(String title, String querytime, String[] fields,
            String datePattern, int colWidth, OutputStream out, String[][] datas) {
        if (datePattern == null) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        // 声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);// 缓存
        workbook.setCompressTempFiles(true);
        // 生成一个(带标题)表格
        SXSSFSheet sheet = workbook.createSheet();
        setTitle(workbook, sheet, title, querytime, fields);
        ZipSecureFile.setMinInflateRatio(0l);
        // 设置列宽
        int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;// 至少字节数
        int[] arrColWidth = new int[fields.length];
        // 填充数据 数据从 rowIndex =3开始
        int dataRowIndex = 3;
        for (String[] data : datas) {
            SXSSFRow dataRow = sheet.createRow(dataRowIndex);
            for (int i = 0; i < data.length; i++) {
                dataRow.createCell(i).setCellValue(data[i]);
                dataRow.getCell(i).setCellStyle(getDataStyle(data[i], workbook));
                // dataRow.getSheet().autoSizeColumn(i);
                int bytes = fields[i].getBytes().length;
                arrColWidth[i] = bytes < minBytes ? minBytes : bytes;
                sheet.setColumnWidth(i, arrColWidth[i] * 256);
            }
            dataRowIndex++;
        }
        try {
            workbook.write(out);
            workbook.close();
            workbook.dispose();
        } catch (IOException e) {
            LOG.error("exportExcelX error, reason {}", e);
        }
    }

    public static void setTitle(SXSSFWorkbook workbook, SXSSFSheet sheet, String title,
            String querytime, String[] fields) {
        // 表头样式
        CellStyle titleStyle = getSheetInfoStyle(workbook);
        // 产生表格标题行,以及设置列宽
        SXSSFRow titleRow = sheet.createRow(0);
        SXSSFRow conditionRow = sheet.createRow(1); // 查询条件 rowIndex =1
        SXSSFRow fieldRow = sheet.createRow(2); // 该行填充标题
        for (int i = 0; i < fields.length; i++) {
            titleRow.createCell(i).setCellStyle(titleStyle);
            conditionRow.createCell(i).setCellStyle(titleStyle);
            fieldRow.createCell(i).setCellStyle(titleStyle);
            fieldRow.getCell(i).setCellValue(fields[i]);
            if (i == 0) {
                // 表头 rowIndex=0
                titleRow.getCell(0).setCellValue(title);
                conditionRow.getCell(0).setCellValue("时间:");
            } else if (i == 1) {
                conditionRow.getCell(1).setCellValue(querytime);
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fields.length - 1));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, fields.length - 1));
    }

    private static CellStyle getSheetInfoStyle(SXSSFWorkbook workbook) {
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);// 设置字体大小
        titleStyle.setFont(font);
        return titleStyle;
    }

    public static CellStyle getDataStyle(String value, SXSSFWorkbook workbook) {
        CellStyle dataStyle = workbook.createCellStyle();
        Boolean strResult = false;
        if (value != null) {
            strResult = value.matches("-?[0-9]+.*[0-9]*");
        }
        if (strResult) {
            DataFormat format = workbook.createDataFormat();
            dataStyle.setDataFormat(format.getFormat("@"));
        }
        dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        Font font = workbook.createFont();
        dataStyle.setFont(font);
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);// 设置字体大小
        dataStyle.setFont(font);
        return dataStyle;
    }
    /**
     * 
     * @param title  文件名称
     * @param querytime 导出日期(不输入默认当前日期) 默认格式:yyyy-MM-dd HH:mm:ss
     * @param fields  格式:[enName&chName] eg:["name&姓名","id&主键"] desc:英文名和中文名,用&连接
     * @param request
     * @param response
     * @param datas  导出数据(实体类字段都是String类型)
     * @Description:输出文件格式
     * ----------------------------------
     *|      		title               |
     * ----------------------------------
     *|时间:  |      querytime           |
     *-----------------------------------
     *|field1|field2|field3|field4|field5|
     *-----------------------------------
     * 
     */
    public static void  downloadExcelToFile(String title, String querytime, String[] fields,
            HttpServletRequest request, HttpServletResponse response,List<?> datas) {
    	if(fields !=null && datas!=null) {
    		
	    	String[][] datass=new String[datas.size()] [fields.length];
	    	String[] enTableHead=new String[fields.length];
	    	String[] zhTableHead=new String[fields.length];
	    	createHead(enTableHead,zhTableHead,fields);
	    	for (int i = 0; i < datas.size(); i++) {
	    		Object data=datas.get(i);
	    		for (int j = 0; j < enTableHead.length; j++) {
	    			String dataValue=getFieldByClasss(enTableHead[j],data);
	    			datass[i][j]=dataValue;
	    		}
			}
	    	if(querytime==null || querytime.length()==0) {
	    		querytime=DateUtil.parseStr(new Date(),DateUtil.C_TIME_PATTON_DEFAULT);
	    	}
	    	downloadExcelFile2(title,querytime,zhTableHead,response,datass);
    	}
    }
    
    
    public static void downloadExcelFile2(String title, String querytime, String[] fields,HttpServletResponse response, String[][] datas) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ServletOutputStream outputStream = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ExcelUtil.exportExcelX(title, querytime, fields, null, 0, os, datas);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // Excel导出IE浏览器文件名乱码
            String filename = URLEncoder.encode(title + ".xlsx", "UTF8");
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("octets/stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setContentLength(content.length);
            outputStream = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[8 * 1024];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            LOG.error("downloadExcelFile error, reason {}", e);
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(outputStream);
        }
    }
    
    private static String getFieldByClasss(String fieldName, Object object) {
		Field field = null;
		String value=null;
		Class<?> clazz = object.getClass();
		try {
			field = clazz.getDeclaredField(fieldName);
			if(field==null) {
				clazz = clazz.getSuperclass();
				field = clazz.getDeclaredField(fieldName);
			}
			field.setAccessible(true);
			return  String.valueOf(field.get(object));
		} catch (Exception e) {
			LOG.error("exportExcelX error, reason {}", e);
		}
		return value;
	}
    public static void createHead(String[] enTableHead,String[] zhTableHead,String[] sourceTableHead) {
    	for (int i = 0; i < sourceTableHead.length; i++) {
    		String[] tableHead=sourceTableHead[i].split("&");
    		enTableHead[i]=tableHead[0];
    		zhTableHead[i]=tableHead[1];
		}
    }

    public static void downloadExcelFile(String title, String querytime, String[] fields,
            HttpServletRequest request, HttpServletResponse response, String[][] datas) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ServletOutputStream outputStream = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ExcelUtil.exportExcelX(title, querytime, fields, null, 0, os, datas);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);

            // Excel导出IE浏览器文件名乱码
            String filename = "";
            String userAgent = request.getHeader("user-agent");
            if (userAgent != null
                    && (userAgent.indexOf("Firefox") >= 0 || userAgent.indexOf("Chrome") >= 0 || userAgent
                            .indexOf("Safari") >= 0)) {
                filename = new String((title + ".xlsx").getBytes(), "ISO8859-1");
            } else {
                filename = URLEncoder.encode(title + ".xlsx", "UTF8");
            }
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setContentLength(content.length);
            outputStream = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[8 * 1024];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            LOG.error("downloadExcelFile error, reason {}", e);
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * 读取.xlsx 内容
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static List<ArrayList<String>> readXlsx(MultipartFile file) {
        List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        InputStream input = null;
        XSSFWorkbook wb = null;
        try {
            input = file.getInputStream();
            // 创建文档
            wb = new XSSFWorkbook(input);
            ArrayList<String> rowList = null;
            int totoalRows = 0;// 总行数
            int totalCells = 0;// 总列数
            // 读取sheet(页)
            for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                XSSFSheet xssfSheet = wb.getSheetAt(sheetIndex);

                if (xssfSheet == null) {
                    continue;
                }
                totoalRows = xssfSheet.getLastRowNum();
                // 读取row
                for (int rowIndex = 0; rowIndex <= totoalRows; rowIndex++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowIndex);

                    if (xssfRow == null) {
                        continue;
                    }
                    rowList = new ArrayList<String>();
                    totalCells = xssfRow.getLastCellNum();

                    // 读取列
                    for (int cellIndex = 0; cellIndex < totalCells; cellIndex++) {
                        XSSFCell xssfCell = xssfRow.getCell(cellIndex);
                        if (xssfCell == null) {
                            rowList.add("");
                        } else {
                            xssfCell.setCellType(Cell.CELL_TYPE_STRING);
                            rowList.add(String.valueOf(xssfCell.getStringCellValue()));
                        }
                    }

                    list.add(rowList);

                }
            }
        } catch (Exception e) {
            LOG.error("Exception:", e);
            return null;
        } finally {
            try {
                if (wb != null) {
                    wb.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                LOG.error("Exception:", e);
            }
        }

        return list;
    }

    /**
     * 读取 .xls内容
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static List<ArrayList<String>> readXls(MultipartFile file) {
        List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

        // 创建输入流
        InputStream input = null;
        // 创建文档
        HSSFWorkbook wb = null;

        try {
            input = file.getInputStream();
            // 创建文档
            wb = new HSSFWorkbook(input);

            ArrayList<String> rowList = null;
            int totoalRows = 0;// 总行数
            int totalCells = 0;// 总列数
            // 读取sheet(页)
            for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                HSSFSheet hssfSheet = wb.getSheetAt(sheetIndex);

                if (hssfSheet == null) {
                    continue;
                }

                totoalRows = hssfSheet.getLastRowNum();
                // 读取row
                for (int rowIndex = 0; rowIndex <= totoalRows; rowIndex++) {
                    HSSFRow hssfRow = hssfSheet.getRow(rowIndex);

                    if (hssfRow == null) {
                        continue;
                    }
                    rowList = new ArrayList<String>();
                    totalCells = hssfRow.getLastCellNum();

                    // 读取列
                    for (int cellIndex = 0; cellIndex < totalCells; cellIndex++) {
                        HSSFCell hssfCell = hssfRow.getCell(cellIndex);
                        if (hssfCell == null) {
                            rowList.add("");
                        } else {
                            hssfCell.setCellType(Cell.CELL_TYPE_STRING);
                            rowList.add(String.valueOf(hssfCell.getStringCellValue()));
                        }
                    }

                    list.add(rowList);

                }
            }
        } catch (Exception e) {
            LOG.error("Exception:", e);
            return null;
        } finally {
            try {
                if (wb != null) {
                    wb.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                LOG.error("Exception:", e);
            }
        }
        return list;
    }

    /**
     * 获取文件类型
     * 
     * @param path
     * @return
     */
    public static String getPostfix(String path) {
        if (StringUtils.isBlank(path) || !path.contains(".")) {
            return null;
        }
        return path.substring(path.lastIndexOf(".") + 1, path.length()).trim();
    }
}
