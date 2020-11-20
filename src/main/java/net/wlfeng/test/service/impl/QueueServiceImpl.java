package net.wlfeng.test.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.service.QueueService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author weilingfeng
 * @date 2020/9/23 15:34
 * @description
 */
@Slf4j
@Service
public class QueueServiceImpl implements QueueService {

    private static Queue<String> queue = new ConcurrentLinkedQueue<>();

    private Queue getQueue() {
        return queue;
    }

    @Override
    public void pull(String msg) {
        getQueue().add(msg);
    }

    @Scheduled(cron="0/1 * * * * ?")
    public void queuePollSchedule() {
        try{
            if (!getQueue().isEmpty()) {
                while (getQueue().peek() != null) {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println(getQueue().poll());
                    Thread.sleep(10000);
                }
            }
        }catch(Exception e){
            log.error("===队列执行异常,异常信息:{}===", e);
        }
    }

}
