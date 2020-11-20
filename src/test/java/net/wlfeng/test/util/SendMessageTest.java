package net.wlfeng.test.util;

import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author weilingfeng
 * @date 2020/11/18 16:49
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SendMessageTest {

    @Test
    public void sendShortMessageTest() {
        System.out.println(TestConfig.SHORT_MSG_MOCK);
        if (TestConfig.SHORT_MSG_MOCK) {
            System.out.println(TestConfig.MOCK_MOBILE);
        }
    }

}
