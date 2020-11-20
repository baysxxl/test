package net.wlfeng.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author weilingfeng
 * @date 2020/11/20 09:38
 * @description 测试环境配置
 */
@Configuration
public class TestConfig {

    public static boolean SHORT_MSG_MOCK;

    public static String MOCK_MOBILE;

    @Value("${test.shortMsg.sendMock:false}")
    public void setShortMsgMock(Boolean shortMsgMock) {
        this.SHORT_MSG_MOCK = shortMsgMock;
    }

    @Value("${test.shortMsg.mockMobile:}")
    public void setMockMobile(String mockMobile) {
        this.MOCK_MOBILE = mockMobile;
    }

}
