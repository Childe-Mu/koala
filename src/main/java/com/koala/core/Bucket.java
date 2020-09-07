package com.koala.core;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 任务槽，用于存放一个时间段内的所有需要执行的任务
 *
 * @author admin
 */
public class Bucket implements Delayed {

    /**
     * 当前槽的过期时间
     */
    private AtomicLong expireTime = new AtomicLong(-1L);

    /**
     * 根节点
     */
    private TimedTask root = new TimedTask(-1L, null);

    {
        root.pre = root;
        root.next = root;
    }

    /**
     * 设置某个槽的过期时间
     */
    public boolean setExpire(long expire) {
        return expireTime.getAndSet(expire) != expire;
    }

    /**
     * 获取某个槽的过期时间
     */
    public long getExpire() {
        return expireTime.get();
    }

    /**
     * 新增任务到bucket
     */
    public void addTask(TimedTask timedTask) {
        if (timedTask.bucket == null) {
            timedTask.bucket = this;
            TimedTask tail = root.pre;

            timedTask.next = root;
            timedTask.pre = tail;

            tail.next = timedTask;
            root.pre = timedTask;
        }
    }

    /**
     * 从bucket移除任务
     */
    public void removeTask(TimedTask timedTask) {
        // 这里可能有bug
        if (timedTask.bucket.equals(this)) {
            timedTask.next.pre = timedTask.pre;
            timedTask.pre.next = timedTask.next;
            timedTask.bucket = null;
            timedTask.next = null;
            timedTask.pre = null;
        }
    }

    /**
     * 重新分配槽
     */
    public synchronized void flush(Consumer<TimedTask> flush) {
        // 从尾巴开始（最先加进去的）
        TimedTask timedTask = root.next;
        while (!timedTask.equals(root)) {
            this.removeTask(timedTask);
            flush.accept(timedTask);
            timedTask = root.next;
        }
        expireTime.set(-1L);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return Math.max(0, unit.convert(expireTime.get() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
    }

    @Override
    public int compareTo(Delayed o) {
        if (o instanceof Bucket) {
            return Long.compare(expireTime.get(), ((Bucket) o).expireTime.get());
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Bucket{" +
                "过期时间=" + new Date(expireTime.get()).toString() +
                ", 延迟=" + this.getDelay(TimeUnit.SECONDS) + "s" +
                '}';
    }
}
