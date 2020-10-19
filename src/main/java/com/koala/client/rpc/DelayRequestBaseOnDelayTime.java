package com.koala.client.rpc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

/**
 * 基础请求对象
 *
 * @author moon
 * @date 2020-09-27 20:31:31
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DelayRequestBaseOnDelayTime extends BaseRequest  {

    /**
     * 延时时间大小，必须大于等于0
     */
    private Long delay;

    /**
     * 基础时间戳，配合delay属性使用，即在baseTime的基础上，延迟delay的时间长度，执行任务
     * 如果计算后的时间，小于等于当前时间，则延迟任务会被立即执行
     */
    private Long baseTime;

    /**
     * 延迟时间单位 默认 秒
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;
}
