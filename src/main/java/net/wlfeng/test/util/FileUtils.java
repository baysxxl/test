package net.wlfeng.test.util;

import org.apache.commons.codec.binary.Base64;

import java.io.*;

/**
 * @author weilingfeng
 * @date 2020/11/10 9:28
 * @description
 */
public class FileUtils {

    /**
     * 将文本文件中的内容读入到buffer中
     * @param filePath 文件路径
     * @return
     */
    public static StringBuffer readToBuffer(String filePath) throws IOException {
        StringBuffer buffer = new StringBuffer();
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        return buffer;
    }

    /**
     * 读取文本文件内容
     * @param filePath 文件所在路径
     * @return
     */
    public static String readFile(String filePath) throws IOException {
        return FileUtils.readToBuffer(filePath).toString();
    }

    /**
     * 读取文件内容返回byte数组
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] getFileBytes(String filePath) throws IOException {

        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    /**
     * 将文件字节数组转换为base64字符串
     * @param fileBytes 文件字节数组
     * @return
     */
    public static String fileBytesToBase64(byte[] fileBytes) {
        return Base64.encodeBase64String(fileBytes);
    }

    public static void main(String[] args) {
        try {
            System.out.println(getFileBytes("D:\\config\\wx\\apiclient_cert_suyi_jsapi.p12"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
