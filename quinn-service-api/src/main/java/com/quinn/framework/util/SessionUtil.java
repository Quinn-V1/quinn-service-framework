package com.quinn.framework.util;

import com.quinn.framework.api.strategy.Strategy;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.StringConstant;
import org.springframework.context.i18n.LocaleContextHolder;

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

    /**
     * 会话信息键：用户ID
     */
    public static final String SESSION_KEY_AUTH_INFO = "SESSION_KEY_AUTH_INFO";

    /**
     * 会话信息键：用户ID
     */
    public static final String SESSION_KEY_SESSION_INFO = "SESSION_KEY_SESSION_INFO";

    /**
     * 会话信息键：用户ID
     */
    public static final String SESSION_KEY_USER_ID = "SESSION_KEY_USER_ID";

    /**
     * 会话信息键：用户编码
     */
    public static final String SESSION_KEY_USER_KEY = "SESSION_KEY_USER_KEY";

    /**
     * 会话信息键：组织ID
     */
    public static final String SESSION_KEY_ORG_ID = "SESSION_KEY_ORG_ID";

    /**
     * 会话信息键：组织编码
     */
    public static final String SESSION_KEY_ORG_KEY = "SESSION_KEY_ORG_KEY";

    /**
     * 会话信息键：本地对象
     */
    public static final String SESSION_KEY_LOCALE = "SESSION_KEY_LOCALE";

    /**
     * 会话信息键：请求
     */
    public static final String SESSION_KEY_REQUEST = "SESSION_KEY_REQUEST";

    /**
     * 会话信息键：请求
     */
    public static final String SESSION_KEY_RESPONSE = "SESSION_KEY_RESPONSE";

    /**
     * 会话信息键：请求
     */
    public static final String SESSION_KEY_CURR_TENANT = "SESSION_KEY_CURR_TENANT";

    /**
     * 会话信息默认值：用户ID
     */
    public static final Long DEFAULT_SYSTEM_USER_ID = NumberConstant.NONE_OF_DATA_ID;

    /**
     * 会话信息默认值：组织ID
     */
    public static final Long DEFAULT_SYSTEM_ORG_ID = NumberConstant.NONE_OF_DATA_ID;

    /**
     * 会话信息默认值：用户编码
     */
    public static final String DEFAULT_SYSTEM_USER_KEY = StringConstant.NONE_OF_DATA;

    /**
     * 会话信息默认值：组织编码
     */
    public static final String DEFAULT_SYSTEM_ORG_KEY = StringConstant.NONE_OF_DATA;

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    @Strategy("SessionUtil.getUserId")
    public static Long getUserId() {
        return getValue(SESSION_KEY_USER_ID, DEFAULT_SYSTEM_USER_ID);
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    @Strategy("SessionUtil.getOrgId")
    public static Long getOrgId() {
        return getValue(SESSION_KEY_ORG_ID, DEFAULT_SYSTEM_ORG_ID);
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    @Strategy("SessionUtil.getUserKey")
    public static String getUserKey() {
        return getValue(SESSION_KEY_USER_KEY, DEFAULT_SYSTEM_USER_KEY);
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    @Strategy("SessionUtil.getOrgKey")
    public static String getOrgKey() {
        return getValue(SESSION_KEY_ORG_KEY, DEFAULT_SYSTEM_ORG_KEY);
    }

    /**
     * 获取用户本地化对象
     *
     * @return 本地化对象
     */
    @Strategy("SessionUtil.getLocale")
    public static Locale getLocale() {
        return LocaleContextHolder.getLocale();
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
     * 获取会话
     *
     * @return 会话
     */
    public static HttpSession getSession() {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getSession();
    }

    /**
     * 获取上下文路径
     *
     * @return 上下文路径
     */
    @Strategy("SessionUtil.getSessionValue")
    public static <T> T getSessionValue(String key) {
        HttpSession getSession = getSession();
        if (getSession == null) {
            return null;
        }
        return (T) getSession.getAttribute(key);
    }

    /**
     * 获取上下文路径
     *
     * @return 上下文路径
     */
    public static String getContentPath() {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getServletContext().getRealPath("/");
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
     * 会话存值
     *
     * @param key   键
     * @param value 值
     */
    public static boolean putSession(String key, Object value) {
        HttpSession getSession = getSession();
        if (getSession == null) {
            return false;
        }
        getSession.setAttribute(key, value);
        return true;
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
     * @param key          键
     * @param defaultValue 默认值
     * @param <T>          结果泛型
     * @return 会话线程保存值
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
     * @param key   键
     * @param value 值
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
     * 清除内容
     */
    public static void clear() {
        content.remove();
    }

    /**
     * 是否有角色
     *
     * @param roleKey
     * @return 有角色：true
     */
    public static boolean hasRole(String roleKey) {
        return true;
    }

    /**
     * 是否有超级管理员
     *
     * @return 是超级管理员：true
     */
    public static boolean isSuperAdmin() {
        return true;
    }

    /**
     * 是否有租户管理员
     *
     * @return 是租户管理员：true
     */
    public static boolean isOrgAdmin() {
        return true;
    }

    /**
     * 获取授权纤细对象
     *
     * @return 授权信息对象
     */
    public static Object getAuthInfo() {
        return getValue(SESSION_KEY_AUTH_INFO, null);
    }

    /**
     * 设置授权纤细对象
     *
     * @param object 授权信息对象
     */
    public static void setAuthInfo(Object object) {
        setValue(SESSION_KEY_AUTH_INFO, object);
    }

}
