package com.quinn.framework.filter;

import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Swagger-UI文档禁用过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
public class SwaggerDocumentationDisabledFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        byte[] responseData = "<h1>Swagger rest documentation is disabled</h1>".getBytes();
        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setContentLength(responseData.length);
        httpServletResponse.getOutputStream().write(responseData);
    }

    @Override
    public void destroy() {

    }
}
