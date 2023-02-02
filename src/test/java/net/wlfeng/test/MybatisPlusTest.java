package net.wlfeng.test;

import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dal.dao.UserMapper;
import net.wlfeng.test.dal.domain.User;
import net.wlfeng.test.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author weilingfeng
 * @date 2020/3/4 11:03
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void idTest() {
        log.info("===service insert test===");
        User user1 = new User();
        user1.setAge(18);
        user1.setName("张三");
        user1.setEmail("zhangsan@qq.com");
        boolean serviceInsertResult = userService.save(user1);
        System.out.println("service insert result:" + serviceInsertResult);
        System.out.println("service insert id:" + user1.getId());

        log.info("===mapper insert test===");
        User user2 = new User();
        user2.setAge(19);
        user2.setName("李四");
        user2.setEmail("lisi@qq.com");
        Integer mapperInsertResult = userMapper.insert(user2);
        System.out.println("mapper insert result:" + mapperInsertResult);
        System.out.println("mapper insert id:" + user2.getId());
    }
}
