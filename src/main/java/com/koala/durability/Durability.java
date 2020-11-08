package com.koala.durability;

import com.koala.client.rpc.DelayRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 持久化接口
 *
 * @author yaojia.mu@luckincoffee.com
 * @date 2020-11-02 21:14:27
 */
@Mapper
public interface Durability {

    /**
     * 添加任务数据到待执行表
     *
     * @param delayRequest 任务数据
     */
    @Insert("insert into *** set ***=##, ***=## ")
    void addTaskToWaitExecute(DelayRequest delayRequest);

    /**
     * 添加任务数据到归档表表
     *
     * @param delayRequest 任务数据
     */
    @Insert("insert into *** set ***=##, ***=## ")
    void addTaskToArchived(DelayRequest delayRequest);
}
