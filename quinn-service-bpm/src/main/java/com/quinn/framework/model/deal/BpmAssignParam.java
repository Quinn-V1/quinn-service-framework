package com.quinn.framework.model.deal;

import com.quinn.framework.api.BpmDealParamSupplier;
import com.quinn.framework.util.BpmInstParamName;
import com.quinn.framework.util.enums.BpmDealTypeEnum;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.enums.CommonMessageEnum;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * BPM 任务转办参数
 *
 * @author Qunhua.Liao
 * @since 2020-05-01
 */
@Getter
@Setter
public class BpmAssignParam extends AbstractBpmDealParam {

    {
        setDealType(BpmDealTypeEnum.ASSIGN.name());
        setTodoType(BpmTodoTypeEnum.AUDIT.name());
    }

    /**
     * 转办用户编码
     */
    @ApiModelProperty("转办用户编码")
    private String toUserKey;

    @Override
    protected BaseResult subValidate() {
        if (StringUtil.isEmptyInFrame(toUserKey)) {
            return BaseResult.fail()
                    .buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.key(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], BpmInstParamName.TO_USER_KEY)
                    .result();
        }

        return BaseResult.SUCCESS;
    }

    @Override
    public void initWithParam(ComplexDealParam param) {
        super.initWithParam(param);
        setToUserKey(param.getToUserKey());
    }

    /**
     * BPM 任务转办参数
     *
     * @author Qunhua.Liao
     * @since 2020-07-02
     */
    public static class BpmAssignParamSupplier implements BpmDealParamSupplier<BpmAssignParam> {

        @Override
        public BpmAssignParam supply() {
            return new BpmAssignParam();
        }

        @Override
        public String[] getDealTypes() {
            return new String[] {BpmDealTypeEnum.ASSIGN.name()};
        }
    }

}
