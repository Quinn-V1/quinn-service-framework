package com.quinn.framework.service.impl;

import com.quinn.framework.api.entityflag.IdGenerateAble;
import com.quinn.framework.service.IdGenerateAbleService;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.NumberConstant;

import java.util.Random;

/**
 * 默认审计业务实现
 *
 * @author Qunhua.Liao
 * @since 2020-04-08
 */
public class DefaultIdGenerateAbleServiceImpl implements IdGenerateAbleService {

    @Override
    public BaseResult generateId(IdGenerateAble idGenerateAble) {
        idGenerateAble.setId(System.currentTimeMillis() * NumberConstant.MAX_TPS
                + new Random().nextInt(NumberConstant.MAX_TPS));
        return BaseResult.SUCCESS;
    }

}
