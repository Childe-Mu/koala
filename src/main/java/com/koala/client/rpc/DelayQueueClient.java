package com.koala.client.rpc;

import com.alibaba.fastjson.JSON;
import com.koala.common.entity.Result;
import com.koala.common.enums.ErrorCodeEnum;
import com.koala.core.TimedTask;
import com.koala.core.Timer;
import com.koala.core.TimerExecutor;
import com.koala.durability.Durability;
import com.koala.exception.DelayQueueException;
import com.koala.mq.Sender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 延迟类型
 *
 * @author moon
 * @date 2020-09-27 20:18:55
 */
@Slf4j
@Service
public class DelayQueueClient implements DelayRequestSender<DelayRequest> {

    /**
     * 数据持久化接口
     */
    private final Durability durability;

    /**
     * 消息发送接口
     */
    private final Sender sender;

    public DelayQueueClient(Durability durability, Sender sender) {
        this.durability = durability;
        this.sender = sender;
    }

    @Override
    public Result<Boolean> sendDelayRequest(DelayRequest delayRequest) {
        // 入参校验
        if (Objects.isNull(delayRequest) || Objects.isNull(delayRequest.getBusinessId())
                || Objects.isNull(delayRequest.getBusinessTime()) || Objects.isNull(delayRequest.getNamespace())) {
            return Result.invalidRequest("sendDelayRequest，入参不合法！");
        }
        try {
            Boolean re = handleDelayTask(delayRequest);
            return Result.success(re);
        } catch (Exception e) {
            log.error("sendDelayRequest，接口发生异常：", e);
            return Result.error("sendDelayRequest，调用发生异常!");
        }
    }

    private Boolean handleDelayTask(DelayRequest delayRequest) {
        // <= 当前时间，延迟任务会被立即执行
        if (delayRequest.getBusinessTime() <= System.currentTimeMillis()) {
            return executeNow(delayRequest);
        } else {
            return enqueueWaitToExecute(delayRequest);
        }
    }

    /**
     * 加入到延迟队列中，等待执行
     *
     * @param delayRequest 延迟任务对象
     * @return 处理结果
     */
    private Boolean enqueueWaitToExecute(DelayRequest delayRequest) {
        try {
            // 1.获取时间轮，并将任务添加到其中，
            Timer timer = TimerExecutor.getInstance().getTimerByNamespace(delayRequest.getNamespace());
            // todo 添加事物以及失败重试
            Runnable task = () -> executeTask(delayRequest);
            TimedTask timedTask = new TimedTask(delayRequest.getBusinessTime(), task);
            timer.addTask(timedTask);
        } catch (Exception e) {
            // 2.如果添加到延迟队列失败，就直接失败，返回给调用方
            log.error("延迟任务({})加入到延迟队列异常：", JSON.toJSONString(delayRequest), e);
            throw new DelayQueueException(ErrorCodeEnum.ERROR_ENQUEUE);
        }
        try {
            // 3.同步持久化任务信息到数据库（mysql，redis，es）
            durability.addTaskToWaitExecute(delayRequest);
            return true;
        } catch (Exception e) {
            log.error("延迟任务({})入库异常，开始重试，异常信息：", JSON.toJSONString(delayRequest), e);
            // todo 重试，一般重试三次，可以通过netty延迟队列来实现，但是。。。我实现就是延迟队列，这就很奇怪，自己实现一个吧
            return false;
        }
    }

    /**
     * 延迟队列中的任务到期，执行任务，发送消息，并将待执行表的数据迁移到归档表
     *
     * @param delayRequest 延迟任务对象
     */
    @Transactional(rollbackFor = Exception.class)
    void executeTask(DelayRequest delayRequest) {
        try {
            durability.addTaskToArchived(delayRequest);
            durability.deleteTaskFromWaitExecute(delayRequest);
            sender.send(delayRequest);
        } catch (Exception e) {
            log.error("执行任务({})异常，开始重试，异常信息为：", JSON.toJSONString(delayRequest), e);
            // todo 重试，一般重试三次，可以通过netty延迟队列来实现，但是。。。我实现就是延迟队列，这就很奇怪，自己实现一个吧
        }
    }

    /**
     * 延迟任务已经到期，需要被立即执行，并将数据归档
     *
     * @param delayRequest 延迟任务对象
     * @return 处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    Boolean executeNow(DelayRequest delayRequest) {
        // 1.直接执行任务（即发送消息）
        sender.send(delayRequest);
        // 2.执行成功以后，直接将数据保存至归档表
        durability.addTaskToArchived(delayRequest);
        return true;
    }


    @Override
    public Boolean sendDelayRequests(List<DelayRequest> delayRequests) {
        return this.sendDelayRequests(delayRequests, 3000L, TimeUnit.MILLISECONDS);
    }

    private <T extends DelayRequest> Boolean sendDelayRequests(List<T> delayRequests, long timeout, TimeUnit unit) {
        if (CollectionUtils.isEmpty(delayRequests)) {
            log.warn("empty delay requests");
            return false;
        } else {
            delayRequests.forEach((x0) -> {
                // ValidationUtil.validationAndThrow(x$0, new Class[0]);
            });
            Map delayRequestsByNs = (Map) delayRequests.stream().collect(Collectors.groupingBy(DelayRequest::getNamespace));
            boolean result = true;

            List delayReqs;
            // for (Iterator var7 = delayRequestsByNs.values().iterator(); var7.hasNext(); result &= this.getSender(((BaseDelayRequest) delayReqs.get(0)).getDelayType()).sendDelayRequests(delayReqs, timeout, unit)) {
            //     delayReqs = (List) var7.next();
            // }

            return result;
        }
    }
}
