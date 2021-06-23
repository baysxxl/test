package net.wlfeng.test.util;

import com.baidu.aip.ocr.AipOcr;
import net.wlfeng.test.dto.CommonResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;

/**
 * @author weilingfeng
 * @date 2020/11/18 10:30
 * @description 百度ocr接口工具
 */
@Configuration
public class BaiduOcrUtils {

    public static final String APP_ID = "23044762";

    public static final String API_KEY = "0QkNpYiISoCLlO5KWTocrvox";

    public static final String SECRET_KEY = "Wab34UU80OyvIPgGEsgQkVpxjdCZCRxA";

    /**
     * 身份证图片识别
     * @param fileResource 如果是网络图片之间传文件url地址，如果是本地图片传文件base64编码字符串
     * @param isFace 正反面，正面传true
     * @return
     */
    public static CommonResponse<AliOcrIdCardResultDTO> idCardOcr(String fileResource, boolean isFace) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        // client.setConnectionTimeoutInMillis(2000);
        // client.setSocketTimeoutInMillis(60000);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");

        String idCardSide = isFace ? "front" : "back";
        try {
            JSONObject res = client.idcard(fileResource, idCardSide, options);
            System.out.println(res.toString(2));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResponse.fail("身份信息识别失败");
    }

    public static void main(String[] args) {
        idCardOcr("D:\\微信图片_20201118170909.jpg", true);
    }

}