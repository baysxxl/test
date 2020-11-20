package net.wlfeng.test.controller;

import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dto.CommonResponse;
import net.wlfeng.test.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author weilingfeng
 * @date 2019/11/26 10:03
 * @description redis相关功能测试controller
 */
@Slf4j
@Controller
@RequestMapping("queueTest")
public class QueueTestController {

    @Autowired
    private QueueService queueService;

    @ResponseBody
    @PostMapping("/pull")
    public CommonResponse pullTest(@RequestBody String value) {
        log.info("======推送数据到队列:{}======", value);
        queueService.pull(value);
        return CommonResponse.success();
    }

}
