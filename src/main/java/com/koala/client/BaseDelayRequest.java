package com.koala.client;

import com.koala.enums.DelayTimeUnit;
import com.koala.enums.DelayType;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础请求对象
 *
 * @author moon
 * @date 2020-09-27 20:31:31
 */
@Data
public abstract class BaseDelayRequest implements Serializable {

    /**
     * 业务线主键（例如订单id），请务必保证唯一，该值会在消息到期后通过mq返回给业务端
     */
    // @NotBlank("业务主键id不能为空")
    private String businessId;

    /**
     * 延迟时间单位 默认 秒
     */
    private DelayTimeUnit delayTimeUnit = DelayTimeUnit.SECONDS;

    /**
     * 延时时间大小
     */
    // @Min(value = 0, message = "延迟大小必须大于等于0")
    private int delay;

    /**
     * 业务时间戳-如果为空则采用实际到达服务端时间为准
     */
    private Long businessTime;

    /**
     * 延迟任务命名空间
     */
    private String taskNs;

    /**
     * 请求类型
     *
     * @return DelayType
     */
    public abstract DelayType getDelayType();

}
