package com.koala.core;

import lombok.Getter;

import java.util.concurrent.DelayQueue;

/**
 * 时间轮，可以推进时间和添加任务
 * @author admin
 */
@Getter
class TimeWheel {

    /**
     * 一个时间槽的时间（时间刻度，比如手表秒针刻度为1秒）
     */
    private long tickTime;

    /**
     * 时间轮大小（当前时间轮，一圈有多少个刻度，比如手表秒针一圈60个刻度）
     */
    private int wheelSize;

    /**
     * 时间跨度
     */
    private long interval;

    /**
     * 槽
     */
    private Bucket[] buckets;

    /**
     * 时间轮指针（类似钟表的指针，只能取整数的秒数，分钟数等等）
     */
    private long currentTimestamp;

    /**
     * 上层时间轮
     */
    private volatile TimeWheel overflowWheel;

    /**
     * 对于一个Timer以及附属的时间轮，都只有一个delayQueue
     */
    private DelayQueue<Bucket> delayQueue;

    TimeWheel(long tickTime, int wheelSize, long currentTimestamp, DelayQueue<Bucket> delayQueue) {
        this.currentTimestamp = currentTimestamp;
        this.tickTime = tickTime;
        this.wheelSize = wheelSize;
        this.interval = tickTime * wheelSize;
        this.buckets = new Bucket[wheelSize];
        this.currentTimestamp = currentTimestamp - (currentTimestamp % tickTime);
        this.delayQueue = delayQueue;
        for (int i = 0; i < wheelSize; i++) {
            buckets[i] = new Bucket();
        }
    }

    private TimeWheel getOverflowWheel() {
        if (overflowWheel == null) {
            synchronized (this) {
                if (overflowWheel == null) {
                    overflowWheel = new TimeWheel(interval, wheelSize, currentTimestamp, delayQueue);
                }
            }
        }
        return overflowWheel;
    }

    /**
     * 添加任务到某个时间轮
     */
    boolean addTask(TimedTask timedTask) {
        long expireTimestamp = timedTask.getExpireTimestamp();
        long delayTime = expireTimestamp - currentTimestamp;
        // 这个任务的到期时间小于时间轮指针，过期了
        if (delayTime < tickTime) {
            return false;
        } else {
            // 扔进当前时间轮的某个槽中，只有时间【大于某个槽】，才会放进去
            if (delayTime < interval) {
                // expireTimestamp / tickTime 是用来计算时间戳可以被时间刻度均分多少份，比如刻度为1秒，则时间戳可被分为多少秒（分钟、小时...同理）
                // (expireTimestamp / tickTime) % wheelSize  用来计算时间戳落到本次时间轮的那个刻度，
                // 如果刻度为1秒，时间轮刻度数为60，则时间戳表示的多少秒，就会落到那个刻度，（分钟、小时...同理）
                // 比如时间戳为"2020-09-03 10:39:25",当前时间为"2020-09-03 10:39:00"，则时间戳会落到下标为25的刻度内（(25,26],在26秒的时候执行）
                int bucketIndex = (int) ((expireTimestamp / tickTime) % wheelSize);
                Bucket bucket = buckets[bucketIndex];
                bucket.addTask(timedTask);
                // 给任务槽设置过期时间（过期时间即为执行时间，取刻度开始的时间）
                if (bucket.setExpire(expireTimestamp - expireTimestamp % tickTime)) {
                    delayQueue.offer(bucket);
                }
            } else {
                // 当maybeInThisBucket大于等于wheelSize时，需要将它扔到上一层的时间轮
                // 上一次的时间轮，时间刻度为当前时间轮的时间跨度，刻度数为当前时间轮的刻度数
                TimeWheel timeWheel = getOverflowWheel();
                timeWheel.addTask(timedTask);
            }
        }
        return true;
    }

    /**
     * 尝试推进一下指针
     */
    void advanceClock(long timestamp) {
        if (timestamp >= currentTimestamp + tickTime) {
            currentTimestamp = timestamp - (timestamp % tickTime);
            if (overflowWheel != null) {
                this.getOverflowWheel().advanceClock(timestamp);
            }
        }
    }
}
