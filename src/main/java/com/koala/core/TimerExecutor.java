package com.koala.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 */
public class TimerExecutor {

    /**
     * 缓存Timer池
     */
    private ConcurrentHashMap<String, Timer> timerPool = new ConcurrentHashMap<>();

    /**
     * 执行器单例实体
     */
    private static volatile TimerExecutor INSTANCE;

    /**
     * 获取Timer执行器方法
     *
     * @return Timer执行器方法
     */
    public static TimerExecutor getInstance() {
        if (INSTANCE == null) {
            synchronized (Timer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TimerExecutor();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 根据namespace获取对应的timer，每个namespace对应一个独有的timer
     *
     * @param namespace 命名空间
     * @return namespace获取对应的timer
     */
    public Timer getTimerByNamespace(String namespace) {
        if (!timerPool.containsKey(namespace)) {
            timerPool.put(namespace, new Timer());
        }
        return timerPool.get(namespace);
    }

    private TimerExecutor() {
    }
}
