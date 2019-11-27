package net.wlfeng.test.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dto.FileDTO;
import net.wlfeng.test.service.OssService;
import net.wlfeng.test.util.OssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OssServiceImpl implements OssService {
	
	@Autowired
	private BucketManager bucketManager;
	
	@Autowired
	private Auth auth;
	
	@Autowired
	private UploadManager uploadManager;
	
	@Value("${oss.domain}")
	private String domain;
	
	@Value("${oss.bucket}")
	private String bucket;

	@Override
 	public void upload(String filePath, String fileName) {
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = null;
		String upToken = auth.uploadToken(bucket);
		try {
		    Response response = uploadManager.put(filePath, key, upToken);
		    //解析上传成功的结果
		    DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);
		    System.out.println(putRet);
		    System.out.println(response.bodyString());
		} catch (QiniuException ex) {
		    Response r = ex.response;
		    System.err.println(r.toString());
		}
		
	}

	@Override
	public List<FileDTO> list(String filePrefix, String delimiter, Integer limit) {
		List<FileDTO> fileList = new ArrayList<FileDTO>();
		//列举空间文件列表
		BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, filePrefix, limit, delimiter);
		while (fileListIterator.hasNext()) {
		    //处理获取的file list结果
		    FileInfo[] items = fileListIterator.next();
		    for (FileInfo item : items) {
				FileDTO fileDto = new FileDTO();
		        fileDto.setFileName(item.key);
		        fileDto.setUrl(domain + item.key);
		        fileDto.setFileSize(OssUtils.bytesTokbOrmb(item.fsize));
		        fileList.add(fileDto);
		    }
		}
		return fileList;
	}
	
}
