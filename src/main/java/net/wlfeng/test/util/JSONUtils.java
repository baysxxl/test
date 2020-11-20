package net.wlfeng.test.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

/**
 * @author weilingfeng
 * @date 2020/9/22 17:21
 * @description
 */
public class JSONUtils {

    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String file = "C:\\Users\\86176\\Desktop\\bankInfo-master\\银行信息\\bank.json";
        JSONObject json = JSON.parseObject(JSONUtils.readJsonFile(file));
        System.out.println(json);
    }

}
