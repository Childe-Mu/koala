package com.koala.client.rpc;

import com.koala.common.entity.Result;

import java.util.List;

/**
 * 延迟请求发送器
 *
 * @author moon
 * @date 2020-09-27 20:28:07
 */
public interface DelayRequestSender<T extends DelayRequest> {

    /**
     * 发送延迟请求
     *
     * @param delayRequest 延迟请求
     * @return 发送延迟请求结果，true：成功入队或者执行，false：延迟请求入队或者执行失败
     */
    Result<Boolean> sendDelayRequest(T delayRequest);

    /**
     * 批量发送延迟请求
     *
     * @param delayRequests 延迟请求列表
     * @return 批量发送延迟请求结果，true：成功入队或者执行，false：延迟请求入队或者执行失败
     */
    Boolean sendDelayRequests(List<T> delayRequests);

    // /**
    //  * 批量发送延迟请求，带超时时间，如果到超时时间未完成，则直接返回false
    //  *
    //  * @param delayRequests 延迟请求列表
    //  * @param timeout       等待超时时间
    //  * @param unit          等待超时时间单位
    //  * @return true/false
    //  */
    // Boolean sendDelayRequests(List<T> delayRequests, Long timeout, TimeUnit unit);
    //
    // /**
    //  * 返回请求的类型
    //  *
    //  * @return Class<T>
    //  */
    // Class<T> getRequestType();
}