package com.koala.client;

import com.google.common.collect.Maps;
import com.koala.entity.BasicConfig;
import com.koala.enums.DelayType;
import com.koala.sender.DoveMqDelayRequestSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 延迟类型
 *
 * @author moon
 * @date 2020-09-27 20:18:55
 */
@Slf4j
public class DelayQueueClient {
    private Map<DelayType, DelayRequestSender<?>> delayRequestSenders = Maps.newHashMap();

    public DelayQueueClient(BasicConfig basicConfig) {
        this.delayRequestSenders.put(DelayType.MQ, DoveMqDelayRequestSender.newInstance(basicConfig));
    }

    private DelayRequestSender getSender(DelayType delayType) {
        DelayRequestSender delayRequestSender = (DelayRequestSender) this.delayRequestSenders.get(delayType);
        if (delayRequestSender == null) {
            throw new IllegalStateException("not such DelayRequestSender instance by type " + delayType.name());
        } else {
            return delayRequestSender;
        }
    }

    public <T extends BaseDelayRequest> boolean sendDelayRequest(T delayRequest) {
        if (delayRequest == null) {
            return false;
        } else {
            ValidationUtil.validationAndThrow(delayRequest, new Class[0]);
            return this.getSender(delayRequest.getDelayType()).sendDelayRequest(delayRequest);
        }
    }

    public <T extends BaseDelayRequest> boolean sendDelayRequests(List<T> delayRequests) {
        return this.sendDelayRequests(delayRequests, 3000L, TimeUnit.MILLISECONDS);
    }

    public <T extends BaseDelayRequest> boolean sendDelayRequests(List<T> delayRequests, long timeout, TimeUnit unit) {
        if (CollectionUtils.isEmpty(delayRequests)) {
            log.warn("empty delay requests");
            return false;
        } else {
            delayRequests.forEach((x$0) -> {
                ValidationUtil.validationAndThrow(x$0, new Class[0]);
            });
            Map<String, List<T>> delayRequestsByNs = (Map) delayRequests.stream().collect(Collectors.groupingBy(BaseDelayRequest::getTaskNs));
            boolean result = true;

            List delayReqs;
            for (Iterator var7 = delayRequestsByNs.values().iterator(); var7.hasNext(); result &= this.getSender(((BaseDelayRequest) delayReqs.get(0)).getDelayType()).sendDelayRequests(delayReqs, timeout, unit)) {
                delayReqs = (List) var7.next();
            }

            return result;
        }
    }
}
