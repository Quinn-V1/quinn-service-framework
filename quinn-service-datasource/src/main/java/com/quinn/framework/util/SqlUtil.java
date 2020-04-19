package com.quinn.framework.util;

import com.quinn.util.constant.CharConstant;

/**
 * Sql拼接工具类
 *
 * @author Qunhua.Liao
 * @since 2020-04-04
 */
public class SqlUtil {

    /**
     * 为字符串加单引号
     *
     * @param str 字符串
     * @return 加引号的字符串
     */
    public static String quote(String str) {
        return new StringBuilder().append(CharConstant.QUOTE).append(str).append(CharConstant.QUOTE).toString();
    }

    /**
     * 包含相似
     *
     * @param str 字符串
     * @return 相似字符串
     */
    public static String likeContain(String str) {
        return new StringBuilder().append(CharConstant.PERCENTAGE).append(str).append(CharConstant.PERCENTAGE).toString();
    }

    /**
     * 开头是
     *
     * @param str 字符串
     * @return 头相似字符串
     */
    public static String likeEnd(String str) {
        return new StringBuilder().append(str).append(CharConstant.PERCENTAGE).toString();
    }

    /**
     * 结尾是
     *
     * @param str 字符串
     * @return 尾相似字符串
     */
    public static String likeStart(String str) {
        return new StringBuilder().append(CharConstant.PERCENTAGE).append(str).toString();
    }

}
