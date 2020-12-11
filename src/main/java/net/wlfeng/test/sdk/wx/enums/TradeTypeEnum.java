package net.wlfeng.test.sdk.wx.enums;

/**
 * @author weilingfeng
 * @date 2019/12/5 19:22
 * @description 微信交易类型枚举
 */
public enum TradeTypeEnum {

    H5("MWEB", "H5支付"),
    JSAPI("JSAPI", "公众号或小程序支付"),
    APP("APP", "app支付"),
    SCAN("NATIVE", "扫码支付"),;

    private String type;
    private String desc;

    TradeTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
