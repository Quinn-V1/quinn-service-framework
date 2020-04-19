package com.quinn.framework.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 会话信息获取类
 *
 * @author Qunhua.Liao
 * @since 2020-02-10
 */
public class SessionUtil {

    private static ThreadLocal<Map<String, Object>> content = new ThreadLocal<>();

    public static final String SESSION_KEY_USER_ID = "SESSION_KEY_USER_ID";

    public static final String SESSION_KEY_USER_KEY = "SESSION_KEY_USER_KEY";

    public static final String SESSION_KEY_ORG_ID = "SESSION_KEY_ORG_ID";

    public static final String SESSION_KEY_ORG_KEY = "SESSION_KEY_ORG_KEY";

    public static final String SESSION_KEY_LOCALE = "SESSION_KEY_LOCALE";

    public static final String SESSION_KEY_REQUEST = "SESSION_KEY_REQUEST";

    public static final Integer DEFAULT_SYSTEM_USER_ID = -1;

    public static final Integer DEFAULT_SYSTEM_ORG_ID = -1;

    public static final String DEFAULT_SYSTEM_USER_KEY = "system";

    public static final String DEFAULT_SYSTEM_ORG_KEY = "system";

    /**
     * 清除内容
     */
    public static void clear() {
        content.remove();
    }

    /**
     * 获取会话消息
     *
     * @return 会话消息
     */
    public static Map<String, Object> get() {
        return content.get();
    }

    /**
     * 设置会话消息
     *
     * @param info 会话消息
     */
    public static void set(Map<String, Object> info) {
        content.set(info);
    }

    /**
     * 通用取值
     *
     * @param key
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T getValue(String key, T defaultValue) {
        Map<String, Object> map = content.get();
        if (map == null) {
            return defaultValue;
        }

        T result = (T) map.get(key);
        if (result == null) {
            return defaultValue;
        }
        return result;
    }

    /**
     * 通用存值
     *
     * @param key
     * @param value
     */
    public static void setValue(String key, Object value) {
        Map<String, Object> map = content.get();
        if (map == null) {
            map = new HashMap<>(4);
            content.set(map);
        }
        map.put(key, value);
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public static Integer getUserId() {
        return getValue(SESSION_KEY_USER_ID, DEFAULT_SYSTEM_USER_ID);
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public static Integer getOrgId() {
        return getValue(SESSION_KEY_ORG_ID, DEFAULT_SYSTEM_ORG_ID);
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public static String getUserKey() {
        return getValue(SESSION_KEY_USER_KEY, DEFAULT_SYSTEM_USER_KEY);
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public static String getOrgKey() {
        return getValue(SESSION_KEY_ORG_KEY, DEFAULT_SYSTEM_ORG_KEY);
    }

    /**
     * 获取用户本地化对象
     *
     * @return 本地化对象
     */
    public static Locale getLocale() {
        return getValue(SESSION_KEY_LOCALE, Locale.getDefault());
    }

    /**
     * 获取请求对象
     *
     * @return 请求对象
     */
    public static HttpServletRequest getRequest() {
        return getValue(SESSION_KEY_REQUEST, null);
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public static void putUserId(Integer userId) {
        setValue(SESSION_KEY_USER_ID, userId);
    }

    /**
     * 设置用户ID
     *
     * @param orgId 组织ID
     */
    public static void putOrgId(Integer orgId) {
        setValue(SESSION_KEY_ORG_ID, orgId);
    }

    /**
     * 设置用户ID
     *
     * @param userKey 用户编码
     */
    public static void putUserKey(String userKey) {
        setValue(SESSION_KEY_USER_KEY, userKey);
    }

    /**
     * 设置用户ID
     *
     * @param orgKey 组织编码
     */
    public static void putOrgKey(String orgKey) {
        setValue(SESSION_KEY_ORG_KEY, orgKey);
    }

    /**
     * 设置用户本地化对象
     *
     * @param locale 本地化对象
     */
    public static void putLocale(Locale locale) {
        setValue(SESSION_KEY_LOCALE, locale);
    }

    /**
     * 设置请求对象
     *
     * @param request 请求对象
     */
    public static void putRequest(HttpServletRequest request) {
        setValue(SESSION_KEY_REQUEST, request);
    }

    /**
     * 获取会话
     *
     * @return 会话
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }


    /**
     * 会话存值
     *
     * @param key   键
     * @param value 值
     */
    public static void putSession(String key, Object value) {
        getRequest().getSession().setAttribute(key, value);
    }


    /**
     * 获取上下文路径
     *
     * @return 上下文路径
     */
    public static String getContentPath() {
        return getSession().getServletContext().getRealPath("/");
    }

}
