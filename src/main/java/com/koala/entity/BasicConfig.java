package com.koala.entity;


import com.koala.entity.inner.TaskConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * 基础配置
 *
 * @author moon
 * @date 2020-09-27 20:37:20
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicConfig {

    /**
     * 业务线
     */
    private String businessLine;
    /**
     * 项目
     */
    private String project;
    /**
     * 由于延迟任务会先丢到一个固定mq集群中销峰，所以需要配置此mq的zk集群地址信息
     */
    private String mqMetaServers;

    /**
     * 延迟任务配置
     */
    private Map<String, TaskConfig> taskConfigs;

}