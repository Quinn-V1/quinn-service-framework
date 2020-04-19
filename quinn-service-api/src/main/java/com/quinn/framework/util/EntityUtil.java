package com.quinn.framework.util;

import com.quinn.framework.entity.data.BaseDO;
import com.quinn.framework.entity.dto.BaseDTO;

import java.util.Map;

/**
 * 实体工具类
 *
 * @author Qunhua.Liao
 * @since 2020-03-20
 */
public final class EntityUtil {

    private EntityUtil() {
    }

    /**
     * 获取一个对象缓存键
     *
     * @param object 对象
     * @return 缓存键
     */
    public static final String cacheKeyOf(Object object) {
        if (object instanceof BaseDTO) {
            return ((BaseDTO) object).cacheKey();
        }

        if (object instanceof BaseDO) {
            return ((BaseDO) object).cacheKey();
        }

        if (object instanceof Map) {
            return (String) ((Map) object).get("cacheKey");
        }

        return null;
    }

}
