package net.wlfeng.test.config;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OssConfig {
	
	@Value("${oss.accessKey}")
	private String accessKey;
	
	@Value("${oss.secretKey}")
	private String secretKey;
	
	/**
     * 配置信息
	 * @return
	 */
	@Bean
	public com.qiniu.storage.Configuration configuration() {
		// 华东机房
		return new com.qiniu.storage.Configuration(Region.region0());
	}
	
	/**
     * 认证信息实例
     * @return
     */
    @Bean
    public Auth auth() {
        return Auth.create(accessKey, secretKey);
    }

    /**
     *  构建七牛空间管理实例
     */
    @Bean
    public BucketManager bucketManager() {
        return new BucketManager(auth(), configuration());
    }
    
    /**
     * 构建一个七牛上传工具实例
     */
    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(configuration());
    }
}
