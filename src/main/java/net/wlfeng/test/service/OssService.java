package net.wlfeng.test.service;

import com.qiniu.common.QiniuException;
import net.wlfeng.test.dto.FileDTO;

import java.util.List;

/**
 * oss服务
 * @author 86176
 *
 */
public interface OssService {
	
	/**
	 * 上传文件
	 * @param filePath 上传的文件路径
	 * @param fileName 上传后的文件名
	 */
	void upload(String filePath, String fileName);

	/**
	 * 上传文件
	 * @param data 上传的文件字节码
	 * @param fileName 上传后的文件名
	 */
	void upload(byte[] data, String fileName);
	
	/**
	 * 获取文件列表
	 * @param filePrefix 文件名前缀,，无需匹配则传空字符串
	 * @param delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
	 * @param limit 文件列表长度限制，最大1000，推荐值 1000
	 * @return
	 */
	List<FileDTO> list(String filePrefix, String delimiter, Integer limit);

	/**
	 * 获取文件
	 * @param fileName 文件名
	 * @return
	 */
	FileDTO get(String fileName);

}
