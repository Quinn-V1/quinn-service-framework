package com.quinn.framework.util;

import com.quinn.framework.entity.data.BaseDO;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.MethodInvokerOneParam;
import com.quinn.util.base.api.MethodInvokerTwoParam;
import com.quinn.util.base.handler.MultiMessageResolver;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.StringKeyValue;
import com.quinn.util.constant.StringConstant;

import java.util.*;

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
    public static <T> void resolveEntities(
            Locale locale, Collection<T> dataList, MethodInvokerOneParam<T, String> keyInvoker,
            MethodInvokerTwoParam<T, String, String> descInvoker) {
        for (T data : dataList) {
            String key = keyInvoker.invoke(data);
            String desc = MultiMessageResolver.resolveString(locale, key);
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
    public static <T> void resolveEntity(Locale locale, T data, MethodInvokerOneParam<T, String> keyInvoker,
                                         MethodInvokerTwoParam<T, String, String> descInvoker) {
        String key = keyInvoker.invoke(data);
        String desc = MultiMessageResolver.resolve(locale, key);
        if (!StringUtil.isEmptyInFrame(desc)) {
            descInvoker.invoke(data, desc);
        }
    }

    /**
     * 将字符串转变为键值对
     *
     * @param keys        键
     * @param keyResolver 键解析器
     * @return 键值对列表
     */
    public static BaseResult<List<StringKeyValue>> stringToKeyValue(
            Collection<String> keys, MethodInvokerOneParam<StringKeyValue, String> keyResolver) {
        if (CollectionUtil.isEmpty(keys)) {
            // FIXME
            return BaseResult.fail();
        }

        List<StringKeyValue> result = new ArrayList<>(keys.size());
        for (String authType : keys) {
            StringKeyValue kv = new StringKeyValue();
            kv.setDataKey(authType);
            kv.setDataValue(authType);
            result.add(kv);
        }

        Locale locale = SessionUtil.getLocale();
        EntityUtil.resolveEntities(locale, result, keyResolver, (e, desc) -> {
            e.setDataValue(desc);
            return null;
        });

        return BaseResult.success(result);
    }
}
