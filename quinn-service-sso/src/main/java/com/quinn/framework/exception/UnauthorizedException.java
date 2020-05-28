package com.quinn.framework.exception;

import com.quinn.util.base.exception.BaseBusinessException;

/**
 * 未授权
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
public class UnauthorizedException extends BaseBusinessException {

    /**
     * 状态码
     */
    private int statusCode;

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UnauthorizedException ofStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
