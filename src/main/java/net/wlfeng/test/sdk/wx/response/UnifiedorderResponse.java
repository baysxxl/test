package net.wlfeng.test.sdk.wx.response;

import lombok.Data;

/**
 * @author weilingfeng
 * @date 2019/12/2 20:01
 * @description 统一下单接口响应
 */
@Data
public class UnifiedorderResponse {
    /**
     * 返回状态码，SUCCESS/FAIL
     * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
     */
    private String return_code;
    /**
     * 返回信息,当return_code为FAIL时返回信息为错误原因
     */
    private String return_msg;

    /*以下字段在return_code为SUCCESS的时候有返回*/
    /**
     * 公众账号ID
     */
    private String appid;
    /**
     * 商户号
     */
    private String mch_id;
    /**
     * 设备号
     */
    private String device_info;
    /**
     * 随机字符串
     */
    private String nonce_str;
    /**
     * 签名
     */
    private String sign;
    /**
     * 业务结果,SUCCESS/FAIL
     */
    private String result_code;
    /**
     * 错误代码,当result_code为FAIL时返回错误代码
     */
    private String err_code;
    /**
     * 错误代码描述,当result_code为FAIL时返回错误描述
     */
    private String err_code_des;

    /*以下字段在return_code 和result_code都为SUCCESS的时候有返回*/
    /**
     * 交易类型,
     * JSAPI -JSAPI支付
     * NATIVE -Native支付
     * APP -APP支付
     */
    private String trade_type;
    /**
     * 预支付交易会话标识,微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
     */
    private String prepay_id;
    /**
     * 二维码链接,trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。
     */
    private String code_url;
}
