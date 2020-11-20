package net.wlfeng.test.controller;

import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dto.FileDTO;
import net.wlfeng.test.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * oss控制器
 * @author 86176
 *
 */
@Slf4j
@Controller
@RequestMapping("oss")
public class OssController {
	
	@Autowired
	private OssService ossService;
	
	@ResponseBody
	@GetMapping("list")
	public List<FileDTO> list(@RequestParam("filePrefix") String filePrefix,
							  @RequestParam("delimiter") String delimiter,
							  @RequestParam("limit") Integer limit) {
		log.info("获取oss文件列表，请求参数:{},{},{}", filePrefix, delimiter, limit);
		return ossService.list(filePrefix, delimiter, limit);
	}

	@ResponseBody
	@GetMapping("get")
	public FileDTO get(@RequestParam("fileName") String fileName) throws QiniuException {
		log.info("获取oss文件，请求参数:{}", fileName);
		return ossService.get(fileName);
	}

}
