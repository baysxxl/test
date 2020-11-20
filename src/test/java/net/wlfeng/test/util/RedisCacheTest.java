package net.wlfeng.test.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author weilingfeng
 * @date 2020/11/20 11:27
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisCacheTest {

    @Test
    public void cacheTest() {
        CacheUtil.cacheByMinutes("test", "1", 10);
        System.out.println(CacheUtil.getString("test"));
    }

}
