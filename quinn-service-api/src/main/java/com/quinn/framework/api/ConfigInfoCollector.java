package com.quinn.framework.api;

import java.util.Properties;
import java.util.Set;

/**
 * 配置信息收集器
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public interface ConfigInfoCollector {

    /**
     * 这个收集器的优先级
     *
     * @return 优先级：越大优先级越大
     */
    default int getPriority() {
        return Integer.MIN_VALUE;
    }

    /**
     * 收集配置信息
     *
     * @param properties 加载源属性
     * @param ignoreKeys 加载过程中如果有以下属性不要覆盖
     * @return
     */
    void collect(Properties properties, Set<String> ignoreKeys);

}
