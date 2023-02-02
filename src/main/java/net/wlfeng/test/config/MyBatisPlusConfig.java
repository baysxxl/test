package net.wlfeng.test.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
// import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("net.wlfeng.test.dal")
public class MyBatisPlusConfig {
	
	/*	旧版本配置,3.4.0版本后改用了MybatisPlusInterceptor
	@Bean
	public PaginationInterceptor paginationInterceptor(){
		return new PaginationInterceptor();
	}*/

	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
		dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
			System.out.println(tableName);
			// 获取参数方法
//			Map<String, Object> paramMap = RequestDataHelper.getRequestData();
//			paramMap.forEach((k, v) -> System.err.println(k + "----" + v));
			return tableName;
		});
		interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
		/**
		 * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题（该属性会在旧插件移除后一同移除）
		 */
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}

	/**
	 * 旧分页移除后就没这个属性了
	 */
//	@Bean
//	public ConfigurationCustomizer configurationCustomizer() {
//		return configuration -> configuration.setUseDeprecatedExecutor(false);
//	}

}
