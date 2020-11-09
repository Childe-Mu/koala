package com.koala.common.enums;

import com.koala.common.interfaces.EnumValues;

/**
 * 延迟队列错误码枚举类
 *
 * @author moon
 * @date 2020-11-09 21:04:59
 */
public enum ErrorCodeEnum implements EnumValues {
    /**
     * 处理任务时，发送消息失败
     */
    ERROR_SEND_MQ(1, "EC001", "处理任务时，发送消息失败"),
    ;

    /**
     * 索引
     */
    private Integer index;

    /**
     * 编码
     */
    private String code;

    /**
     * 信息
     */
    private String msg;

    private ErrorCodeEnum(Integer index, String code, String msg) {
        this.index = index;
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getIndex() {
        return this.index;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}