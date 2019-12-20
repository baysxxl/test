package net.wlfeng.test.sdk.wx.config;

import net.wlfeng.test.sdk.wx.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weilingfeng
 * @date 2019/12/5 19:53
 * @description
 */
@Configuration
public class WxPayConfiguration {

    @Autowired
    private IWxPayConfig iWxPayConfig;

    @Bean
    public WXPay wxPay() throws Exception {
        return new WXPay(iWxPayConfig, iWxPayConfig.getNotifyUrl());
    }
}
