package net.wlfeng.test.dal.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import net.wlfeng.test.dal.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {
	
	Integer updateEmail(@Param("id") Integer id, @Param("email") String email);

}
