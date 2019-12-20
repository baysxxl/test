package net.wlfeng.test.sdk;

import com.alibaba.fastjson.JSON;
import net.wlfeng.test.sdk.wx.api.PayApi;
import net.wlfeng.test.sdk.wx.response.UnifiedorderResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author weilingfeng
 * @date 2019/12/5 20:11
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PayTest {

    @Test
    public void payTest() {
        UnifiedorderResponse resp = PayApi.unifiedorder(PayApi.buildRequest("P2019120500000001", 10, "邮费支付", "127.0.0.1", "oglZD********2KM"));
        System.out.println(JSON.toJSONString(resp));
    }
}
