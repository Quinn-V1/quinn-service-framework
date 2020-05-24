package com.quinn.framework.exception;

import com.quinn.util.base.enums.ExceptionEnum;

/**
 * 数据操作事务异常
 *
 * @author Qunhua.Liao
 * @since 2020-03-20
 */
public class DataOperationTransactionException extends ServiceException {

    {
        buildParam(ExceptionEnum.DATA_OPERATION_TRANSACTION_TERMINATED.name(), 2, 2);
    }

    public DataOperationTransactionException() {
        super();
    }

    public DataOperationTransactionException(String message) {
        super(message);
    }

    public DataOperationTransactionException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
