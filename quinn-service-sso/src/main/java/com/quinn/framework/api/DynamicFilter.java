package com.quinn.framework.api;

import javax.servlet.Filter;

/**
 * 动态配置的过滤器
 *
 * @author Qunhua.liao
 * @since 2020-05-23
 */
public interface DynamicFilter extends Filter {

    /**
     * 获取名字
     *
     * @return
     */
    String name();

}
