package com.quinn.framework.handler;

import com.quinn.framework.api.ErrorHandler;
import com.quinn.framework.model.DefaultErrorHandler;
import com.quinn.framework.util.enums.AuthMessageEnum;
import com.quinn.util.base.model.BaseResult;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.stereotype.Component;

/**
 * 密码错误异常
 *
 * @author Qunhua.Liao
 * @since 2020-05-29
 */
@Component("incorrectCredentialsExceptionHandler")
public class IncorrectCredentialsExceptionHandler extends DefaultErrorHandler<IncorrectCredentialsException>
        implements ErrorHandler<IncorrectCredentialsException> {

    @Override
    public void generateMessage(
            IncorrectCredentialsException e, BaseResult result) {
        result.buildMessage(AuthMessageEnum.AUTH_CREDENTIALS_MISMATCH.name(), 0, 0);
    }

    @Override
    public Class<?> getDivClass() {
        return IncorrectCredentialsException.class;
    }

}
