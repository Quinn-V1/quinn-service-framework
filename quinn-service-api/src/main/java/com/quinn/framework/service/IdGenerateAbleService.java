package com.quinn.framework.service;

import com.quinn.framework.api.entityflag.IdGenerateAble;
import com.quinn.util.base.model.BaseResult;

/**
 * ID生成服务
 *
 * @author Qunhua.Liao
 * @since 2020-04-08
 */
public interface IdGenerateAbleService {

    /**
     * 生成ID
     *
     * @param idGenerateAble 实体
     * @return 是否处理成功
     */
    BaseResult generateId(IdGenerateAble idGenerateAble);

}
