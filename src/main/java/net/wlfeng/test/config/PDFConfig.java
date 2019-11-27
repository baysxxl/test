package net.wlfeng.test.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author weilingfeng
 * @date 2019/10/21 10:25
 * @description
 */
@Configuration
@Data
public class PDFConfig {

    @Value("${pdf.ftlPath}")
    private String ftlPath;
}
