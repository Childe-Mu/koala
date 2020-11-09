package com.koala.durability;

import com.koala.client.rpc.DelayRequest;
import org.springframework.stereotype.Component;

/**
 * 持久化接口
 *
 * @author moon
 * @date 2020-11-02 21:18:32
 */
@Component
public class DurabilityImpl implements Durability {
    @Override
    public void addTaskToWaitExecute(DelayRequest delayRequest) {

    }

    @Override
    public void addTaskToArchived(DelayRequest delayRequest) {

    }
}
