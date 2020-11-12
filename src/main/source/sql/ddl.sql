CREATE TABLE `t_wait_execute`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `business_id`   varchar(32) DEFAULT NULL COMMENT '业务线主键',
    `business_time` bigint(20)  DEFAULT NULL COMMENT '业务执行时间戳',
    `namespace`     varchar(64) DEFAULT NULL COMMENT '延迟任务命名空间',
    `message`       text        DEFAULT NULL COMMENT 'String类型延时数据(目前只支持String类型，长度限制为1Kb内)',
    `topic`         varchar(64) DEFAULT NULL COMMENT '延迟任务到期执行时，通过此topic通知消费端，进行延迟任务处理',
    PRIMARY KEY (`id`),
    KEY `idx_business_time` (`business_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='等待执行的延迟任务数据表';

CREATE TABLE `t_archived`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `business_id`   varchar(32) DEFAULT NULL COMMENT '业务线主键',
    `business_time` bigint(20)  DEFAULT NULL COMMENT '业务执行时间戳',
    `namespace`     varchar(64) DEFAULT NULL COMMENT '延迟任务命名空间',
    `message`       text        DEFAULT NULL COMMENT 'String类型延时数据(目前只支持String类型，长度限制为1Kb内)',
    `topic`         varchar(64) DEFAULT NULL COMMENT '延迟任务到期执行时，通过此topic通知消费端，进行延迟任务处理',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='延迟任务归档表';