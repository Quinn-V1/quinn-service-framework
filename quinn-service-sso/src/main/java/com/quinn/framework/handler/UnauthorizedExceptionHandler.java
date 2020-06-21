package com.quinn.framework.handler;

import com.quinn.framework.component.BaseBusinessExceptionHandler;
import com.quinn.framework.exception.UnauthorizedException;
import com.quinn.util.base.NumberUtil;
import com.quinn.util.base.model.BaseResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限异常
 *
 * @author Qunhua.Liao
 * @since 2020-04-29
 */
@Component("unauthorizedExceptionHandler")
public class UnauthorizedExceptionHandler extends BaseBusinessExceptionHandler<UnauthorizedException> {

    @Override
    public BaseResult handleError(UnauthorizedException e, HttpServletRequest request, HttpServletResponse response) {
        if (NumberUtil.isEmptyInFrame(e.getStatusCode())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            response.setStatus(e.getStatusCode());
        }
        return super.handleError(e, request, response);
    }

    @Override
    public Class<?> getDivClass() {
        return UnauthorizedException.class;
    }

}
