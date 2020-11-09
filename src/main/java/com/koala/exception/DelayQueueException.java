package com.koala.exception;

import com.koala.common.enums.ErrorCodeEnum;

/**
 * @author admin
 */
public class DelayQueueException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误编码
     */
    private ErrorCodeEnum errCode;

    /**
     * 错误信息
     */
    private String errMsg;

    /**
     * 无参构造函数
     */
    public DelayQueueException() {
        super();
    }

    public DelayQueueException(Throwable e) {
        super(e);
    }

    public DelayQueueException(ErrorCodeEnum errCode, String... errMsg) {
        super(errCode.getMsg());
        this.errCode = errCode;
        setErrMsg(errMsg, true);
    }

    public DelayQueueException(ErrorCodeEnum errCode, String errMsg, Boolean isTransfer) {
        super(errMsg);
        this.errCode = errCode;
        setErrMsg(new String[]{errMsg}, isTransfer);
    }

    /**
     * 构造函数
     *
     * @param cause 异常
     */
    public DelayQueueException(ErrorCodeEnum errCode, Throwable cause, String... errMsg) {
        super(errCode.getCode() + errCode.getMsg(), cause);
        this.errCode = errCode;
        setErrMsg(errMsg, true);
    }

    public DelayQueueException(String code, String msg) {

    }

    public ErrorCodeEnum getErrCode() {
        return errCode;
    }

    public void setErrCode(ErrorCodeEnum errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    private void setErrMsg(String[] errMsg, Boolean isTransfer) {

        if (null != errMsg && errMsg.length > 0) {
            if (errCode.getMsg().contains("%s") && isTransfer) {
                this.errMsg = String.format(errCode.getMsg(), (Object) errMsg);
            } else {
                StringBuilder sf = new StringBuilder();
                for (String msg : errMsg) {
                    sf.append(msg).append(";");
                }
                this.errMsg = sf.toString();
            }
        } else {
            this.errMsg = errCode.getMsg();
        }

    }

    public static void main(String[] args) {
        String str = "ERRCode:1004--对象不存在:[%s]";
        System.out.println(String.format(str, "包含"));
    }

}