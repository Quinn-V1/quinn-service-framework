package com.quinn.framework.exception;

import com.quinn.framework.util.enums.AuthMessageEnum;
import com.quinn.util.base.exception.BaseBusinessException;

/**
 * 未授权
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
public class AuthInfoNotFoundException extends BaseBusinessException {

    {
        buildParam(AuthMessageEnum.AUTH_INFO_NOT_FOUND.key(), 0, 0);
    }

    public AuthInfoNotFoundException() {
        super();
    }

    public AuthInfoNotFoundException(String message) {
        super(message);
    }

    public AuthInfoNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
