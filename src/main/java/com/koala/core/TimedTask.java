package com.koala.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 需要延迟执行的任务，放在槽 {@link Bucket} 里面
 * @author admin
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class TimedTask {

    private Class<?> fromClazz;

    /**
     * 延迟多久执行
     */
    private Long delayTime;

    /**
     * 过期时间戳
     */
    private Long expireTimestamp;

    /**
     * 具体要进行的任务操作
     */
    private Runnable task;

    /**
     * 是否取消
     */
    private volatile boolean cancel;

    /**
     * 任务槽
     */
    protected Bucket bucket;

    /**
     * 下一个需要延迟执行的任务
     */
    protected TimedTask next;

    /**
     * 前一个需要延迟执行的任务
     */
    protected TimedTask pre;

    /**
     * 描述
     */
    public String desc;

    public TimedTask(Long expireTimestamp, Runnable task) {
        this.delayTime = expireTimestamp - System.currentTimeMillis();
        this.task = task;
        this.bucket = null;
        this.next = null;
        this.pre = null;
        this.expireTimestamp = expireTimestamp;
        this.cancel = false;
    }

    public TimedTask(Long delayTime) {
        this.delayTime = delayTime;
        this.task = null;
        this.bucket = null;
        this.next = null;
        this.pre = null;
        this.expireTimestamp = System.currentTimeMillis() + delayTime;
        this.cancel = false;
    }
}
