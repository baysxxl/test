package net.wlfeng.test.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dto.CommonResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author weilingfeng
 * @date 2019/10/21 10:20
 * @description pdf工具类
 */
@Slf4j
public class PDFUtils {

    private volatile static Configuration configuration;

    static {
        if (configuration == null) {
            synchronized (PDFUtils.class) {
                if (configuration == null) {
                    configuration = new Configuration(Configuration.VERSION_2_3_28);
                }
            }
        }
    }

    private PDFUtils(){}

    /**
     * 给定参数导出pdf
     * @param outFileName
     * @param ftlPath
     * @param dataMap
     * @return
     */
    public static ResponseEntity<?> export(String outFileName, String ftlPath, Map<String, Object> dataMap) {
        /**
         * 数据导出(PDF 格式)
         */
        String htmlStr = PDFUtils.freemarkerRender(dataMap, ftlPath);
        return  exportHtml(outFileName, htmlStr);
    }

    /**
     * 给定html页面字符串导出pdf
     * @param outFileName
     * @param html
     * @return
     */
    public static ResponseEntity<?> exportHtml(String outFileName, String html) {
        HttpHeaders headers = new HttpHeaders();
        /**
         * 数据导出(PDF 格式)
         */
        byte[] pdfBytes = PDFUtils.createPDF(html);
        if (pdfBytes != null && pdfBytes.length > 0) {
            String fileName = outFileName + ".pdf";
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);
        }
        return returnFailed(HttpStatus.NOT_FOUND);
    }

    /**
     * freemarker 引擎渲染 html
     *
     * @param dataMap 传入 html 模板的 Map 数据
     * @param ftlFilePath html 模板文件相对路径(相对于 resources路径,路径 + 文件名)
     *                    例如: "templates/pdf_export_demo.ftl"
     * @return
     */
    public static String freemarkerRender(Map<String, Object> dataMap, String ftlFilePath) {
        Writer out = new StringWriter();
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        try {
            configuration.setDirectoryForTemplateLoading(new File(ResourceFileUtils.getParent(ftlFilePath)));
            configuration.setLogTemplateExceptions(false);
            configuration.setWrapUncheckedExceptions(true);
            Template template = configuration.getTemplate(ResourceFileUtils.getFileName(ftlFilePath));
            template.process(dataMap, out);
            out.flush();
            return out.toString();
        } catch (IOException e) {
            log.error("===IO异常,异常信息:{}===", e.getMessage());
        } catch (TemplateException e) {
            log.error("===模板异常,异常信息:{}===", e.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error("===IO异常-Writer关闭失败,异常信息:{}===", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 使用 iText 生成 PDF 文档
     *
     * @param htmlTmpStr html 模板文件字符串
     * */
    public static byte[] createPDF(String htmlTmpStr) {
        ByteArrayOutputStream outputStream = null;
        byte[] result = null;
        try {
            outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
            document.open();
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            worker.parseXHtml(pdfWriter, document, new ByteArrayInputStream(htmlTmpStr.getBytes()),
                    Charset.forName("UTF-8"), new AsianFontProvider());
            document.close();
            result = outputStream.toByteArray();
        }  catch (DocumentException e) {
            log.error("===文档异常,异常信息:{}===", e.getMessage());
        } catch (IOException e) {
            log.error("===IO异常,异常信息:{}===", e.getMessage());
        } finally {
            if(outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    log.error("===IO异常-ByteArrayOutputStream刷新关闭失败,异常信息:{}===", e.getMessage());
                }
            }
        }
        return result;
    }

    public static ResponseEntity<CommonResponse> returnFailed(HttpStatus httpStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<CommonResponse>(CommonResponse.fail(httpStatus.value(), httpStatus.getReasonPhrase()),
                headers, httpStatus);
    }
}

/**
 * 用于中文显示的Provider
 */
@Slf4j
class AsianFontProvider extends XMLWorkerFontProvider {
    @Override
    public Font getFont(final String fontname, String encoding, float size, final int style) {
        try {
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            return new Font(bfChinese, size, style);
        } catch (Exception e) {
            log.error("===获取中文字体异常,异常信息:{}===", e.getMessage());
        }
        return super.getFont(fontname, encoding, size, style);
    }
}