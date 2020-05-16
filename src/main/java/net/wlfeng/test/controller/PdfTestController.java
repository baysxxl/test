package net.wlfeng.test.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.config.PDFConfig;
import net.wlfeng.test.dal.domain.User;
import net.wlfeng.test.service.PdfService;
import net.wlfeng.test.service.UserService;
import net.wlfeng.test.util.EntityUtils;
import net.wlfeng.test.util.PDFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author weilingfeng
 * @date 2019/11/27 19:03
 * @description pdf测试controller
 */
@Slf4j
@RestController
@RequestMapping("pdf")
public class PdfTestController {

    @Autowired
    private PDFConfig pdfConfig;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "export", method = RequestMethod.GET, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity export(@RequestParam("type") Integer type, @RequestParam("id") Integer id) {
        if (Integer.valueOf(1).equals(type)) {
            User user = userService.selectOne(new EntityWrapper<User>().eq("id", id));
            return exportOne(user);
        } else {
            List<User> userList = userService.selectList(new EntityWrapper<>());
            return exportList(userList);
        }
    }

    private ResponseEntity exportOne(User user) {
        log.info("导出单个用户,用户id:{}", user.getId());
        try {
            String outFileName = user.getName() + "-" + System.currentTimeMillis();
            return PDFUtils.export(outFileName, pdfConfig.getFtlPath(), EntityUtils.entityToMap(user));
        } catch (Exception e) {
            log.error("===导出单个用户异常,异常信息:{}===", e.getMessage());
        }
        return PDFUtils.returnFailed(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity exportList(List<User> userList) {
        log.info("导出多个用户,用户数量:{}", userList.size());
        try {
            String outFileName = userList.size() + "-" + System.currentTimeMillis();
            StringBuilder result = new StringBuilder();
            for (User user: userList) {
                String documentHtmlStr = PDFUtils.freemarkerRender(EntityUtils.entityToMap(user), pdfConfig.getFtlPath());
                result.append(documentHtmlStr);
            }
            return PDFUtils.exportHtml(outFileName, result.toString());
        } catch (Exception e) {
            log.error("===导出多个用户异常,异常信息:{}===", e.getMessage());
        }
        return PDFUtils.returnFailed(HttpStatus.NOT_FOUND);
    }


}
