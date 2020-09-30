package com.koala.entity.inner;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 任务配置
 *
 * @author moon
 * @date 2020-09-27 20:38:45
 */
@Data
public class TaskConfig {

    /**
     * 任务命名空间--为了区分业务的
     */
    private String ns;

    /**
     * 对应集群编码
     */
    private String clusterCode;

    /**
     * 接收client延迟任务的topic
     */
    private String metaTopic;

    public void validate() {
        if (StringUtils.isBlank(ns)) {
            throw new IllegalArgumentException("taskNs can not be null");
        }
        if (StringUtils.isBlank(clusterCode)) {
            throw new IllegalArgumentException("clusterCode can not be null");
        }
        if (StringUtils.isBlank(metaTopic)) {
            throw new IllegalArgumentException("metaTopic can not be null");
        }
    }
}
