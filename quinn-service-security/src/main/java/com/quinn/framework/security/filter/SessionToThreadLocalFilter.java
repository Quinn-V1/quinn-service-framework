package com.quinn.framework.security.filter;

import com.quinn.framework.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 会话信息存入ThreadLocal
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public class SessionToThreadLocalFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put(SessionUtil.SESSION_KEY_REQUEST, request);

        try {
            SessionUtil.set(infoMap);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            SessionUtil.clear();
        }
    }

    @Override
    public void destroy() {

    }

}
