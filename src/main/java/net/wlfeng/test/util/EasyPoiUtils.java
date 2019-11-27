package net.wlfeng.test.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

/**
 * @author weilingfeng
 * @date 2019/10/29 14:55
 * @description EasyPoi工具类
 */
public class EasyPoiUtils {

    /**
     *  导出excel
     * @param pojoClass
     * @param dataSet
     * @param fileName
     * @param response
     * @throws IOException
     */
    public static void exportExcel(Class<?> pojoClass, Collection<?> dataSet, String fileName, HttpServletResponse response) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), pojoClass, dataSet);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }

    /**
     * 导入excel
     * @param file
     * @param pojoClass
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(File file, Class<?> pojoClass, ImportParams params){
        return ExcelImportUtil.importExcel(file, pojoClass, params);
    }
}
