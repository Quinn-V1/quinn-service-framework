package com.quinn.framework.util;

import com.quinn.framework.component.SpringBeanHolder;

/**
 * Spring Bean 工具
 *
 * @author Qunhua.Liao
 * @since 2020-05-19
 */
public final class SpringBeanUtil {

    private SpringBeanUtil() {
    }

    /**
     * 获取Spring Bean
     *
     * @param name 名称
     * @param <T>  类型泛型
     * @return Bean
     */
    public static <T> T getBean(String name) {
        return SpringBeanHolder.getBean(name);
    }

}
