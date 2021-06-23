package net.wlfeng.test.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 通用脱敏工具类
 * 可用于：
 * 用户名
 * 手机号
 * 邮箱
 * 地址等
 */
public class DesensitizationUtil {

    private static final String SYMBOL = "*";

    /**
     * 字符串打码隐藏加*
     * @param str 字符串
     * @param prefix 需显示前缀长度
     * @param suffix 需显示后缀长度
     * @return 处理完成的字符串,参数异常直接返回null
     */
    public static String mask(String str, int prefix, int suffix) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        //需要截取的长度不能大于字符串长度
        if ((prefix + suffix) > str.length()) {
            return null;
        }
        //需要截取的不能小于0
        if (prefix < 0 || suffix < 0) {
            return null;
        }
        //计算*的数量
        StringBuffer asteriskStr = new StringBuffer();
        for (int i = 0; i < str.length() - (prefix + suffix); i++) {
            asteriskStr.append(SYMBOL);
        }
        String regex = "(\\w{" + prefix + "})(\\w+)(\\w{" + suffix + "})";
        return str.replaceAll(regex, "$1" + asteriskStr + "$3");
    }

    public static void main(String[] args) {
        System.out.println(mask("12312320000101123X", 6, 4));
    }

}