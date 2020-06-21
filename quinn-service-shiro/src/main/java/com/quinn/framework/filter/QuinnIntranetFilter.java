package com.quinn.framework.filter;

import com.quinn.framework.api.DynamicFilter;
import com.quinn.framework.exception.UnauthorizedException;
import com.quinn.framework.util.MultiErrorHandler;
import com.quinn.framework.util.RequestUtil;
import com.quinn.framework.util.enums.AuthMessageEnum;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 内部访问过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
public class QuinnIntranetFilter extends AnonymousFilter implements DynamicFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (RequestUtil.isRequestFromIntranet(req)) {
            return super.onPreHandle(request, response, mappedValue);
        } else {
            MultiErrorHandler.handleError(
                    new UnauthorizedException().ofStatusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                            .buildParam(AuthMessageEnum.INTRANET_RESOURCE_ACCESS_FROM_OUT.key(), 0, 0)
                            .exception()
                    , req, res);
            return false;
        }
    }

    @Override
    public String name() {
        return "intranet";
    }
}
