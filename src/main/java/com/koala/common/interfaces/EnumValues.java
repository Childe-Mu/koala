package com.koala.common.interfaces;

/**
 * 公共枚举类接口，所有内部枚举类都要实现这个接口
 *
 * @author moon
 * @date 2020-11-09 21:04:59
 */
public interface EnumValues {
    /**
     * index
     *
     * @return 返回index
     */
    Integer getIndex();

    /**
     * 编码
     *
     * @return 返回编码
     */
    String getCode();

    /**
     * 信息
     *
     * @return 返回信息
     */
    String getMsg();
}
