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
public class BaseRequest implements Serializable {

    /**
     * 业务线主键（例如订单id），请务必保证唯一，该值会在消息到期后通过mq返回给业务端
     */
    private String businessId;

    /**
     * 延迟任务命名空间
     */
    private String namespace;

    /**
     * String类型延时数据(目前只支持String类型，长度限制为1Kb内)
     */
    private String message;

    /**
     * 延迟任务到期执行时，通过此topic通知消费端，进行延迟任务处理
     */
    private String topic;
}
