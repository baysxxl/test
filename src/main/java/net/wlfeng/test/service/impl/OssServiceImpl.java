package net.wlfeng.test.service.impl;

import com.alibaba.fastjson.JSON;
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
		String upToken = auth.uploadToken(bucket);
		try {
		    Response response = uploadManager.put(filePath, fileName, upToken);
		    //解析上传成功的结果
		    DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);
		    log.info(JSON.toJSONString(putRet));
			log.info(response.bodyString());
		} catch (QiniuException ex) {
		    Response r = ex.response;
		    log.error(r.toString());
		}
	}

	@Override
	public void upload(byte[] data, String fileName) {
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String upToken = auth.uploadToken(bucket);
		try {
			Response response = uploadManager.put(data, fileName, upToken);
			//解析上传成功的结果
			DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);
			log.info(JSON.toJSONString(putRet));
			log.info(response.bodyString());
		} catch (QiniuException ex) {
			Response r = ex.response;
			log.error(r.toString());
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

	@Override
	public FileDTO get(String fileName) {
		FileDTO fileDto = null;
		try {
			FileInfo fileInfo = bucketManager.stat(bucket, fileName);
			fileDto = new FileDTO();
			fileDto.setFileName(fileName);
			fileDto.setUrl(domain + fileName);
			fileDto.setFileSize(OssUtils.bytesTokbOrmb(fileInfo.fsize));
		} catch (QiniuException e) {
			log.error(e.getMessage());
		}
		return fileDto;
	}

}
