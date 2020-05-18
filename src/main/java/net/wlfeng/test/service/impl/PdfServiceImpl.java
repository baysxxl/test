package net.wlfeng.test.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.service.PdfService;
import net.wlfeng.test.util.PDFUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author weilingfeng
 * @date 2020/5/16 12:01
 * @description
 */
@Slf4j
@Service
public class PdfServiceImpl implements PdfService {


    @Override
    public ResponseEntity<?> exportCommitment(String outFileName, Map<String, Object> dataMap) throws UnsupportedEncodingException {
        ByteArrayOutputStream outputStream = null;
        byte[] result = null;
        try {
            outputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 100, 100, 100, 100);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
            pdfWriter.setViewerPreferences(PdfWriter.PageModeUseThumbs);
            document.open();

            //添加标题
            document.add(getParagraph("承诺函", Element.ALIGN_CENTER, null, 25));

            //添加内容
            document.add(getParagraph("        XXXXXXXX有限公司在XXXX的广告后台账户里所推广的微信个人账号和推广链接都属于XXXXXXXX统一运作管理，" +
                    "承诺不会出现违规操作，承诺不使用XX、XX、XX等专业称谓，如出现此情况，愿接受违规处罚。", Element.ALIGN_LEFT, 80f));

            //添加内容
            document.add(getParagraph("        特此证明", Element.ALIGN_LEFT, 60f));

            //添加公司名
            document.add(getParagraph("XXXXXXXX有限公司", Element.ALIGN_RIGHT, 100f));

            //添加日期
            document.add(getParagraph((String) dataMap.get("date"), Element.ALIGN_RIGHT, 10f));

            document.newPage();// 换页

            //添加附件内容
            document.add(getParagraph("附: 微信个人账号列表", Element.ALIGN_LEFT, null));

            //添加计数
            document.add(getParagraph("共" + dataMap.get("count") + "个", Element.ALIGN_LEFT, null));

            PdfPTable table = new PdfPTable(2);
            table.setLockedWidth(true);
            table.setTotalWidth(400);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setSpacingBefore(6f);

            table.addCell(getCell("微信号昵称"));
            table.addCell(getCell("微信号"));

            List<Map<String, String>> wechatList = (List) dataMap.get("wechatList");
            for (Map<String,String> wechatMap: wechatList) {
                table.addCell(getCell(wechatMap.get("nickName")));
                table.addCell(getCell(wechatMap.get("wechat")));
            }
            document.add(table); //pdf文档中加入table
            document.close();
            result = outputStream.toByteArray();
        }  catch (DocumentException e) {
            log.error("===文档异常,异常信息:{}===", e.getMessage());
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
        return PDFUtils.returnResult(outFileName, result);
    }

    private static PdfPCell getCell(String value) {
        PdfPCell pdfCell = new PdfPCell();
        pdfCell.setMinimumHeight(40);//设置表格行高
        pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfCell.setPhrase(getParagraph(value, Element.ALIGN_CENTER, null));
        return pdfCell;
    }

    private static Paragraph getParagraph(String value, int alignment, Float spacingBefore) {
        return getParagraph(value, alignment, spacingBefore, 14);
    }

    private static Paragraph getParagraph(String value, int alignment, Float spacingBefore, int fontSize) {
        Paragraph paragraph = new Paragraph(value, PDFUtils.getFont(fontSize));
        paragraph.setAlignment(alignment);
        paragraph.setMultipliedLeading(2);
        if (Objects.nonNull(spacingBefore)) {
            paragraph.setSpacingBefore(spacingBefore.floatValue());
        }
        return paragraph;
    }

}
