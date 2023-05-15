package net.wlfeng.test.util;

import cn.afterturn.easypoi.util.PoiCellUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import net.wlfeng.test.model.RCell;
import net.wlfeng.test.model.RRow;
import net.wlfeng.test.model.RSheet;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;

/**
 * @description: 多个Excel合并Sheet
 * @author: weilingfeng
 * @time: 2023/4/08 10:28
 */
public class ExcelUtil {

    public static void main(String[] args) throws IOException {
        /*List<String> list = Arrays.asList(
                new File("D:\\test\\a.xlsx").toString(),
                new File("D:\\test\\b.xlsx").toString()
        );
        mergexcel(list,"D:\\test","ab.xlsx");
        System.out.println("done");*/

        InputStream in = new FileInputStream("D:\\work\\临时文件\\产出报表.xlsx");
        RSheet rSheet = parseExcelDataStructure(new XSSFWorkbook(in));
        System.out.println(JSON.toJSONString(rSheet));
    }

    /**
     * * 合并多个ExcelSheet
     * @param files 文件字符串(file.toString)集合,按顺序进行合并，合并的Excel中Sheet名称不可重复
     * @param excelName 合并后Excel名称(包含后缀.xslx)
     * @param dirPath 存储目录
     * @return
     */
    public static void mergexcel(List<String> files, String dirPath, String excelName) {
        XSSFWorkbook toExcel = new XSSFWorkbook();
        // 遍历每个源excel文件，TmpList为源文件的名称集合
        for (String fromExcelPath: files) {
            try (InputStream in = new FileInputStream(fromExcelPath)) {
                XSSFWorkbook fromExcel = new XSSFWorkbook(in);
                int length = fromExcel.getNumberOfSheets();
                if (length > 0) {
                    // 遍历复制每个sheet
                    for (int i = 0; i < length; i++) {
                        XSSFSheet oldSheet = fromExcel.getSheetAt(i);
                        XSSFSheet newSheet = toExcel.createSheet(oldSheet.getSheetName());
                        copySheet(toExcel, oldSheet, newSheet);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 定义新生成的xlxs表格文件
        String allFileName = dirPath + File.separator + excelName;
        try (FileOutputStream fileOut = new FileOutputStream(allFileName)) {
            toExcel.write(fileOut);
            fileOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                toExcel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并单元格
     * @param fromSheet
     * @param toSheet
     */
    private static void mergeSheetAllRegion(XSSFSheet fromSheet, XSSFSheet toSheet) {
        int num = fromSheet.getNumMergedRegions();
        CellRangeAddress cellR;
        for (int i = 0; i < num; i++) {
            cellR = fromSheet.getMergedRegion(i);
            toSheet.addMergedRegion(cellR);
        }
    }

    /**
     * Sheet复制
     * @param wb
     * @param fromSheet
     * @param toSheet
     */
    private static void copySheet(XSSFWorkbook wb, XSSFSheet fromSheet, XSSFSheet toSheet) {
        mergeSheetAllRegion(fromSheet, toSheet);
        // 设置列宽
        int length = fromSheet.getRow(fromSheet.getFirstRowNum()).getLastCellNum();
        for (int i = 0; i <= length; i++) {
            toSheet.setColumnWidth(i, fromSheet.getColumnWidth(i));
        }
        for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext(); ) {
            XSSFRow oldRow = (XSSFRow) rowIt.next();
            XSSFRow newRow = toSheet.createRow(oldRow.getRowNum());
            copyRow(wb, oldRow, newRow);
        }
    }

    /**
     * 行复制功能
     * @param wb
     * @param oldRow
     * @param toRow
     */
    private static void copyRow(XSSFWorkbook wb, XSSFRow oldRow, XSSFRow toRow) {
        toRow.setHeight(oldRow.getHeight());
        for (Iterator cellIt = oldRow.cellIterator(); cellIt.hasNext();) {
            XSSFCell tmpCell = (XSSFCell) cellIt.next();
            XSSFCell newCell = toRow.createCell(tmpCell.getColumnIndex(), tmpCell.getCellType());
            copyCell(wb, tmpCell, newCell);
        }
    }

    /**
     * 复制单元格
     * @param wb
     * @param fromCell
     * @param toCell
     */
    private static void copyCell(XSSFWorkbook wb, XSSFCell fromCell, XSSFCell toCell) {
        XSSFCellStyle newstyle = wb.createCellStyle();
        // 复制单元格样式
        newstyle.cloneStyleFrom(fromCell.getCellStyle());
        // 样式
        toCell.setCellStyle(newstyle);
        if (fromCell.getCellComment() != null) {
            toCell.setCellComment(fromCell.getCellComment());
        }
        // 不同数据类型处理
        CellType fromCellType = fromCell.getCellType();
        if (fromCellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(fromCell)) {
                toCell.setCellValue(fromCell.getDateCellValue());
            } else {
                toCell.setCellValue(fromCell.getNumericCellValue());
            }
        } else if (fromCellType == CellType.STRING) {
            toCell.setCellValue(fromCell.getRichStringCellValue());
        } else if (fromCellType == CellType.BLANK) {
            // 空数据不处理
        } else if (fromCellType == CellType.BOOLEAN) {
            toCell.setCellValue(fromCell.getBooleanCellValue());
        } else if (fromCellType == CellType.ERROR) {
            toCell.setCellErrorValue(fromCell.getErrorCellValue());
        } else if (fromCellType == CellType.FORMULA) {
            toCell.setCellFormula(fromCell.getCellFormula());
        } else {
            // 不处理
        }
    }

    /**
     * 解析Excel数据和结构
     * @param wb
     * @return
     */
    private static RSheet parseExcelDataStructure(XSSFWorkbook wb) {
        Sheet sheet = wb.getSheetAt(0);
        List<RRow> headList = new ArrayList<>();
        List<RRow> dataList = new ArrayList<>();

        // 获取头
        List<Map<Integer,String>> headMapList = new ArrayList<>();
        // 默认第一行为头
        Row titleRow = sheet.getRow(0);
        //构建头，用来获取数据的key
        buildHead(sheet, headMapList, titleRow);

        RRow headRow = new RRow();
        for (int i = 0; i < titleRow.getLastCellNum(); i++) {
            RCell cell = PoiUtil.getCell(sheet, titleRow, i);
            if (cell != null) {
                headRow.getCellList().add(cell);
            }
        }
        headList.add(headRow);

        // 获取数据
        Row row;
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            RRow dataRow = new RRow();
            for (int j =0; j < row.getLastCellNum();j++){
                //组件渲染表格的数据集
                RCell cell = PoiUtil.getCell(sheet, row, j);
                if (cell != null) {
                    dataRow.getCellList().add(cell);
                }
            }
            if (dataRow != null) {
                dataList.add(dataRow);
            }
        }
        RSheet rSheetTable = new RSheet();
        rSheetTable.setMaxRowNum(sheet.getLastRowNum());
        rSheetTable.setMaxColNum(titleRow.getLastCellNum());
        rSheetTable.setHeadList(headList);
        rSheetTable.setDataList(dataList);
        return rSheetTable;
    }

    private static void buildHead(Sheet sheet, List<Map<Integer, String>> headMapList, Row title) {
        Map<Integer, String>  head = new HashMap<>();
        for (int i = 0; i< title.getLastCellNum(); i++){
            //判断是否是合并单元格
            boolean mergedRegion = PoiCellUtil.isMergedRegion(sheet, title.getRowNum(), i);
            String value=null;
            if(mergedRegion){
                value= PoiCellUtil.getMergedRegionValue(sheet, title.getRowNum(), i);
            }else {
                value =PoiCellUtil.getCellValue(title.getCell(i));
            }
            if(headMapList.size()==0){
                head.put(i, value);
            }else {
                String preLine = headMapList.get(headMapList.size() - 1).get(i);
                //合并单元格并且不是首行就取上面一行的数据作为表头
                if(mergedRegion&&!PoiUtil.isMergedRegionFirstRow(sheet, title.getRowNum(), i)){
                    head.put(i, preLine);
                }else {
                    //多级表头自动拼上上级表头
                    head.put(i,(StrUtil.isEmpty(preLine)?"": preLine +"_")+ value);
                }
            }
        }
        headMapList.add(head);
    }

}