package com.quinn.framework.model.deal;

import com.quinn.framework.api.BpmDealParamSupplier;
import com.quinn.framework.util.BpmInstParamName;
import com.quinn.framework.util.enums.BpmDealTypeEnum;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import com.quinn.util.base.NumberUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.enums.CommonMessageEnum;
import com.quinn.util.base.model.BaseResult;
import lombok.Getter;
import lombok.Setter;

/**
 * BPM 任务同意参数
 *
 * @author Qunhua.Liao
 * @since 2020-05-01
 */
@Getter
@Setter
public class BpmRevokeParam extends AbstractBpmDealParam {

    {
        setDealType(BpmDealTypeEnum.REVOKE.name());
        setTodoType(BpmTodoTypeEnum.AUDIT.name());
    }

    @Override
    public BaseResult validate() {
        if (NumberUtil.isEmptyInFrame(getInstanceId())) {
            return BaseResult.fail()
                    .buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.key(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], BpmInstParamName.INSTANCE_ID)
                    .result();
        }

        if (StringUtil.isEmpty(getSuggestion())) {
            return BaseResult.fail()
                    .buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.key(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], BpmInstParamName.SUGGESTION)
                    .result();
        }
        return subValidate();
    }

    @Override
    protected BaseResult subValidate() {
        return BaseResult.SUCCESS;
    }

    /**
     * BPM 任务同意参数提供器
     *
     * @author Qunhua.Liao
     * @since 2020-05-01
     */
    public static class BpmRevokeParamSupplier implements BpmDealParamSupplier<BpmRevokeParam> {

        @Override
        public BpmRevokeParam supply() {
            return new BpmRevokeParam();
        }

        @Override
        public String[] getDealTypes() {
            return new String[] {BpmDealTypeEnum.REVOKE.name()};
        }
    }
}
