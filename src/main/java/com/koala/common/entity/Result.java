package com.koala.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 返回结果包装类
 *
 * @author moon
 * @date 2020-10-19 22:08:08
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -6029022489278479070L;

    /**
     * 成功
     */
    public static final int SUCCESS = 0;

    /**
     * 失败
     */
    public static final int FAIL = 1;

    /**
     * 成功
     */
    public static final String MSG_SUCCESS = "success";

    /**
     * 服务器内部错误
     */
    public static final int STATUS_500 = 500;

    /**
     * 参数不合法
     */
    public static final int STATUS_400 = 400;


    /**
     * 服务状态码。为描述一致，0定义为成功。500默认为服务器内部错误
     */
    private int status = STATUS_500;

    /**
     * 服务错误详情码
     */
    private int errCode;

    /**
     * 错误描述信息。
     */
    private String msg;

    /**
     * 返回数据。
     */
    private T re;

    /**
     * 全参构造
     *
     * @param re     数据对象
     * @param status 状态值
     * @param msg    提示语
     */
    private Result(T re, int status, String msg) {
        super();
        this.status = status;
        this.msg = msg;
        this.re = re;
    }

    /**
     * 成功结果
     *
     * @param t 返回结果
     * @return 包装后的结果
     */
    public static <T> Result<T> success(T t) {
        return new Result<>(t, SUCCESS, MSG_SUCCESS);
    }

    /**
     * 空的成功结果
     *
     * @return 包装后的结果
     */
    public static <T> Result<T> success() {
        return new Result<>(null, SUCCESS, MSG_SUCCESS);
    }

    /**
     * 空的参数不合法结果
     *
     * @param message 消息
     * @return 包装后的结果
     */
    public static <T> Result<T> invalidRequest(String message) {
        return new Result<>(null, STATUS_400, message);
    }

    /**
     * 失败处理结果
     *
     * @param msgCode 失败编码
     * @param message 消息
     * @return 包装后的结果
     */
    public static <T> Result<T> fail(int msgCode, String message) {
        return new Result<>(null, msgCode, message);
    }

    /**
     * 失败结果
     *
     * @param msg 失败信息
     * @return 包装后的结果
     */
    public static <T> Result<T> error(String msg) {
        return new Result<>(null, STATUS_500, msg);
    }
}