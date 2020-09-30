package com.koala.sender;


import com.koala.entity.BasicConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * dove mq 延迟请求发送器
 *
 * @author moon
 * @date 2020-09-27 20:43:35
 */
@Slf4j
// public class DoveMqDelayRequestSender implements DelayRequestSender<MqDelayRequest> {
public class DoveMqDelayRequestSender {
    /**
     * 基础配置
     */
    private BasicConfig basicConfig;
    // private TaskConfigAdapter taskConfigAdapter;
    private Map<String, Boolean> doveProducerPreparedMap = new ConcurrentHashMap<>(8);

    private DoveMqDelayRequestSender(BasicConfig basicConfig) {
        this.basicConfig = basicConfig;
        // this.taskConfigAdapter = new TaskConfigAdapter(basicConfig);
    }

    public static DoveMqDelayRequestSender newInstance(BasicConfig basicConfig) {
        return new DoveMqDelayRequestSender(basicConfig);
    }
    //
    // @Override
    // public boolean sendDelayRequest(MqDelayRequest delayRequest) {
    //     if (delayRequest == null) {
    //         return false;
    //     }
    //     TaskConfig taskConfig = this.taskConfigAdapter.getTaskConfig(delayRequest);
    //     Message<String, DelayDetail> message = new Message<>(taskConfig.getMetaTopic(), createDelayDetail(delayRequest));
    //     try {
    //         prepareDoveProducer(taskConfig.getMetaTopic());
    //         SendResult sendResult = ProducerManager.send(message);
    //         if (sendResult != null) {
    //             return sendResult.isSuccess();
    //         }
    //     } catch (InterruptedException e) {
    //         log.error("发送mq延迟请求失败", e);
    //     }
    //     return false;
    // }
    //
    // @Override
    // public boolean sendDelayRequests(List<MqDelayRequest> delayRequests) {
    //     return sendDelayRequests(delayRequests, 3000, TimeUnit.MILLISECONDS);
    // }
    //
    // @Override
    // public boolean sendDelayRequests(List<MqDelayRequest> delayRequests, long timeout, TimeUnit unit) {
    //     if (delayRequests == null || delayRequests.isEmpty()) {
    //         return false;
    //     }
    //     //获取延迟任务配置信息
    //     MqDelayRequest headMqDelayRequest = delayRequests.get(0);
    //     TaskConfig taskConfig = this.taskConfigAdapter.getTaskConfig(headMqDelayRequest);
    //     List<DelayDetail> details = new ArrayList<>();
    //     for (MqDelayRequest delayRequest : delayRequests) {
    //         details.add(createDelayDetail(delayRequest));
    //     }
    //     //按请求的触发时间排序，更早触发尽早发出
    //     details.sort((o1, o2) -> {
    //         BaseDelayRequest o1DelayRequest = o1.getDelayRequest();
    //         BaseDelayRequest o2DelayRequest = o2.getDelayRequest();
    //         long o1TriggerTime = ObjectUtils.defaultIfNull(o1DelayRequest.getBusinessTime(), 0L)
    //                 + o1DelayRequest.getDelayTimeUnit().getTimeUnit().toMillis(o1DelayRequest.getDelay());
    //         long o2TriggerTime = ObjectUtils.defaultIfNull(o2DelayRequest.getBusinessTime(), 0L)
    //                 + o2DelayRequest.getDelayTimeUnit().getTimeUnit().toMillis(o2DelayRequest.getDelay());
    //         return (int) (o1TriggerTime - o2TriggerTime);
    //     });
    //     try {
    //         Future<SendResult> sendResultFuture = null;
    //         prepareDoveProducer(taskConfig.getMetaTopic());
    //         for (DelayDetail delayDetail : details) {
    //             Message<String, DelayDetail> message = new Message<>(taskConfig.getMetaTopic(), delayDetail);
    //             sendResultFuture = ProducerManager.asyncSendForBatch(message);
    //         }
    //         if (sendResultFuture != null) {
    //             return waitFutureDone(sendResultFuture, timeout, unit);
    //         }
    //     } catch (Exception e) {
    //         log.error("发送mq延迟请求失败", e);
    //         return false;
    //     }
    //     return true;
    // }
    //
    // private boolean waitFutureDone(Future<SendResult> sendResultFuture, long timeout, TimeUnit unit) {
    //     try {
    //         return sendResultFuture.get(timeout, unit).isSuccess();
    //     } catch (InterruptedException e) {
    //         log.error("获取mq发送批次结果异常", e);
    //         Thread.currentThread().interrupt();
    //
    //     } catch (ExecutionException e) {
    //         log.error("获取mq发送批次结果异常", e);
    //
    //     } catch (TimeoutException e) {
    //         log.error("获取mq发送批次结果超时", e);
    //     }
    //     return false;
    // }
    //
    // private DelayDetail createDelayDetail(MqDelayRequest delayRequest) {
    //     if (delayRequest.getBusinessTime() == null) {
    //         delayRequest.setBusinessTime(System.currentTimeMillis());
    //     }
    //     DelayDetail delayDetail = new DelayDetail(delayRequest);
    //     delayDetail.setBusinessLine(basicConfig.getBusinessLine());
    //     delayDetail.setProjectCode(basicConfig.getProject());
    //     delayDetail.setDelayRequestClass(delayRequest.getClass());
    //     delayDetail.setDelayRequestJson(JSONObject.toJSONString(delayRequest));
    //     delayDetail.setTaskNs(delayRequest.getTaskNs());
    //     return delayDetail;
    // }
    //
    // @Override
    // public Class<MqDelayRequest> getRequestType() {
    //     return MqDelayRequest.class;
    // }
    //
    // /**
    //  * prepare doveProducer
    //  *
    //  * @param topic dove topic
    //  * @throws InterruptedException
    //  */
    // private void prepareDoveProducer(String topic) throws InterruptedException {
    //     if (doveProducerPreparedMap.containsKey(topic)) {
    //         return;
    //     }
    //     synchronized (topic.intern()) {
    //         if (doveProducerPreparedMap.containsKey(topic)) {
    //             return;
    //         }
    //         ProducerMetaDataConfig.Builder builder = new ProducerMetaDataConfig.Builder(basicConfig.getMqMetaServers(),
    //                 Sets.newHashSet(topic));
    //         ProducerManager.addProducer(builder.build());
    //         this.doveProducerPreparedMap.putIfAbsent(topic, true);
    //     }
    // }
}