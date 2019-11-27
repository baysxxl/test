package net.wlfeng.test.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dal.domain.User;
import net.wlfeng.test.dto.CommonResponse;
import net.wlfeng.test.dto.UserDTO;
import net.wlfeng.test.service.UserService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author weilingfeng
 * @date 2019/11/27 14:49
 * @description mybatis plus测试controller
 */
@Slf4j
@RestController
@RequestMapping("mybatisPlus")
public class MybatisPlusTestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedissonClient redissonClient;

    private final String USER_ADD_KEY = "user:add:";

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    @GetMapping("query")
    public CommonResponse<UserDTO> queryUser(@RequestParam("id") Integer id) {
        log.info("===进入查询用户信息controller,请求参数:{}===", id);
        return CommonResponse.success(userService.selectById(id));
    }

    /**
     * 新增用户
     * @param userDTO
     * @return
     */
    @PostMapping("add")
    public CommonResponse<Boolean> add(@RequestBody @Validated UserDTO userDTO) {
        log.info("===进入新增用户信息controller,请求参数:{}===", JSON.toJSONString(userDTO));
        CommonResponse<Boolean> response = CommonResponse.success(true);
        RLock lock = redissonClient.getLock(USER_ADD_KEY + userDTO.getName());
        try {
            log.info("===isLocked:{}===", lock.isLocked());
            // 尝试加锁，最多等待0秒，上锁以后5秒自动解锁
            if (lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                User user = new User();
                BeanUtils.copyProperties(userDTO, user);
                response.setSuccess(userService.insert(user));
            } else {
                response = CommonResponse.fail("请勿重新请求");
            }
        } catch (Exception e) {
            log.error("===新增用户异常,异常信息:{}===", e.toString());
        } finally {
            // 如果锁不为空，并且当前线程已经持有锁，并且锁是锁住状态
            if (lock != null && lock.isHeldByCurrentThread() && lock.isLocked()) {
                lock.unlock();
            }
        }
        return response;
    }
}
