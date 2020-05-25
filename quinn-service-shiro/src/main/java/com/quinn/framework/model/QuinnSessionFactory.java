package com.quinn.framework.model;

import com.quinn.framework.util.RequestUtil;
import com.quinn.util.constant.HttpHeadersConstant;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.licence.model.ApplicationInfo;
import org.apache.shiro.ShiroException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Shiro 会话工厂
 *
 * @author Quinn.Liao
 * @since 2020-05-21
 */
public class QuinnSessionFactory implements SessionFactory {

    @Override
    public Session createSession(SessionContext initData) {
        QuinnSession session = new QuinnSession();

        if (initData != null && initData instanceof WebSessionContext) {
            WebSessionContext sessionContext = (WebSessionContext) initData;
            HttpServletRequest request = (HttpServletRequest) sessionContext.getServletRequest();

            if (request == null) {
                request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            }

            if (request == null) {
                throw new ShiroException("request is null");
            }

            session.setHost(RequestUtil.getRealIpAddr(request));
            session.setUserAgent(request.getHeader(HttpHeadersConstant.USER_AGENT));
            session.setSystemHost(ApplicationInfo.getAppKey());
        }
        return session;
    }

}