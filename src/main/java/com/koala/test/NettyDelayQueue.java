package com.koala.test;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

public class NettyDelayQueue {
    public static void main(String[] args) {
        final Timer timer = new HashedWheelTimer(24, //时间轮一圈的长度
                TimeUnit.SECONDS,
                12);//时间轮的度刻
        io.netty.util.TimerTask task = timeout -> System.out.println("任务执行");
        timer.newTimeout(task, 1000, TimeUnit.SECONDS);

        // 定时任务
        io.netty.util.TimerTask task1 = new io.netty.util.TimerTask() {
            @Override
            public void run(Timeout timeout) {
                System.out.println("order1  5s 后执行 ");
                timer.newTimeout(this, 5, TimeUnit.SECONDS);//结束时候再次注册
            }
        };
        timer.newTimeout(task1, 5, TimeUnit.SECONDS);

        TimerTask task2 = new TimerTask() {
            @Override
            public void run(Timeout timeout) {
                System.out.println("order2  10s 后执行");
                timer.newTimeout(this, 10, TimeUnit.SECONDS);//结束时候再注册
            }
        };
        timer.newTimeout(task2, 10, TimeUnit.SECONDS);

        //延迟任务
        timer.newTimeout(timeout -> System.out.println("order3  15s 后执行一次"), 15, TimeUnit.SECONDS);
    }
}
