package net.wlfeng.test.sdk.wx.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.sdk.wx.sdk.IWXPayDomain;
import net.wlfeng.test.sdk.wx.sdk.WXPayConfig;
import net.wlfeng.test.sdk.wx.sdk.WXPayConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author weilingfeng
 * @date 2019/12/5 19:32
 * @description
 */
@Slf4j
@Setter
@Component
public class IWxPayConfig extends WXPayConfig {

    @Value("${pay.wechat.appId}")
    private String appId;

    @Value("${pay.wechat.mchId}")
    private String mchId;

    @Value("${pay.wechat.key}")
    private String key;

    @Value("${pay.wechat.certPath}")
    private String certPath;

    @Value("${pay.wechat.notifyUrl}")
    private String notifyUrl;

    @Override
    public String getAppID() {
        return this.appId;
    }

    @Override
    public String getMchID() {
        return this.mchId;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public InputStream getCertStream() {
        try {
            File file = ResourceUtils.getFile(certPath);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error("微信支付证书加载失败！", e);
        }
        return null;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }
            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }
}
