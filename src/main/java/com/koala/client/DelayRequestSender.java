package com.koala.client;


import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 延迟请求发送器
 *
 * @author moon
 * @date 2020-09-27 20:28:07
 */
public interface DelayRequestSender<T extends BaseDelayRequest> {

    /**
     * 发送延迟请求
     *
     * @param delayRequest 延迟请求
     * @return true/false
     */
    boolean sendDelayRequest(T delayRequest);

    /**
     * 批量发送延迟请求
     *
     * @param delayRequests 延迟请求列表
     * @return true/false
     */
    boolean sendDelayRequests(List<T> delayRequests);

    /**
     * 发送延迟请求
     *
     * @param delayRequests 延迟请求列表
     * @param timeout       等待超时时间
     * @param unit          等待超时时间单位
     * @return true/false
     */
    boolean sendDelayRequests(List<T> delayRequests, long timeout, TimeUnit unit);


    /**
     * 返回请求的类型
     *
     * @return Class<T>
     */
    Class<T> getRequestType();
}