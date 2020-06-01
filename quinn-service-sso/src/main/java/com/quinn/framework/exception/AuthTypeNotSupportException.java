package com.quinn.framework.exception;

import com.quinn.framework.util.enums.AuthMessageEnum;
import com.quinn.util.base.exception.BaseBusinessException;

/**
 * 校验类型不支持
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public class AuthTypeNotSupportException extends BaseBusinessException {

    {
        buildParam(AuthMessageEnum.AUTH_TYPE_NOT_SUPPORT.key(), 0, 2);
    }

    public AuthTypeNotSupportException() {
        super();
    }

    public AuthTypeNotSupportException(String message) {
        super(message);
    }

    public AuthTypeNotSupportException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
