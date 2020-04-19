package com.quinn.framework.security.filter;

import com.quinn.framework.security.model.XssDefenceRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 跨站脚本攻击防御过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public class XssDefenceFilter implements Filter {

    private Set<String> excludePathRegex = new HashSet<>();

    public void setExcludePathRegex(Set<String> excludePathRegex) {
        this.excludePathRegex = excludePathRegex;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String uri = httpRequest.getRequestURI();
        for (String regex : excludePathRegex) {
            if (uri.matches(regex)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        filterChain.doFilter(new XssDefenceRequestWrapper((HttpServletRequest) servletRequest), servletResponse);
    }

    @Override
    public void destroy() {

    }
}
