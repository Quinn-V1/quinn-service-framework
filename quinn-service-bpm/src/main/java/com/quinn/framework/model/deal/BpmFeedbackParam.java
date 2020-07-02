package com.quinn.framework.model.deal;

import com.quinn.framework.api.BpmDealParamSupplier;
import com.quinn.framework.util.BpmInstParamName;
import com.quinn.framework.util.enums.BpmDealTypeEnum;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.enums.CommonMessageEnum;
import com.quinn.util.base.model.BaseResult;
import lombok.Getter;
import lombok.Setter;

/**
 * BPM 任务反馈参数
 *
 * @author Qunhua.Liao
 * @since 2020-05-01
 */
@Getter
@Setter
public class BpmFeedbackParam extends AbstractBpmDealParam {

    {
        setDealType(BpmDealTypeEnum.FEEDBACK.name());
        setTodoType(BpmTodoTypeEnum.FEEDBACK.name());
    }

    @Override
    protected BaseResult subValidate() {
        if (StringUtil.isEmpty(getSuggestion())) {
            return BaseResult.fail()
                    .buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.key(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], BpmInstParamName.SUGGESTION)
                    .result();
        }

        return BaseResult.SUCCESS;
    }

    /**
     * BPM 任务反馈参数提供器
     *
     * @author Qunhua.Liao
     * @since 2020-07-02
     */
    public static class BpmFeedbackParamSupplier implements BpmDealParamSupplier<BpmFeedbackParam> {

        @Override
        public BpmFeedbackParam supply() {
            return new BpmFeedbackParam();
        }

        @Override
        public String getDealType() {
            return BpmDealTypeEnum.FEEDBACK.name();
        }
    }

}
