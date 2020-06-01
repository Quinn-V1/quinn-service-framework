package com.quinn.framework.component;

import com.quinn.framework.util.SessionUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

/**
 * 会话管理器
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public class QuinnSessionManager extends DefaultWebSessionManager {

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Session session = SessionUtil.getValue(SessionUtil.SESSION_KEY_SESSION_INFO, null);
        if (session == null) {
            session = super.retrieveSession(sessionKey);
        }
        return session;
    }

}