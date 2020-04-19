package com.quinn.framework.component;

import com.quinn.framework.api.ParamResolver;
import com.quinn.util.constant.enums.ParamTypeEnum;
import com.quinn.util.base.model.BaseResult;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Sql参数解析器
 *
 * @author Qunhua.Liao
 * @since 2020-02-11
 */
@Service("paramResolverJSON")
public class ParamResolverJson implements ParamResolver {

    @Override
    public BaseResult<Map<String, Object>> resolve(Map<String, Object> messageParam) {
        Map<String, Object> paramContent = (Map<String, Object>) messageParam.remove(ParamTypeEnum.PARAM_KEY_DATA);
        if (paramContent != null) {
            messageParam.putAll(paramContent);
        }
        return BaseResult.success(messageParam);
    }

}
