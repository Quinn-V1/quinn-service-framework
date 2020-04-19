package com.quinn.framework.service.impl;

import com.quinn.framework.api.entityflag.AuditAble;
import com.quinn.framework.service.AuditAbleService;
import com.quinn.util.base.model.BaseResult;

import java.util.Map;

/**
 * 默认审计业务实现
 *
 * @author Qunhua.Liao
 * @since 2020-04-08
 */
public class DefaultAuditAbleServiceImpl implements AuditAbleService {

    @Override
    public <T extends AuditAble> BaseResult<Map<String, Object>> auditLog(T oldValue, T newValue) {
        // TODO 审计日志
        return BaseResult.fail();
    }

}
