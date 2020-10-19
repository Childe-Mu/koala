package com.koala.client.rpc;

import com.google.common.collect.Maps;
import com.koala.common.entity.Result;
import com.koala.core.TimedTask;
import com.koala.core.Timer;
import com.koala.entity.BasicConfig;
import com.koala.enums.DelayType;
import lombok.extern.slf4j.Slf4j;
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
public class DelayQueueClient implements DelayRequestSender<DelayRequest> {

    private Map<String, Timer> delayRequestSenders = Maps.newHashMap();

    public DelayQueueClient(BasicConfig basicConfig) {
        Timer timer = Timer.getInstance();
    }

    private DelayRequestSender getSender(DelayType delayType) {
        DelayRequestSender delayRequestSender = (DelayRequestSender) this.delayRequestSenders.get(delayType);
        if (delayRequestSender == null) {
            throw new IllegalStateException("not such DelayRequestSender instance by type " + delayType.name());
        } else {
            return delayRequestSender;
        }
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

            }
            Timer timer = Timer.getInstance();
            TimedTask timedTask1 = new TimedTask(21 * 1000L, () -> System.out.println("task 1"));
            timer.addTask(timedTask1);
            return Result.success(true);
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
