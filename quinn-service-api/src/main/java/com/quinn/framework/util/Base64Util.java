package com.quinn.framework.util;

import com.quinn.framework.api.strategy.Strategy;
import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.StringConstant;
import lombok.SneakyThrows;
import org.springframework.util.Base64Utils;

/**
 * Base64加密解密
 *
 * @author Qunhua.Liao
 * @since 2020-06-19
 */
public class Base64Util {

    /**
     * 基础校验授权
     */
    public static final String BASIC_PREFIX = "Basic ";

    private Base64Util() {
    }

    /**
     * 加密字符串
     *
     * @param userName 源字符串
     * @param password 源字符串
     * @return 加密字符串
     */
    @SneakyThrows
    @Strategy("Base64Util.encodeAuth")
    public static String encodeAuth(String userName, String password) {
        if (userName == null && password == null) {
            return null;
        }
        return BASIC_PREFIX + Base64Utils.encodeToString(
                StringUtil.getBytes(userName + StringConstant.CHAR_COLON + password));
    }

    /**
     * 加密字符串
     *
     * @param str 源字符串
     * @return 加密字符串
     */
    @SneakyThrows
    @Strategy("Base64Util.encode")
    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        return Base64Utils.encodeToString(StringUtil.getBytes(str));
    }

    /**
     * 加密字符串
     *
     * @param str 源字符串
     * @return 加密字符串
     */
    @SneakyThrows
    @Strategy("Base64Util.decode")
    public static String decode(String str) {
        if (str == null) {
            return null;
        }
        return StringUtil.forBytes(Base64Utils.decodeFromString(str));
    }

}
