package net.wlfeng.test.util;

import com.alibaba.fastjson.JSON;
import net.wlfeng.test.dto.AliBankInfoDTO;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author weilingfeng
 * @date 2020/10/13 17:33
 * @description
 */
public class AliBankUtils {

    private static final String QUERY_CARD_INFO_URL = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json";

    private static final String QUERY_BANK_LOGO_URL = "https://apimg.alipay.com/combo.png";

    public static AliBankInfoDTO queryCardBankInfo(String bankNo) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        String url = QUERY_CARD_INFO_URL + "?_input_charset=utf-8&cardBinCheck=true&cardNo=" + bankNo;
        String res = HttpUtils.doSslGet(url);
        return JSON.parseObject(res, AliBankInfoDTO.class);
    }

    public static byte[] queryBankLogo(String bank) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String url = QUERY_BANK_LOGO_URL + "?d=cashier&t=" + bank;
        return HttpUtils.doSslGetBytes(url);
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        byte[] imgBytes = queryBankLogo("NCB");
        System.out.println(imgBytes);
    }

}
