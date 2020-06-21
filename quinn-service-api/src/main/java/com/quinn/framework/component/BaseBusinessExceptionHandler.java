package com.quinn.framework.component;

import com.quinn.framework.model.DefaultErrorHandler;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.NumberUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.base.handler.MultiMessageResolver;
import com.quinn.util.base.model.BaseResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 基础业务类
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
@Component("baseBusinessExceptionHandler")
public class BaseBusinessExceptionHandler<T extends BaseBusinessException> extends DefaultErrorHandler<T> {

    @Override
    public void generateMessage(T e, BaseResult result) {
        String message = MultiMessageResolver.resolveMessageProp(SessionUtil.getLocale(),
                e.getMessageProp());
        result.setMessage(StringUtil.isEmpty(message) ? e.getMessage() : message);
    }

    @Override
    public Class<?> getDivClass() {
        return BaseBusinessException.class;
    }
}
