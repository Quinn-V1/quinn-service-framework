package com.quinn.framework.model;

import com.quinn.framework.entity.data.BaseDO;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.util.base.api.MethodInvokerOneParam;
import com.quinn.util.constant.StringConstant;

/**
 * 基础实体消息键解析器
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public class BaseDoMsgResolver implements MethodInvokerOneParam<BaseDO, String> {

    public BaseDoMsgResolver() {
        this(StringConstant.NONE_OF_DATA);
    }

    public BaseDoMsgResolver(String propName) {
        this.propName = propName;
    }

    private String propName;

    @Override
    public String invoke(BaseDO baseDO) {
        return baseDO.cacheKey() + BaseDTO.PROPERTY_DELIMITER + propName;
    }

}
