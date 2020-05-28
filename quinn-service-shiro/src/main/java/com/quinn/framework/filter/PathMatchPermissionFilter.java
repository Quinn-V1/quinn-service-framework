package com.quinn.framework.filter;

import com.quinn.framework.api.DynamicFilter;
import com.quinn.framework.exception.UnauthorizedException;
import com.quinn.framework.util.MultiErrorHandler;
import com.quinn.framework.util.enums.AuthMessageEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 路径匹配权限控制过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
public class PathMatchPermissionFilter extends OncePerRequestFilter implements DynamicFilter {

    @Override
    public void doFilterInternal(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Subject subject = SecurityUtils.getSubject();
        boolean authenticated = subject.isAuthenticated();

        // 是否登录
        if (!authenticated) {
            MultiErrorHandler.handleError(new UnauthorizedException().ofStatusCode(HttpStatus.UNAUTHORIZED.value())
                    .buildParam(AuthMessageEnum.UNAUTHORIZED_ACCESS.name(), 0, 0)
                    .exception(), request, response);
            return;
        }

        // 是否有权限访问
        String path = request.getServletPath();
        try {
            subject.checkPermission(path);
        } catch (AuthorizationException e) {
            MultiErrorHandler.handleError(new UnauthorizedException().ofStatusCode(HttpStatus.FORBIDDEN.value())
                    .addParam(AuthMessageEnum.OVER_AUTHORIZED_ACCESS.paramNames()[0], path)
                    .exception(), request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public String name() {
        return "pathMatch";
    }

}
