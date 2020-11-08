package com.koala.client.rpc;

import com.koala.common.entity.Result;
import com.koala.core.TimedTask;
import com.koala.core.Timer;
import com.koala.core.TimerExecutor;
import com.koala.durability.Durability;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    private final Durability durability;

    public DelayQueueClient(Durability durability) {
        this.durability = durability;
    }

    @Override
    public Result<Boolean> sendDelayRequest(DelayRequest delayRequest) {
        // 入参校验
        if (Objects.isNull(delayRequest) || Objects.isNull(delayRequest.getBusinessId())
                || Objects.isNull(delayRequest.getBusinessTime()) || Objects.isNull(delayRequest.getNamespace())) {
            return Result.invalidRequest("sendDelayRequest，入参不合法！");
        }
        try {
            // <= 当前时间，延迟任务会被立即执行
            if (delayRequest.getBusinessTime() <= System.currentTimeMillis()) {
                // todo 直接发mq 或者现将任务存储到时间轮，因为时间会判断时间是否过期，如果过期也会立即执行
                // 1.直接执行任务（即发送消息）

                // 2.执行成功以后，直接将数据保存至归档表
                durability.addTaskToArchived(delayRequest);
                return Result.success(true);
            } else {
                // 1.获取时间轮，并将任务添加到其中
                Timer timer = TimerExecutor.getInstance().getTimerByNamespace(delayRequest.getNamespace());
                TimedTask timedTask = new TimedTask(delayRequest.getBusinessTime(), () -> System.out.println("task 1"));
                timer.addTask(timedTask);
                // 2.同步持久化任务信息到数据库（mysql，redis，es）
                durability.addTaskToWaitExecute(delayRequest);
                return Result.success(true);
            }
        } catch (Exception e) {
            log.error("sendDelayRequest，接口发生异常：", e);
            return Result.error("sendDelayRequest，调用发生异常!");
        }
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
