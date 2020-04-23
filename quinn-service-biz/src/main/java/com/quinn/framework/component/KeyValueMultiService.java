package com.quinn.framework.component;

import com.quinn.framework.api.KeyValueService;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.service.BaseEntityService;
import com.quinn.util.base.api.KeyValue;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.util.StringUtil;
import com.quinn.util.constant.enums.ExceptionEnum;

import java.util.List;
import java.util.Map;

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
        if (baseEntityService == null) {
            return BaseResult.fail().buildMessage(ExceptionEnum.RESULT_NOT_FOUND.name(), 0, 1)
                    .addParamI8n(ExceptionEnum.RESULT_NOT_FOUND.paramNames[0], KeyValueService.class.getName())
                    .result();
        }
        return baseEntityService.select(condition);
    }

    /**
     * 综合查询
     *
     * @param condition 基础条件
     * @return
     */
    public static <K extends KeyValue> BaseResult<List<K>> selectByMap(Map condition) {
        String dataType = (String) condition.get("dataType");
        if (StringUtil.isEmpty(dataType)) {
            return BaseResult.fail().buildMessage(ExceptionEnum.PARAM_SHOULD_NOT_NULL.name(), 1, 0)
                    .addParam(ExceptionEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "dataType")
                    .result();
        }

        KeyValueService keyValueService = SpringBeanHolder.getKeyValueService(dataType);
        if (keyValueService == null) {
            return BaseResult.fail().buildMessage(ExceptionEnum.RESULT_NOT_FOUND.name(), 0, 1)
                    .addParamI8n(ExceptionEnum.RESULT_NOT_FOUND.paramNames[0], KeyValueService.class.getName())
                    .result();
        }

        return keyValueService.selectByMap(condition);
    }

    /**
     * 指定查询
     *
     * @param dataType 数据类型
     * @param dataKey  数据编码
     * @return 通用键值对
     */
    public static <K extends KeyValue> BaseResult<List<K>> get(String dataType, String dataKey) {
        KeyValueService keyValueService = SpringBeanHolder.getKeyValueService(dataType);
        if (StringUtil.isEmpty(dataType)) {
            return BaseResult.fail().buildMessage(ExceptionEnum.PARAM_SHOULD_NOT_NULL.name(), 1, 0)
                    .addParam(ExceptionEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "dataType")
                    .result();
        }

        BaseDTO baseDTO = SpringBeanHolder.getDto(dataType, dataKey);
        if (baseDTO == null) {
            return BaseResult.fail().buildMessage(ExceptionEnum.PARAM_SHOULD_NOT_NULL.name(), 1, 0)
                    .addParam(ExceptionEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "dataType")
                    .result();
        }

        return keyValueService.select(baseDTO);
    }

}
