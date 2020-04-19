package com.quinn.framework.exception;

/**
 * 重复删除异常
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
public class RepeatDeleteException extends ServiceException {

    public RepeatDeleteException() {
        super();
    }

    public RepeatDeleteException(String message) {
        super(message);
    }

    public RepeatDeleteException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
