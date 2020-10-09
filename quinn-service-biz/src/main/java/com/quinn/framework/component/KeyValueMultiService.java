package com.quinn.framework.component;

import com.quinn.framework.api.KeyValueService;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.model.PageInfo;
import com.quinn.framework.service.BaseEntityService;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.KeyValue;
import com.quinn.util.base.api.Strategy;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.CommonMessageEnum;

import java.util.*;

/**
 * 集成键值对处理器
 *
 * @author Qunhua.Liao
 * @since 2020-04-23
 */
public final class KeyValueMultiService {

    private KeyValueMultiService() {
    }

    /**
     * 综合查询
     *
     * @param condition 基础条件
     * @return
     */
    public static <K extends KeyValue> BaseResult<List<K>> select(BaseDTO condition) {
        BaseEntityService baseEntityService = SpringBeanHolder.getServiceByEntityClass(condition.getClass());
        return baseEntityService.select(condition);
    }

    /**
     * 综合查询
     *
     * @param condition 基础条件
     * @return
     */
    public static <K extends KeyValue> BaseResult<PageInfo<K>> page(BaseDTO condition) {
        BaseEntityService baseEntityService = SpringBeanHolder.getServiceByEntityClass(condition.getClass());
        return baseEntityService.page(condition);
    }

    /**
     * 综合查询
     *
     * @param condition 基础条件
     * @return
     */
    @Strategy("KeyValueMultiService.pageByMap")
    public static <K extends KeyValue> BaseResult<PageInfo<K>> pageByMap(Map condition) {
        String dataType = (String) condition.get("dataType");
        if (StringUtil.isEmpty(dataType)) {
            return BaseResult.fail().buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.key(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "dataType")
                    .result();
        }

        KeyValueService keyValueService = SpringBeanHolder.getKeyValueService(dataType);
        return keyValueService.pageByMap(condition);
    }

    /**
     * 综合查询
     *
     * @param condition 基础条件
     * @return
     */
    @Strategy("KeyValueMultiService.selectByMap")
    public static <K extends KeyValue> BaseResult<List<K>> selectByMap(Map condition) {
        String dataType = (String) condition.get("dataType");
        if (StringUtil.isEmpty(dataType)) {
            return BaseResult.fail().buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.key(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "dataType")
                    .result();
        }

        KeyValueService keyValueService = SpringBeanHolder.getKeyValueService(dataType);
        return keyValueService.selectByMap(condition);
    }

    /**
     * 指定查询
     *
     * @param dataType 数据类型
     * @param dataKey  数据编码
     * @return 通用键值对
     */
    @Strategy("KeyValueMultiService.get")
    public static <K extends KeyValue> BaseResult<K> get(String dataType, String dataKey) {
        if (StringUtil.isEmpty(dataType) || StringUtil.isEmpty(dataKey)) {
            return BaseResult.fail().buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.key(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "dataType or dataKey")
                    .result();
        }

        KeyValueService keyValueService = SpringBeanHolder.getKeyValueService(dataType);
        BaseDTO baseDTO = SpringBeanHolder.getDto(dataType, dataKey);
        if (baseDTO == null) {
            return BaseResult.fail().buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.key(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "dataType")
                    .result();
        }

        return keyValueService.get(baseDTO);
    }

    /**
     * 指定显示
     *
     * @param dataType 数据类型
     * @param dataKeys 数据编码
     * @return 通用键值对
     */
    @Strategy("KeyValueMultiService.show")
    public static BaseResult<List<KeyValue>> show(String dataType, String dataKeys) {
        if (dataKeys == null) {
            return BaseResult.fail().buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.key(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "dataKeys")
                    .result();
        }
        return shows(dataType, Arrays.asList(dataKeys.split(StringConstant.CHAR_COMMA)));
    }

    /**
     * 指定显示
     *
     * @param dataType 数据类型
     * @param dataKeys 数据编码
     * @return 通用键值对
     */
    @Strategy("KeyValueMultiService.shows")
    public static BaseResult<List<KeyValue>> shows(String dataType, Collection<String> dataKeys) {
        if (StringUtil.isEmpty(dataType) || CollectionUtil.isEmpty(dataKeys)) {
            return BaseResult.fail().buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.key(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "dataType or dataKeys")
                    .result();
        }

        KeyValueService keyValueService = SpringBeanHolder.getKeyValueService(dataType);
        Iterator<String> iterator = dataKeys.iterator();

        BaseDTO baseDTO = SpringBeanHolder.getDto(dataType, iterator.next());
        List<KeyValue> result = new ArrayList<>();
        while (iterator.hasNext()) {
            if (!baseDTO.dataKey(iterator.next())) {
                continue;
            }
            BaseResult<KeyValue> res = keyValueService.get(baseDTO);
            if (res.isSuccess()) {
                result.add(res.getData());
            }
        }
        return BaseResult.success(result);
    }

}
