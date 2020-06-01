package com.quinn.framework.filter;

import com.quinn.framework.util.SessionUtil;
import lombok.SneakyThrows;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 会话信息处理过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-06-01
 */
public class SessionInfoFilter implements Filter {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        SessionUtil.clear();
        SessionUtil.setValue(SessionUtil.SESSION_KEY_REQUEST, servletRequest);
        SessionUtil.setValue(SessionUtil.SESSION_KEY_RESPONSE, servletResponse);
        filterChain.doFilter(servletRequest, servletResponse);
        SessionUtil.clear();
    }

}
