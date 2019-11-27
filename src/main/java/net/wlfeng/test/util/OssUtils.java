package net.wlfeng.test.util;

import java.math.BigDecimal;

/**
 * @author weilingfeng
 * @date 2019/10/21 10:20
 * @description oss工具类
 */
public class OssUtils {

	/**
     * byte(字节)根据长度转成KB(千字节)、MB(兆字节)
     *  
     * @param bytes 
     * @return 
     */  
    public static String bytesTokbOrmb(long bytes) {
        int rate = 1024;// 进制比率
        int scale = 2;// 小数位数
        BigDecimal size = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(rate * rate);
        float returnValue = size.divide(megabyte, scale, BigDecimal.ROUND_UP)
                .floatValue();  
        if (returnValue > 1) {
            return (returnValue + "MB");
        }
        BigDecimal kilobyte = new BigDecimal(rate);
        returnValue = size.divide(kilobyte, scale, BigDecimal.ROUND_UP)
                .floatValue();  
        return (returnValue + "KB");  
    }
    
}
