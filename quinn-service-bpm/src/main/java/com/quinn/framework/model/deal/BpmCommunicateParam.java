package com.quinn.framework.model.deal;

import com.quinn.framework.api.BpmDealParamSupplier;
import com.quinn.framework.util.BpmInstParamName;
import com.quinn.framework.util.enums.BpmDealTypeEnum;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.enums.CommonMessageEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * BPM 任务沟通、抄送参数
 *
 * @author Qunhua.Liao
 * @since 2020-05-01
 */
@Getter
@Setter
public class BpmCommunicateParam extends AbstractBpmDealParam {

    {
        setDealType(BpmDealTypeEnum.COMMUNICATE.name());
        setTodoType(BpmTodoTypeEnum.AUDIT.name());
    }

    /**
     * 沟通用户编码（可多个）
     */
    @ApiModelProperty("沟通用户编码")
    private String[] toUserKeys;

    @Override
    public void initWithParam(ComplexDealParam param) {
        super.initWithParam(param);
        setToUserKeys(param.getToUserKeys());
    }

    @Override
    public void initWithMap(Map<String, Object> param) {
        super.initWithMap(param);
        Object o = param.get(BpmInstParamName.TO_USER_KEYS);
        if (o != null) {
            List<String> strings = CollectionUtil.toArray(o, String.class);
            String[] toUserKeys = strings.toArray(new String[0]);
            setToUserKeys(toUserKeys);
        }
    }

    @Override
    protected BaseResult subValidate() {
        if (CollectionUtil.isEmpty(toUserKeys)) {
            return BaseResult.fail()
                    .buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.name(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], BpmInstParamName.TO_USER_KEYS)
                    .result();
        }

        if (StringUtil.isEmpty(getSuggestion())) {
            return BaseResult.fail()
                    .buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.name(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], BpmInstParamName.SUGGESTION)
                    .result();
        }

        return BaseResult.SUCCESS;
    }

    /**
     * BPM 任务沟通、抄送参数提供器
     *
     * @author Qunhua.Liao
     * @since 2020-07-02
     */
    public static class BpmCommunicateParamSupplier implements BpmDealParamSupplier<BpmCommunicateParam> {

        @Override
        public BpmCommunicateParam supply() {
            return new BpmCommunicateParam();
        }

        @Override
        public String[] getDealTypes() {
            return new String[]{BpmDealTypeEnum.COMMUNICATE.name(), BpmDealTypeEnum.COPY.name()};
        }
    }

}
