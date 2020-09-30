package com.koala.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * 延迟时间单位
 *
 * @author moon
 * @date 2020-09-27 20:34:33
 */
@Getter
@AllArgsConstructor
public enum DelayTimeUnit {

    /**
     * 天
     */
    DAYS(TimeUnit.DAYS),

    /**
     * 小时
     */
    HOURS(TimeUnit.HOURS),

    /**
     * 分钟
     */
    MINUTES(TimeUnit.MINUTES),

    /**
     * 秒
     */
    SECONDS(TimeUnit.SECONDS),
    ;

    private TimeUnit timeUnit;


    public static DelayTimeUnit parse(String unitName) {
        if (unitName == null) {
            return null;
        }
        for (DelayTimeUnit delayTimeUnit : DelayTimeUnit.values()) {
            if (delayTimeUnit.name().equals(unitName)) {
                return delayTimeUnit;
            }
        }
        return null;
    }
}