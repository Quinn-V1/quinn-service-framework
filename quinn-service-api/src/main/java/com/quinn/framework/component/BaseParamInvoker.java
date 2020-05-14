package com.quinn.framework.component;

import com.quinn.framework.api.ParamInvoker;
import com.quinn.framework.api.ParamResolver;
import com.quinn.util.constant.enums.ParamTypeEnum;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.CollectionUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 消息参数业务实现接口
 *
 * @author Qunhua.Liao
 * @since 2020-02-11
 */
@Service("paramInvoker")
public class BaseParamInvoker implements ParamInvoker {

    @Resource
    private Map<String, ParamResolver> resolverMap;

    @Override
    public BaseResult<Map<String, Object>> invoke(Map<String, Object> messageParam) {
        String paramType = (String) messageParam.remove(ParamTypeEnum.PARAM_KEY_TYPE);

        // 处理时间
        handleTime(messageParam, ParamTypeEnum.PARAM_KEY_THIS_EXEC, LocalDateTime.now());
        handleTime(messageParam, ParamTypeEnum.PARAM_KEY_LAST_SUCCESS, null);
        handleTime(messageParam, ParamTypeEnum.PARAM_KEY_LAST_EXEC, null);
        handleTime(messageParam, ParamTypeEnum.PARAM_KEY_LAST_FAIL, null);

        if (StringUtils.isEmpty(paramType) || CollectionUtil.isEmpty(resolverMap)) {
            Map<String, Object> paramContent = (Map<String, Object>) messageParam.remove(ParamTypeEnum.PARAM_KEY_DATA);
            if (paramContent != null) {
                messageParam.putAll(paramContent);
            }
        }

        ParamResolver paramResolver = resolverMap.get(ParamTypeEnum.PARAM_RESOLVER_BEAN_PREFIX + paramType);
        if (paramResolver != null) {
            return paramResolver.resolve(messageParam);
        }

        return BaseResult.success(messageParam);
    }

    /**
     * 处理时间
     *
     * @param messageParam 参数
     * @param timeType     时间类型
     * @param defaultTime  默认时间
     * @return 处理结果
     */
    private BaseResult handleTime(Map<String, Object> messageParam, String timeType, LocalDateTime defaultTime) {
        Object o = messageParam.get(timeType);
        if (o == null && defaultTime != null) {
            messageParam.put(timeType, defaultTime);
        } else {
            if (o instanceof String) {
                messageParam.put(timeType, BaseConverter.staticConvert(o, LocalDateTime.class));
            }
        }
        return new BaseResult();
    }

}
