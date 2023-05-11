package com.fckey.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname BootPool
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/11 17:22
 **/
public class BootPool extends Thread{
    // 自己定义一把锁
    private final ReentrantLock lock = new ReentrantLock();
    // 定义信号
    private final Condition condition = lock.newCondition();
    private final Queue<Bot> bots = new LinkedList<>();
    
    /**
     * @author Jeff Fong
     * @description 向队列中添加bot
     * @date 2023/5/11 18:10
     * @param: userId
     * @param: botCode
     * @param: input
     * @return void
     **/
    public void produce(Integer userId, String botCode, String input){
        lock.lock();
        try {
            bots.add(new Bot(userId, botCode, input));
            // 唤醒其他线程
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    /**
     * @author Jeff Fong
     * @description 消费消息队列中的bot
     * @date 2023/5/11 18:05
     * @param: bot
     * @return void
     **/
    private void consume(Bot bot){
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000, bot);
    }

    @Override
    public void run() {
        while(true){
            lock.lock();
            if(bots.isEmpty()){
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }else{
                Bot bot = bots.remove(); // 返回一个结果来进行消费
                lock.unlock();
                // 消费消息队列队列中的一个bot，由于比较耗时，所以一定要加在解锁的后面
                consume(bot);
            }
        }
    }
}