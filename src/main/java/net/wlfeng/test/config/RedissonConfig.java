package net.wlfeng.test.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RedissonConfig {

    @Bean
    public RedissonAutoConfigurationCustomizer redissonAutoConfigurationCustomizer() {
        return configuration -> {
            if (StringUtils.isEmpty(configuration.useSingleServer().getPassword())) {
                // 解决配置文件中密码为空导致redisson启动报错问题
                configuration.useSingleServer().setPassword(null);
            }
        };
    }

}
