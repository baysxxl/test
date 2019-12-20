package net.wlfeng.test.sdk.wx.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weilingfeng
 * @date 2019/12/2 19:42
 * @description 统一下单请求类
 */
@Data
public class UnifiedorderRequest {

    /**
     * 公众账号ID
     */
    @NotBlank
    private String appid;
    /**
     * 商户号
     */
    @NotBlank
    private String mch_id;
    /**
     * 设备号,自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
     */
    private String device_info;
    /**
     * 随机字符串
     */
    @NotBlank
    private String nonce_str;
    /**
     * 签名
     */
    @NotBlank
    private String sign;
    /**
     * 签名类型,默认为MD5，支持HMAC-SHA256和MD5。
     */
    private String sign_type;
    /**
     * 商品描述
     */
    @NotBlank
    private String body;
    /**
     * 商品详情
     */
    private String detail;
    /**
     * 附加数据
     */
    private String attach;
    /**
     * 商户订单号
     */
    @NotBlank
    private String out_trade_no;
    /**
     * 标价币种,默认人民币：CNY
     */
    private String fee_type;
    /**
     * 标价金额,订单总金额，单位为分
     */
    @NotNull
    private Integer total_fee;
    /**
     * 终端IP,支持IPV4和IPV6两种格式的IP地址。用户的客户端IP
     */
    @NotBlank
    private String spbill_create_ip;
    /**
     * 交易起始时间,订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
     */
    private String time_start;
    /**
     * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010<br/>
     * 订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id<br/>
     * time_expire只能第一次下单传值，不允许二次修改，二次修改系统将报错。如用户支付失败后，需再次支付，需更换原订单号重新下单。
     */
    private String time_expire;
    /**
     * 订单优惠标记
     */
    private String goods_tag;
    /**
     * 通知地址
     */
    @NotBlank
    private String notify_url;
    /**
     * 交易类型,
     * JSAPI -JSAPI支付
     * NATIVE -Native支付
     * APP -APP支付
     */
    @NotBlank
    private String trade_type;
    /**
     * 商品ID,trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义
     */
    private String product_id;
    /**
     * 指定支付方式,上传此参数no_credit--可限制用户不能使用信用卡支付
     */
    private String limit_pay;
    /**
     * 用户标识,trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识
     */
    private String openid;
    /**
     * 电子发票入口开放标识,传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效
     */
    private String receipt;
    /**
     * 场景信息,该字段常用于线下活动时的场景信息上报，支持上报实际门店信息，商户也可以按需求自己上报相关信息。
     * 该字段为JSON对象数据，对象格式为{"store_info":{"id": "门店ID","name": "名称","area_code": "编码","address": "地址" }}
     */
    private String scene_info;
}
