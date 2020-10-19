package com.koala.client.rpc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
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
public class DelayRequest extends BaseRequest  {

    /**
     * 业务时间戳，延迟任务执行时间为此时间戳的时刻，如果此时间戳 <= 当前时间，延迟任务会被立即执行
     */
    private Long businessTime;
}
