package com.quinn.framework.exception;

import com.quinn.framework.util.enums.AuthExceptionEnum;
import com.quinn.util.base.exception.BaseBusinessException;

/**
 * 未授权
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
public class UnauthorizedException extends BaseBusinessException {

    {
        buildParam(AuthExceptionEnum.UNAUTHORIZED_ACCESS.name(), 0, 0);
    }

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
