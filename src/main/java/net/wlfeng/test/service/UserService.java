package net.wlfeng.test.service;

import com.baomidou.mybatisplus.service.IService;
import net.wlfeng.test.dal.domain.User;

public interface UserService extends IService<User> {
	
	boolean updateEmail(Integer id, String email);
	
}
