package com.quinn.framework.filter;

import com.quinn.framework.api.DynamicFilter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;

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
        System.out.println(authenticated);

        System.out.println(request.getServletPath());
        System.out.println(request.getContextPath());

        filterChain.doFilter(request, response);
    }

    @Override
    public String name() {
        return "pathMatch";
    }

}
