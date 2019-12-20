package net.wlfeng.test.sdk.wx.api;

import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.sdk.wx.request.UnifiedorderRequest;
import net.wlfeng.test.sdk.wx.response.UnifiedorderResponse;
import net.wlfeng.test.sdk.wx.sdk.WXPay;
import net.wlfeng.test.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author weilingfeng
 * @date 2019/12/5 19:21
 * @description 微信支付api
 */
@Slf4j
@Component
public class PayApi {

    private static WXPay wxPay;

    /**
     * 统一下单接口
     * @return
     */
    public static UnifiedorderResponse unifiedorder(UnifiedorderRequest request) {
        try {
            Map<String, String> unifiedOrderRes = wxPay.unifiedOrder(EntityUtils.entityToMap(request));
            UnifiedorderResponse resp = EntityUtils.mapToEntity(unifiedOrderRes, new TypeReference<UnifiedorderResponse>(){});
            return resp;
        } catch (Exception e) {
            log.info("===微信统一下单异常,异常信息:{}===", e.getMessage());
            return null;
        }
    }

    public static UnifiedorderRequest buildRequest(String orderNumber, Integer totalFee, String body, String userIp, String openId) {
        UnifiedorderRequest request = new UnifiedorderRequest();
        request.setOut_trade_no(orderNumber);
        request.setTotal_fee(totalFee);
        request.setBody(body);
        request.setSpbill_create_ip(userIp);
        request.setOpenid(openId);
        request.setTrade_type("JSAPI");
        return request;
    }

    public static Map<String, String> buildJsApiStr(String prepayId) {
        Map<String, String> result = null;
        try {
            result = wxPay.buildJsApiStr(prepayId);
        } catch (Exception e) {
            log.info("===微信支付生成支付参数异常,异常信息:{}===", e.getMessage());
            return null;
        }
        return result;
    }

    @Autowired
    public void setWxPay(WXPay wxPay) {
        this.wxPay = wxPay;
    }
}
