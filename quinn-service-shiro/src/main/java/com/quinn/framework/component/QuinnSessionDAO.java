package com.quinn.framework.component;

import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.framework.model.QuinnSession;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 会话获取接口
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public class QuinnSessionDAO extends AbstractSessionDAO {

    private static final String DEFAULT_SESSION_KEY_PREFIX = "shiro:session:";

    private String keyPrefix = DEFAULT_SESSION_KEY_PREFIX;

    private static final long DEFAULT_SESSION_IN_MEMORY_TIMEOUT = 1800L;

    private long sessionInMemoryTimeout = DEFAULT_SESSION_IN_MEMORY_TIMEOUT;

    private static final int DEFAULT_EXPIRE = -2;

    private static final int NO_EXPIRE = -1;

    private int expire = DEFAULT_EXPIRE;

    private static final int MILLISECONDS_IN_A_SECOND = 1000;

    private CacheAllService cacheAllService;

    public QuinnSessionDAO(CacheAllService cacheAllService) {
        this.cacheAllService = cacheAllService;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        try {
            if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
                return;
            }

            if (session instanceof QuinnSession) {
                QuinnSession ss = (QuinnSession) session;
                if (!ss.isChanged()) {
                    return;
                }
                ss.setChanged(false);
            }

            this.saveSession(session);
        } catch (Exception e) {
        }
    }

    @Override
    protected Serializable doCreate(Session session) {
        if (session == null) {
            throw new UnknownSessionException("session is null");
        }

        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        Session s = null;

        try {
            String realKey = getRedisSessionKey(sessionId);
            s = this.cacheAllService.get(realKey);
            if (s != null) {
                this.cacheAllService.set(realKey, s, expire);
            }
        } catch (Exception e) {
        }

        return s;
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }

        try {
            this.cacheAllService.delete(getRedisSessionKey(session.getId()));
        } catch (Exception e) {
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<Session>();
        try {
            Set<String> keys = cacheAllService.keys(this.keyPrefix + "*");
            if (keys != null && keys.size() > 0) {
                for (String key : keys) {
                    Session s = cacheAllService.get(key, Session.class);
                    sessions.add(s);
                }
            }
        } catch (Exception e) {
        }
        return sessions;
    }

    private void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            throw new UnknownSessionException("session or session id is null");
        }

        String key = getRedisSessionKey(session.getId());
        if (expire == DEFAULT_EXPIRE) {
            this.cacheAllService.set(key, session, (int) (session.getTimeout() / MILLISECONDS_IN_A_SECOND));
            return;
        }

        if (expire != NO_EXPIRE && expire * MILLISECONDS_IN_A_SECOND < session.getTimeout()) {
        }
        this.cacheAllService.set(key, session, expire);
    }

    private String getRedisSessionKey(Serializable sessionId) {
        return this.keyPrefix + sessionId;
    }

    public Long getActiveSessionsSize() {
        Long size = 0L;
        try {
            int len = this.cacheAllService.size(this.keyPrefix + "*");
            size = new Long(len);
        } catch (Exception e) {
        }
        return size;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public long getSessionInMemoryTimeout() {
        return sessionInMemoryTimeout;
    }

    public void setSessionInMemoryTimeout(long sessionInMemoryTimeout) {
        this.sessionInMemoryTimeout = sessionInMemoryTimeout;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

}