package com.quinn.framework.exception;

import com.quinn.util.constant.enums.CommonMessageEnum;

/**
 * 数据操作事务异常
 *
 * @author Qunhua.Liao
 * @since 2020-03-20
 */
public class DataOperationTransactionException extends ServiceException {

    {
        buildParam(CommonMessageEnum.DATA_OPERATION_TRANSACTION_TERMINATED.key(), 2, 2);
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
