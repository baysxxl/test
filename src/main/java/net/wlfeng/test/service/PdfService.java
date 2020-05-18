package net.wlfeng.test.service;

import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author weilingfeng
 * @date 2020/5/16 12:00
 * @description
 */
public interface PdfService {

    ResponseEntity<?> exportCommitment(String outFileName, Map<String, Object> dataMap) throws UnsupportedEncodingException;

}
