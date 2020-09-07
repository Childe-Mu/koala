package com.koala.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author admin
 */
public class Timer {

    /**
     * 最底层的那个时间轮
     */
    private TimeWheel timeWheel;

    /**
     * 对于一个Timer以及附属的时间轮，都只有一个delayQueue
     */
    private final DelayQueue<Bucket> delayQueue = new DelayQueue<>();

    /**
     * 只有一个线程的无限阻塞队列线程池
     */
    private ExecutorService workerThreadPool;

    private ExecutorService bossThreadPool;

    private static volatile Timer INSTANCE;

    private AtomicLong consumeCounter = new AtomicLong(0);

    public static Timer getInstance() {
        if (INSTANCE == null) {
            synchronized (Timer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Timer();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 新建一个Timer，同时新建一个时间轮
     */
    public Timer() {
        workerThreadPool = new ThreadPoolExecutor(100, 100,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10000),
                new ThreadFactoryBuilder().setPriority(10).setNameFormat("TimeWheelWorker").build(),
                new ThreadPoolExecutor.AbortPolicy());
        bossThreadPool = new ThreadPoolExecutor(100, 100,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10000),
                new ThreadFactoryBuilder().setPriority(10).setNameFormat("TimeWheelBoss").build(),
                new ThreadPoolExecutor.AbortPolicy());

        timeWheel = new TimeWheel(1000, 60, System.currentTimeMillis(), delayQueue);

        bossThreadPool.execute(() -> {
            while (true) {
                getInstance().advanceClock(timeWheel.getTickTime());
            }
        });
    }

    public static void main(String[] args) {
        // TimeWheel timeWheel = new TimeWheel(1000, 60, System.currentTimeMillis(), new DelayQueue<>());
        TimedTask timedTask1 = new TimedTask(21 * 1000L, () -> System.out.println("task 1"));
        TimedTask timedTask2 = new TimedTask(50 * 1000L, () -> System.out.println("task 2"));
        TimedTask timedTask3 = new TimedTask(70 * 1000L, () -> System.out.println("task 3"));
        getInstance().addTask(timedTask1);
        getInstance().addTask(timedTask2);
        getInstance().addTask(timedTask3);
        System.out.println();
    }

    /**
     * 将任务添加到时间轮
     */
    public void addTask(TimedTask timedTask) {
        // 添加到时间轮中
        boolean isSuccess = timeWheel.addTask(timedTask);
        // 添加不成功，要么任务被取消，要么任务超时了
        if (!isSuccess) {
            // 还没有被取消，那就是超时了，直接执行
            if (!timedTask.isCancel()) {
                workerThreadPool.submit(timedTask.getTask());
            }
        }
    }

    /**
     * 推进一下时间轮的指针，并且将delayQueue中的任务取出来再重新扔进去
     */
    private void advanceClock(long timeout) {
        System.out.println("前进" + timeout + "ms");
        try {
            // 没有到超时不会被轮询出
            // poll(long timeout,TimeUnit unit)检索并移除此队列的头部，如果此队列不存在未到期延迟的元素，则在到达指定的等待时间之前，一直等待（如果有必要）。
            Bucket bucket = delayQueue.poll(timeout, TimeUnit.MILLISECONDS);
            // 从延迟队列轮询出存储的延迟任务队列
            if (bucket != null) {
                timeWheel.advanceClock(bucket.getExpire());
                // 重新插入,函数的entry参数只有真正调用flush方法才能知道
                bucket.flush(this::addTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
