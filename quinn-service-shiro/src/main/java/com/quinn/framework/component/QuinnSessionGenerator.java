package com.quinn.framework.component;

import com.quinn.framework.util.RequestUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 会话生成器
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public class QuinnSessionGenerator implements SessionIdGenerator {

    private String sessionIdCookieName;

    public QuinnSessionGenerator(String sessionIdCookieName) {
        this.sessionIdCookieName = sessionIdCookieName;
    }

    @Override
    public Serializable generateId(Session session) {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        return RequestUtil.getCookieValue(request, sessionIdCookieName);
    }
}
