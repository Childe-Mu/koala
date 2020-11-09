package com.koala.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.common.message.Message;
import com.koala.client.rpc.DelayRequest;
import com.koala.common.enums.ErrorCodeEnum;
import com.koala.exception.DelayQueueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * dove mq 延迟请求发送器
 *
 * @author moon
 * @date 2020-09-27 20:43:35
 */
@Slf4j
@Component
public class Sender {

    /**
     * 使用RocketMq的生产者
     */
    @Autowired
    private DefaultMQProducer defaultMqProducer;

    /**
     * 发送消息
     *
     * @param delayRequest 要发送的消息内容
     */
    public void send(DelayRequest delayRequest) {
        try {
            log.info("开始发送延迟队列消息：{}", JSON.toJSONString(delayRequest));
            Message sendMsg = new Message(delayRequest.getTopic(), delayRequest.getMessage().getBytes());
            defaultMqProducer.send(sendMsg);
            log.info("发送延迟队列消息成功！");
        } catch (Exception e) {
            log.info("发送延迟队列消息，异常：", e);
            throw new DelayQueueException(ErrorCodeEnum.ERROR_SEND_MQ, "延迟队列任务-处理失败-发送消息失败！任务标识：" + delayRequest.getBusinessId());
        }
    }
}