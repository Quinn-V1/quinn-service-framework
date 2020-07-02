package com.quinn.framework.model.deal;

import com.quinn.framework.api.BpmDealParamSupplier;
import com.quinn.framework.util.BpmInstParamName;
import com.quinn.framework.util.enums.BpmDealTypeEnum;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.enums.CommonMessageEnum;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
        public String getDealType() {
            return BpmDealTypeEnum.COMMUNICATE.name();
        }
    }

}
