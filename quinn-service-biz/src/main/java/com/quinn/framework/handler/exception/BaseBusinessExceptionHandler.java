package com.quinn.framework.handler.exception;

import com.quinn.framework.model.DefaultErrorHandler;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.base.handler.MultiMessageResolver;
import com.quinn.util.base.model.BaseResult;
import org.springframework.stereotype.Component;

/**
 * 基础业务类
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
@Component("baseBusinessExceptionHandler")
public class BaseBusinessExceptionHandler extends DefaultErrorHandler {

    @Override
    public void generateMessage(Exception e, BaseResult result) {
        String message = MultiMessageResolver.resolveMessageProp(SessionUtil.getLocale(),
                ((BaseBusinessException) e).getMessageProp());
        result.setMessage(StringUtil.isEmpty(message) ? e.getMessage() : message);
    }

    @Override
    public Class<?> getDivClass() {
        return BaseBusinessException.class;
    }
}
