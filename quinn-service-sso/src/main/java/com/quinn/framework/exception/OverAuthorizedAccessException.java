package com.quinn.framework.exception;

import com.quinn.framework.util.enums.AuthExceptionEnum;
import com.quinn.util.base.exception.BaseBusinessException;

/**
 * 未授权
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
public class OverAuthorizedAccessException extends BaseBusinessException {

    {
        buildParam(AuthExceptionEnum.OVER_AUTHORIZED_ACCESS.name(), 1, 0);
    }

    public OverAuthorizedAccessException() {
        super();
    }

    public OverAuthorizedAccessException(String message) {
        super(message);
    }

    public OverAuthorizedAccessException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
