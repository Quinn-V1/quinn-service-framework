package com.quinn.framework.model.deal;

import com.quinn.framework.api.BpmDealParamSupplier;
import com.quinn.framework.util.enums.BpmDealTypeEnum;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
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
public class BpmReadParam extends AbstractBpmDealParam {

    {
        setDealType(BpmDealTypeEnum.READ.name());
        setTodoType(BpmTodoTypeEnum.READ.name());
    }

    @Override
    protected BaseResult subValidate() {
        return BaseResult.SUCCESS;
    }

    /**
     * BPM 任务反馈参数提供器
     *
     * @author Qunhua.Liao
     * @since 2020-07-02
     */
    public static class BpmReadParamSupplier implements BpmDealParamSupplier<BpmReadParam> {

        @Override
        public BpmReadParam supply() {
            return new BpmReadParam();
        }

        @Override
        public String getDealType() {
            return BpmDealTypeEnum.READ.name();
        }
    }
}
