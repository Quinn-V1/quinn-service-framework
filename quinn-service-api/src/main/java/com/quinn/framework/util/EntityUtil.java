package com.quinn.framework.util;

import com.quinn.framework.component.DegreeCacheService;
import com.quinn.framework.entity.data.BaseDO;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.MethodInvokerOneParam;
import com.quinn.util.base.api.MethodInvokerTwoParam;
import com.quinn.util.constant.StringConstant;

import java.util.Collection;
import java.util.Map;

/**
 * 实体工具类
 *
 * @author Qunhua.Liao
 * @since 2020-03-20
 */
public final class EntityUtil {

    private static DegreeCacheService<String> messageDegreeCacheService;

    public static void setMessageDegreeCacheService(DegreeCacheService<String> messageDegreeCacheService) {
        EntityUtil.messageDegreeCacheService = messageDegreeCacheService;
    }

    private EntityUtil() {
    }

    /**
     * 包装 Key
     *
     * @param locale   语言
     * @param cacheKey 数据编码
     * @return 综合编码
     */
    public static String wrapperKey(String locale, String cacheKey, String delimiter) {
        StringBuilder query = new StringBuilder();
        query.append(locale).append(delimiter);

        // 如果含冒号，说明是实体的 cacheKey
        if (!cacheKey.contains(BaseDTO.CACHE_KEY_DELIMITER)) {
            query.append(StringConstant.DATA_TYPE_OF_MESSAGE).append(delimiter);
        } else {
            cacheKey = cacheKey.replaceFirst(BaseDTO.CACHE_KEY_DELIMITER, delimiter);
        }

        if (!cacheKey.contains(BaseDTO.PROPERTY_DELIMITER)) {
            query.append(cacheKey).append(delimiter).append(StringConstant.NONE_OF_DATA);
        } else {
            cacheKey = cacheKey.replace(BaseDTO.PROPERTY_DELIMITER, delimiter);
            query.append(cacheKey);
        }

        return query.toString();
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

    /**
     * 数据国际化
     *
     * @param dataList    数据
     * @param keyInvoker  获取键
     * @param descInvoker 设置描述
     */
    public static <T> void resolveEntities(Collection<T> dataList, MethodInvokerOneParam<T, String> keyInvoker,
                                           MethodInvokerTwoParam<T, String, String> descInvoker) {
        if (messageDegreeCacheService == null) {
            return;
        }

        for (T data : dataList) {
            String key = keyInvoker.invoke(data);
            String desc = messageDegreeCacheService.get(key);
            if (!StringUtil.isEmptyInFrame(desc)) {
                descInvoker.invoke(data, desc);
            }
        }
    }

    /**
     * 数据国际化
     *
     * @param data        数据
     * @param keyInvoker  获取键
     * @param descInvoker 设置描述
     */
    public static <T> void resolveEntity(T data, MethodInvokerOneParam<T, String> keyInvoker,
                                         MethodInvokerTwoParam<T, String, String> descInvoker) {
        if (messageDegreeCacheService == null) {
            return;
        }

        String key = keyInvoker.invoke(data);
        String desc = messageDegreeCacheService.get(key);
        if (!StringUtil.isEmptyInFrame(desc)) {
            descInvoker.invoke(data, desc);
        }
    }

}
