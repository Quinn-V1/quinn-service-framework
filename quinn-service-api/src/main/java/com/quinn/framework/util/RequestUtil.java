package com.quinn.framework.util;

import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.HttpHeadersConstant;
import com.quinn.util.constant.RegexConstant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;

/**
 * 请求解析工具类
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
public final class RequestUtil {

    private RequestUtil() {
    }

    private static final Map<String, String> DEVICES = new TreeMap<>();

    private static Set<String> deviceTypes;

    private static final String DEFAULT_DEVICE = "pc";

    static {
        DEVICES.put("ipad", "ipad");
        DEVICES.put("iphone os", "ios");
        DEVICES.put("android", "android");
        DEVICES.put("windows mobile", "wp");
        deviceTypes = DEVICES.keySet();
    }

    public static String getDomainFromRequest(HttpServletRequest request) {
        return request.getServerName();
    }

    public static String getTopDomainFromRequest(HttpServletRequest request) {
        String domain = getDomainFromRequest(request);
        Matcher matcher = RegexConstant.NET_PATTERN_TOP_DOMAIN.matcher(domain);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return domain;
    }

    public static boolean isRequestFromIntranet(HttpServletRequest request) {
        String ip = getRealIpAddr(request);
        return RegexConstant.NET_PATTERN_INTRANET.matcher(ip).find();
    }

    public static String getRealIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static String getUserAgentSummary(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }

    public static String getSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getId();
        } else {
            return null;
        }
    }

    public static Object getSessionAttribute(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(name);
        } else {
            return null;
        }
    }

    public static String detectDevice(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeadersConstant.USER_AGENT);
        if (StringUtil.isNotEmpty(userAgent)) {
            userAgent = userAgent.toLowerCase();
            for (String type : deviceTypes) {
                if (userAgent.indexOf(type) >= 0) {
                    return DEVICES.get(type);
                }
            }
            return DEFAULT_DEVICE;
        }
        return null;
    }

    /**
     * 判断是狗为Ajax请求
     *
     * @param request 请求
     * @return Ajax true
     */
    public static boolean isAjax(HttpServletRequest request) {
        if (request == null) {
            return false;
        }

        if (HttpHeadersConstant.XML_HTTP_REQUEST.equalsIgnoreCase(
                request.getHeader(HttpHeadersConstant.X_REQUESTED_WITH))) {
            return true;
        }

        String allowHeaders = request.getHeader(HttpHeadersConstant.ACCESS_CONTROL_ALLOW_HEADERS);
        if (allowHeaders != null &&
                allowHeaders.toUpperCase().contains(HttpHeadersConstant.X_REQUESTED_WIDTH.toUpperCase())
        ) {
            return true;
        }

        return false;
    }

    /**
     * 是否为Restful接口
     *
     * @return restful接口 true
     */
    public static boolean isRestful(HttpServletRequest request) {
        return "empty".equals(request.getHeader(HttpHeadersConstant.SEC_FETCH_DEST));
    }

    /**
     * 获取Cookie值
     *
     * @param request    请求
     * @param cookieName Cookie名
     * @return Cookie值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request == null && cookieName == null) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 设置Cookies
     *
     * @param response 响应
     * @param name     Cookie 名
     * @param value    Cookie值
     * @param time     时间
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int time) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");

        try {
            URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }

        cookie.setMaxAge(time);
        response.addCookie(cookie);
    }

}
