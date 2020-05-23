package com.quinn.framework.filter;

import com.quinn.framework.api.DynamicFilter;
import com.quinn.framework.util.RequestUtil;
import org.apache.shiro.web.filter.authc.AnonymousFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 内部访问过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
public class QuinnIntranetFilter extends AnonymousFilter implements DynamicFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (RequestUtil.isRequestFromIntranet((HttpServletRequest) request)) {
            return super.onPreHandle(request, response, mappedValue);
        } else {
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            try {
                servletResponse.setContentType("text/html;charset=utf8");
                servletResponse.getWriter().println("<h1>框架资源只能从内网访问, 请使用\"127.0.0.1\"替代\"localhost\"</h1>");
            } catch (IOException e) {
            }
            return false;
        }
    }

    @Override
    public String name() {
        return "intranet";
    }
}
