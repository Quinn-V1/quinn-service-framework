package com.quinn.framework.exception;

import com.quinn.util.base.exception.BaseBusinessException;

/**
 * 服务层操作异常
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
public class ServiceException extends BaseBusinessException {

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
