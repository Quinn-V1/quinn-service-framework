package com.quinn.framework.model.deal;

import com.quinn.framework.util.enums.BpmDealTypeEnum;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 综合处理参数
 *
 * @author Qunhua.Liao
 * @since 2020-07-02
 */
@Setter
@Getter
public class ComplexDealParam extends AbstractBpmDealParam {

    /**
     * 沟通用户编码（可多个）
     */
    @ApiModelProperty("沟通用户编码")
    private String[] toUserKeys;

    /**
     * 转办用户编码
     */
    @ApiModelProperty("转办用户编码")
    private String toUserKey;

    /**
     * 驳回节点编码
     */
    @ApiModelProperty("驳回节点编码")
    private String toNodeKey;

    @Override
    protected BaseResult subValidate() {
        return BaseResult.SUCCESS;
    }

}
