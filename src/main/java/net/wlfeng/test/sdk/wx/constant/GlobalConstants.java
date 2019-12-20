package net.wlfeng.test.sdk.wx.constant;

/**
 * @author weilingfeng
 * @date 2019/12/5 19:40
 * @description
 */
public class GlobalConstants {

    public static class TradeState {
        public static final String TRADE_STATE_SUCCESS = "SUCCESS"; // 支付成功
        public static final String TRADE_STATE_REFUND = "REFUND"; // 转入退款
        public static final String TRADE_STATE_NOTPAY = "NOTPAY"; // 未支付
        public static final String TRADE_STATE_CLOSED = "CLOSED"; // 已关闭
        public static final String TRADE_STATE_REVOKED = "REVOKED"; // 已撤销（刷卡支付）
        public static final String TRADE_STATE_USERPAYING = "USERPAYING"; // 用户支付中
        public static final String TRADE_STATE_PAYERROR = "PAYERROR"; // 支付失败(其他原因，如银行返回失败)
    }

    public static class RequestReturnCode {
        public static final String SUCCESS = "SUCCESS"; // 下单请求成功
        public static final String FAIL = "FAIL"; // 下单请求失败
    }

    public static class RequestResultCode {
        public static final String SUCCESS = "SUCCESS"; // 下单成功
        public static final String FAIL = "FAIL"; // 下单失败
    }
}
