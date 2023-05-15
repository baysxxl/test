package net.wlfeng.test.util;

import cn.afterturn.easypoi.util.PoiCellUtil;
import net.wlfeng.test.model.RCell;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PoiUtil {
    public static boolean isMergedRegionFirstRow(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if (row == firstRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    public static RCell getCell(Sheet sheet, Row title, int i) {
        RCell cell = new RCell();
        //判断是否是合并单元格
        boolean mergedRegion = PoiCellUtil.isMergedRegion(sheet, title.getRowNum(), i);
        if (mergedRegion) {
            //合并单元格只取第一个单元格的数据，表格渲染只要渲染第一个
            CellRangeAddress firsMergedRegion = getFirsMergedRegion(sheet, title.getRowNum(), i);
            if (firsMergedRegion != null) {
                cell.setColSpan((firsMergedRegion.getLastColumn() - firsMergedRegion.getFirstColumn()) + 1);
                cell.setRowSpan((firsMergedRegion.getLastRow() - firsMergedRegion.getFirstRow()) + 1);
                cell.setRowIndex(title.getRowNum());
                cell.setColIndex(i);
                cell.setValue(getCellValue(sheet, title.getRowNum(), i));
                cell.setWidth(sheet.getColumnWidth(i));
            } else {
                return null;
            }
        } else {
            cell.setColSpan(1);
            cell.setRowSpan(1);
            cell.setRowIndex(title.getRowNum());
            cell.setColIndex(i);
            cell.setValue(getCellValue(sheet, title.getRowNum(), i));
            cell.setWidth(sheet.getColumnWidth(i));
        }
        return cell;

    }

    public static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);

                    return getCellValue(fCell);
                }
            }
        }

        return null;
    }

    public static String getCellValue(Sheet sheet, int row, int column) {
        String value = null;
        if (PoiCellUtil.isMergedRegion(sheet, row, column)) {
            value = getMergedRegionValue(sheet, row, column);
        } else {
            Row rowData = sheet.getRow(row);
            Cell cell = rowData.getCell(column);
            value = getCellValue(cell);
        }
        return value;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        // 判断数据的类型
        switch (cell.getCellType()) {
            // 数字
            case NUMERIC:
                cellValue = getNumericValue(cell);
                break;
            case STRING: // 字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: // Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: // 公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: // 空值
                cellValue = null;
                break;
            case ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        if (StringUtils.isNotEmpty(cellValue)){
            return cellValue.trim();
        }
        return cellValue;
    }

    public static CellRangeAddress getFirsMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int firstRow = ca.getFirstRow();
            if (row == firstRow && column == firstColumn) {
                return ca;
            }
        }
        return null;
    }

    private static String getNumericValue(Cell cell) {
        //处理yyyy年m月d日,h时mm分,yyyy年m月,m月d日等含文字的日期格式
        //判断cell.getCellStyle().getDataFormat()值，解析数值格式
                /*
                    yyyy-MM-dd----- 14
                    HH:mm:ss ---------21
                    yyyy-MM-dd HH:mm:ss ---------22
                    yyyy年m月d日--- 31
                    h时mm分 ------- 32
                    yyyy年m月------- 57
                    m月d日 ---------- 58
                    HH:mm----------- 20
                */
        String cellValue = cell.toString();
        short format = cell.getCellStyle().getDataFormat();
        if(format == 14) {
            Date date = cell.getDateCellValue();
            cellValue = DateFormatUtils.format(date, "yyyy-MM-dd");
        }else if(format == 31) {
            Date date = cell.getDateCellValue();
            cellValue = DateFormatUtils.format(date, "yyyy年M月d日");
        }else if(format == 57) {
            Date date = cell.getDateCellValue();
            cellValue = DateFormatUtils.format(date, "yyyy年M月");
        }else if(format == 58) {
            Date date = cell.getDateCellValue();
            cellValue = DateFormatUtils.format(date, "M月d日");
        }else if(format == 20) {
            Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
            cellValue = DateFormatUtils.format(date, "HH:mm");
        }else if(format == 32) {
            Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
            cellValue = DateFormatUtils.format(date, "H时mm分");
        }else if (format == 21){
            Date date = cell.getDateCellValue();
            cellValue = DateFormatUtils.format(date, "HH:mm:ss");
        }else if (format == 22){
            Date date = cell.getDateCellValue();
            cellValue = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
        } else if (cell.getCellStyle().getDataFormat() == 0) {//处理数值格式
            cell.setCellType(CellType.STRING);
            cellValue = String.valueOf(cell.getRichStringCellValue().getString());
        } else if (cell.toString().indexOf("%") != -1) {
            // 判断是否是百分数类型
            // cellValue = cell.getNumericCellValue() * 100 + "%";
            cellValue = cell.getNumericCellValue() * 100 + "";
        }
        return cellValue;
    }
}
