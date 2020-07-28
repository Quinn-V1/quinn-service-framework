package com.quinn.framework.model.deal;

import com.quinn.framework.api.BpmDealParamSupplier;
import com.quinn.framework.util.BpmInstParamName;
import com.quinn.framework.util.enums.BpmDealTypeEnum;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.constant.enums.CommonMessageEnum;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * BPM 任务同意参数
 *
 * @author Qunhua.Liao
 * @since 2020-05-01
 */
@Getter
@Setter
public class BpmRejectParam extends AbstractBpmDealParam {

    {
        setDealType(BpmDealTypeEnum.REJECT.name());
        setTodoType(BpmTodoTypeEnum.AUDIT.name());
    }

    /**
     * 驳回节点编码
     */
    @ApiModelProperty("驳回节点编码")
    private String toNodeKey;

    @Override
    public void initWithParam(ComplexDealParam param) {
        super.initWithParam(param);
        setToNodeKey(param.getToNodeKey());
    }

    @Override
    public void initWithMap(Map<String, Object> param) {
        super.initWithMap(param);
        setToNodeKey(BaseConverter.staticToString(param.get(BpmInstParamName.TO_NODE_KEY)));
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
     * BPM 任务同意参数
     *
     * @author Qunhua.Liao
     * @since 2020-05-01
     */
    public static class BpmRejectParamSupplier implements BpmDealParamSupplier<BpmRejectParam> {

        @Override
        public BpmRejectParam supply() {
            return new BpmRejectParam();
        }

        @Override
        public String[] getDealTypes() {
            return new String[] {BpmDealTypeEnum.REJECT.name()};
        }
    }

}
