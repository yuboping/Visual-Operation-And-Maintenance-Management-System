package com.asiainfo.lcims.omc.util.poi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import com.asiainfo.lcims.omc.model.gdcu.GdcuReportHeader;
import com.asiainfo.lcims.omc.model.report.ReportFieldInfo;
import com.asiainfo.lcims.omc.util.StringHelper;

public class AisExcelDocument {

    @SuppressWarnings("unchecked")
    public static HSSFWorkbook mkComplexExcel(List<Map<String, Object>> exportList) {
        HSSFWorkbook wb = new HSSFWorkbook();
        for (Map<String, Object> exportMap : exportList) {
            String tableName = (String) exportMap.get("tableName");
            String[][] data = (String[][]) exportMap.get("data");
            List<ReportFieldInfo> fieldInfoList = (List<ReportFieldInfo>) exportMap
                    .get("fieldInfoList");
            List<GdcuReportHeader> headerList = (List<GdcuReportHeader>) exportMap
                    .get("headerList");
            HSSFSheet sheet = wb.createSheet(tableName);
            Integer headerLastrow = 0;
            Integer footLastrow = 0;
            List<GdcuReportHeader> headerTypeList = new ArrayList<GdcuReportHeader>();
            List<GdcuReportHeader> footTypeList = new ArrayList<GdcuReportHeader>();
            Integer colMax = 0;
            for (GdcuReportHeader reportHeader : headerList) {
                colMax = StringHelper.getMaxInteger(colMax, reportHeader.getLastcol());
                headerTypeList.add(reportHeader);
                headerLastrow = StringHelper.getMaxInteger(headerLastrow,
                        reportHeader.getLastrow());
            }
            if (!headerTypeList.isEmpty()) {
                mkSheetComplex(getFieldNameStyle(wb), sheet, headerTypeList, 0, headerLastrow);
            }
            mkSheetInfo(getSheetInfoStyle(wb), sheet, data, fieldInfoList.size(),
                    headerLastrow + 1);
            if (!footTypeList.isEmpty()) {
                headerLastrow = data.length + headerLastrow + 1;
                for (GdcuReportHeader footType : footTypeList) {
                    Integer firstrow = footType.getFirstrow() + headerLastrow;
                    Integer lastrow = footType.getLastrow() + headerLastrow;
                    footType.setFirstrow(firstrow);
                    footType.setLastrow(lastrow);
                }
                mkSheetComplex(getSheetInfoStyle(wb), sheet, footTypeList, headerLastrow,
                        headerLastrow + footLastrow);
            }
            for (int cm = 0; cm <= colMax; cm++) {
                sheet.setColumnWidth(cm, 4000);
            }
        }
        return wb;
    }

    // 表头样式
    private static CellStyle getFieldNameStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style.setWrapText(true);// 设置自动换行
        HSSFFont font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setBold(false);
        font.setFontHeightInPoints((short) 12);// 设置字体大小
        style.setFont(font);
        return style;
    }

    // 报表业务，复杂表头
    private static void mkSheetComplex(CellStyle style, HSSFSheet sheet,
            List<GdcuReportHeader> reportHeaderList, Integer firstrow, Integer lastrow) {
        for (int r = firstrow; r <= lastrow; r++) {
            HSSFRow row = sheet.createRow(r);
            List<GdcuReportHeader> rowHeaderList = new ArrayList<GdcuReportHeader>();
            for (GdcuReportHeader reportHeader : reportHeaderList) {
                Boolean contain = StringHelper.isContainInteger(r, reportHeader.getFirstrow(),
                        reportHeader.getLastrow());
                if (contain) {
                    rowHeaderList.add(reportHeader);
                }
            }
            if (rowHeaderList.isEmpty()) {
                continue;
            }
            Integer firstcol = 0;
            Integer lastcol = 0;
            for (GdcuReportHeader rowHeader : rowHeaderList) {
                firstcol = StringHelper.getMinInteger(firstcol, rowHeader.getFirstcol());
                lastcol = StringHelper.getMaxInteger(lastcol, rowHeader.getLastcol());
            }
            for (int c = firstcol; c <= lastcol; c++) {
                for (GdcuReportHeader rowHeader : rowHeaderList) {
                    if (c == rowHeader.getFirstcol()) {
                        HSSFCell cell = row.createCell(c);
                        cell.setCellValue(rowHeader.getHeadercontent());
                        cell.setCellStyle(style);
                    }
                }
            }
            for (GdcuReportHeader rowHeader : rowHeaderList) {
                if (r == rowHeader.getFirstrow()) {
                    Integer firstRow = rowHeader.getFirstrow();
                    Integer lastRow = rowHeader.getLastrow();
                    Integer firstCol = rowHeader.getFirstcol();
                    Integer lastCol = rowHeader.getLastcol();
                    CellRangeAddress cas = new CellRangeAddress(firstRow, lastRow, firstCol,
                            lastCol);
                    sheet.addMergedRegion(cas);
                }
            }
        }
    }

    // 内容样式
    private static CellStyle getSheetInfoStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        HSSFFont font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setBold(false);
        font.setFontHeightInPoints((short) 12);// 设置字体大小
        style.setFont(font);
        return style;
    }

    // 内容
    private static void mkSheetInfo(CellStyle style, HSSFSheet sheet, String[][] result,
            int fieldsize, int rowsize) {
        int row_length = result.length + rowsize;
        for (int i = rowsize; i < row_length; i++) {
            HSSFRow row = sheet.createRow(i);
            for (int j = 0; j < fieldsize; j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellValue(result[i - rowsize][j]);
                cell.setCellStyle(style);
            }
        }
    }

    // 自动调整列宽
    private static void autoSheet(HSSFSheet sheet) {
        if (sheet == null) {
            return;
        }
        int rownum = sheet.getFirstRowNum();
        for (int i = sheet.getLastRowNum(); i >= rownum; i--) {
            HSSFRow row = sheet.getRow(i);
            // 宽度自适应
            int width = row.getPhysicalNumberOfCells();
            for (int j = 0; j < width; j++) {
                sheet.autoSizeColumn(j);
                // 调整每一列宽度
                // sheet.autoSizeColumn((short) j);
                // 解决自动设置列宽中文失效的问题
                // sheet.setColumnWidth(j, sheet.getColumnWidth(j) * 17 / 10);
            }
        }
    }

}
