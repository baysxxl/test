package net.wlfeng.test.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author weilingfeng
 * @date 2020/11/18 16:49
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AliOcrUtilsTest {

    @Test
    public void idCodeOcrTest() {
        try {
            System.out.println(JSON.toJSONString(AliOcrUtils.idCardOcr(FileUtils.fileBytesToBase64(FileUtils.getFileBytes("D:\\微信图片_20201118170909.jpg")), true)));
            // System.out.println(JSON.toJSONString(AliOcrUtils.idCardOcr(FileUtils.fileBytesToBase64(FileUtils.getFileBytes("D:\\id_card_back.jpg")), false)));
            /*System.out.println(idCardOcr("http://qiniu.public.weilingfeng.com/id_card_face.jpg", true));
            System.out.println(idCardOcr("http://qiniu.public.weilingfeng.com/id_card_back.jpg", false));*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
