package net.wlfeng.test.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dal.dao.UserMapper;
import net.wlfeng.test.dal.domain.User;
import net.wlfeng.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author weilingfeng
 * @date 2019/11/27 14:58
 * @description
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean updateEmail(Integer id, String email) {
        return userMapper.updateEmail(id, email) > 0;
    }

}
