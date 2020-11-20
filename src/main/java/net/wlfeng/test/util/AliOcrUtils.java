package net.wlfeng.test.util;

import com.alibaba.fastjson.JSONObject;
import net.wlfeng.test.dto.CommonResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weilingfeng
 * @date 2020/11/18 10:30
 * @description 阿里ocr接口工具
 */
@Configuration
public class AliOcrUtils {

    private static final String OCR_REQUEST_URL = "https://dm-51.data.aliyun.com/rest/160601/ocr/ocr_idcard.json";

    private static String APPCODE;

    /**
     * 身份证图片识别
     * @param fileResource 如果是网络图片之间传文件url地址，如果是本地图片传文件base64编码字符串
     * @param isFace 正反面，正面传true
     * @return
     */
    public static CommonResponse<AliOcrIdCardResultDTO> idCardOcr(String fileResource, boolean isFace) {
        // 设置请求头
        Map<String, String> headers = new HashMap();
        headers.put("Authorization", "APPCODE " + APPCODE);
        headers.put("Content-Type", "application/json; charset=UTF-8");

        // configure配置
        JSONObject configObj = new JSONObject();
        configObj.put("side", isFace ? "face" : "back");

        // 拼装请求body的json字符串
        JSONObject param = new JSONObject();
        param.put("image", fileResource);
        param.put("configure", configObj);
        try {
            CloseableHttpResponse response = HttpUtil.doPost(OCR_REQUEST_URL, headers, param.toString());
            if (response.getStatusLine().getStatusCode() == 200) {
                AliOcrIdCardResultDTO resultDTO = AliOcrIdCardResultDTO.buildEntity(EntityUtils.toString(response.getEntity()), isFace);
                if (resultDTO.getSuccess()) {
                    return CommonResponse.success(resultDTO);
                }
                return CommonResponse.fail("身份信息识别失败");
            } else if (response.getStatusLine().getStatusCode() == 463) {
                return CommonResponse.fail("身份信息识别失败,请核实后重新上传");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResponse.fail("身份信息识别失败");
    }

    @Value("${ali.ocr.appCode}")
    private void setAppcode(String appCode) {
        this.APPCODE = appCode;
    }

}

class AliOcrIdCardResultDTO {

    /**
     * 阿里业务请求id
     */
    private String requestId;

    /**
     * 认证成功标识
     */
    private Boolean success;

    /*======正面信息======*/
    /**
     * 住址
     */
    private String address;

    /**
     * 生日
     */
    private Date birth;

    /**
     * 姓名
     */
    private String name;

    /**
     * 民族
     */
    private String nationality;

    /**
     * 证身份证号
     */
    private String num;

    /**
     * 性别
     */
    private String sex;

    /*======背面信息======*/
    /**
     * 签发机关
     */
    private String issue;

    /**
     * 证件有效期-起始时间
     */
    private Date startDate;

    /**
     * 证件有效期-结束时间
     */
    private Date endDate;

    public static AliOcrIdCardResultDTO buildEntity(String idCardInfo, boolean isFace) {
        JSONObject idCardInfoJson = JSONObject.parseObject(idCardInfo);
        AliOcrIdCardResultDTO resultDTO = new AliOcrIdCardResultDTO();
        resultDTO.setRequestId("request_id");
        resultDTO.setSuccess(idCardInfoJson.getBoolean("success"));
        if (idCardInfoJson.getBoolean("success")) {
            if (isFace) {
                resultDTO.setAddress(idCardInfoJson.getString("address"));
                resultDTO.setBirth(DateUtil.convert2Date(idCardInfoJson.getString("birth"), "yyyyMMdd"));
                resultDTO.setName(idCardInfoJson.getString("name"));
                resultDTO.setNationality(idCardInfoJson.getString("nationality"));
                resultDTO.setNum(idCardInfoJson.getString("num"));
                resultDTO.setSex(idCardInfoJson.getString("sex"));
            } else {
                resultDTO.setIssue(idCardInfoJson.getString("issue"));
                resultDTO.setStartDate(DateUtil.convert2Date(idCardInfoJson.getString("start_date"), "yyyyMMdd"));
                resultDTO.setEndDate(DateUtil.convert2Date(idCardInfoJson.getString("end_date"), "yyyyMMdd"));
            }
        }
        return resultDTO;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}