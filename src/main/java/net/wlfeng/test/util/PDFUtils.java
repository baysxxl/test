package net.wlfeng.test.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dto.CommonResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.net.URLEncoder;
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
    public static ResponseEntity<?> export(String outFileName, String ftlPath, Map<String, String> dataMap) throws IOException, TemplateException, DocumentException {
        String htmlStr = PDFUtils.freemarkerRender(dataMap, ftlPath);
        return  exportHtml(outFileName, htmlStr);
    }

    /**
     * 给定html页面字符串导出pdf
     * @param outFileName
     * @param html
     * @return
     */
    public static ResponseEntity<?> exportHtml(String outFileName, String html) throws IOException, DocumentException {
        byte[] pdfBytes = PDFUtils.createPDF(html);
        return returnResult(outFileName, pdfBytes);
    }

    /**
     * freemarker 引擎渲染,主要做参数填充
     *
     * @param dataMap 传入模板的 Map 数据
     * @param ftlFilePath 模板文件相对路径(相对于resources路径,路径 + 文件名)
     *                    例如: "templates/pdf_export_demo.ftl"
     * @return 渲染后的文件字符串
     */
    public static String freemarkerRender(Map<String, String> dataMap, String ftlFilePath) throws TemplateException, IOException {
        Writer out = new StringWriter();
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        try {
            configuration.setClassForTemplateLoading(new PDFUtils().getClass(), "/");
            configuration.setLogTemplateExceptions(false);
            configuration.setWrapUncheckedExceptions(true);
            Template template = configuration.getTemplate(ftlFilePath);
            template.process(dataMap, out);
            out.flush();
            return out.toString();
        } catch (IOException e) {
            log.error("===IO异常,异常信息:{}===", e.getMessage());
            throw e;
        } catch (TemplateException e) {
            log.error("===模板异常,异常信息:{}===", e.getMessage());
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error("===IO异常-Writer关闭失败,异常信息:{}===", e.getMessage());
            }
        }
    }

    /**
     * 使用 iText 生成 PDF 文档
     *
     * @param htmlStr html文件字符串
     * */
    public static byte[] createPDF(String htmlStr) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = null;
        byte[] result = null;
        try {
            outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
            document.open();
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            worker.parseXHtml(pdfWriter, document, new ByteArrayInputStream(htmlStr.getBytes()),
                    Charset.forName("UTF-8"), new AsianFontProvider());
            document.close();
            result = outputStream.toByteArray();
        }  catch (DocumentException e) {
            log.error("===文档异常,异常信息:{}===", e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error("===IO异常,异常信息:{}===", e.getMessage());
            throw e;
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
        return returnFailed(httpStatus, CommonResponse.fail(httpStatus.value(), httpStatus.getReasonPhrase()));
    }

    public static ResponseEntity<CommonResponse> returnFailed(HttpStatus httpStatus, CommonResponse result) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(result, headers, httpStatus);
    }

    public static ResponseEntity returnResult(String outFileName, byte[] result) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        if (result != null && result.length > 0) {
            String fileName = URLEncoder.encode(outFileName, "UTF-8") + ".pdf";
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity(result, headers, HttpStatus.OK);
        }
        return PDFUtils.returnFailed(HttpStatus.NOT_FOUND);
    }

    public static Font getFont(Integer size) {
        return new AsianFontProvider().getFont("STSongStd-Light", "UniGB-UCS2-H", size);
    }

    public static void base64StringToPdf(String base64Content,String filePath){
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            byte[] bytes = Base64.decode(base64Content);//base64编码内容转换为字节数组
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            File path = file.getParentFile();
            if(!path.exists()){
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while(length != -1){
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (bis != null){
                    bis.close();
                }
                if (fos != null){
                    fos.close();
                }
                if (bos != null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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