package com.quinn.framework.model.deal;

import com.quinn.framework.util.enums.BpmDealTypeEnum;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
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
public class BpmAgreeParam extends AbstractBpmDealParam {

    {
        setDealType(BpmDealTypeEnum.AGREE.name());
        setTodoType(BpmTodoTypeEnum.AUDIT.name());
    }

    @Override
    protected BaseResult subValidate() {
        return BaseResult.SUCCESS;
    }

}
