package net.wlfeng.test.sdk.wx.enums;

import net.wlfeng.test.exception.BizException;

/**
 * @author weilingfeng
 * @date 2020/12/11 10:02
 * @description 微信支付子渠道code枚举
 */
public enum WxPaySubChannelCodeEnum {

    OFFICIAL("official", "公众号支付", true),
    APP("app", "app支付", false),
    MINI("mini", "小程序支付", true),
    SCAN("scan", "扫码支付", false);

    private String code;
    private String name;
    private Boolean needOpenId;

    WxPaySubChannelCodeEnum(String code, String name, boolean needOpenId) {
        this.code = code;
        this.name = name;
        this.needOpenId = needOpenId;
    }

    public static Boolean subChannelNeedOpenId(String code) {
        for (WxPaySubChannelCodeEnum e : WxPaySubChannelCodeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getNeedOpenId();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNeedOpenId() {
        return needOpenId;
    }

    public void setNeedOpenId(Boolean needOpenId) {
        this.needOpenId = needOpenId;
    }

}
