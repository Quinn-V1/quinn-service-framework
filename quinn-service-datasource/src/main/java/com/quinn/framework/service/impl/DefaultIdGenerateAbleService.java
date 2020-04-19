package com.quinn.framework.service.impl;

import com.quinn.framework.api.entityflag.IdGenerateAble;
import com.quinn.framework.service.IdGenerateAbleService;
import com.quinn.framework.service.JdbcService;
import com.quinn.util.base.model.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 默认ID生成器
 *
 * @author Qunhua.Liao
 * @since 2020-04-08
 */
public class DefaultIdGenerateAbleService implements IdGenerateAbleService {

    @Autowired
    private JdbcService jdbcService;

    @Override
    public BaseResult generateId(IdGenerateAble idGenerateAble) {
        BaseResult<Long> longBaseResult = jdbcService.generateNextValueOfSeq(idGenerateAble.seqName());
        if (!longBaseResult.isSuccess()) {
            return longBaseResult;
        }
        idGenerateAble.setId(longBaseResult.getData());
        return BaseResult.success(idGenerateAble);
    }

}
