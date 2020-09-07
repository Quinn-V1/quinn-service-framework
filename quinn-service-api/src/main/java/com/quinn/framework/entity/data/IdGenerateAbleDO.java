package com.quinn.framework.entity.data;

import com.quinn.framework.api.entityflag.IdGenerateAble;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础实体类
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
@Setter
@Getter
public abstract class IdGenerateAbleDO extends BaseDO implements IdGenerateAble {

    @Override
    public String seqName() {
        return this.getClass().getName();
    }

}
