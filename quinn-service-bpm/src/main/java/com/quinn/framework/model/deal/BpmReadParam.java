package com.quinn.framework.model.deal;

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

}
