package com.quinn.framework.model;

import org.apache.shiro.session.mgt.SimpleSession;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 自定义会话
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public class QuinnSession extends SimpleSession implements Serializable {

    public QuinnSession() {
        super();
        this.setChanged(true);
    }

    public QuinnSession(String host) {
        super(host);
        this.setChanged(true);
    }

    /**
     * 是否变更
     */
    private boolean isChanged = false;

    /**
     * 用户登录时系统IP
     */
    private String systemHost;

    /**
     * 用户浏览器类型
     */
    private String userAgent;

    /**
     * 在线状态
     */
    private OnlineStatusEnum status = OnlineStatusEnum.ON_LINE;

    @Override
    public void setId(Serializable id) {
        super.setId(id);
        this.setChanged(true);
    }

    @Override
    public void setStopTimestamp(Date stopTimestamp) {
        super.setStopTimestamp(stopTimestamp);
        this.setChanged(true);
    }

    @Override
    public void setExpired(boolean expired) {
        super.setExpired(expired);
        this.setChanged(true);
    }

    @Override
    public void setTimeout(long timeout) {
        super.setTimeout(timeout);
        this.setChanged(true);
    }

    @Override
    public void setHost(String host) {
        super.setHost(host);
        this.setChanged(true);
    }

    @Override
    public void setAttributes(Map<Object, Object> attributes) {
        super.setAttributes(attributes);
        this.setChanged(true);
    }

    @Override
    public void setAttribute(Object key, Object value) {
        super.setAttribute(key, value);
        this.setChanged(true);
    }

    @Override
    public Object removeAttribute(Object key) {
        this.setChanged(true);
        return super.removeAttribute(key);
    }

    @Override
    public Object getAttribute(Object key) {
        Object attribute = super.getAttribute(key);
        return attribute;
    }

    @Override
    public void stop() {
        super.stop();
        this.setChanged(true);
    }

    @Override
    protected void expire() {
        this.stop();
        this.setExpired(true);
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected boolean onEquals(SimpleSession ss) {
        return super.onEquals(ss);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public OnlineStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OnlineStatusEnum status) {
        this.status = status;
    }

    public String getSystemHost() {
        return systemHost;
    }

    public void setSystemHost(String systemHost) {
        this.systemHost = systemHost;
    }

    public enum OnlineStatusEnum {

        // 在线状态
        ON_LINE("在线"),

        // 隐身状态
        HIDDEN("隐身"),

        // 强制退出
        FORCE_LOGOUT("强制退出");

        private final String info;

        OnlineStatusEnum(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }

    }

}