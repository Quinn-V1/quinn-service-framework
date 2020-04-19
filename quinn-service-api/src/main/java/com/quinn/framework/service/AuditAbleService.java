package com.quinn.framework.service;

import com.quinn.framework.api.entityflag.AuditAble;
import com.quinn.util.base.model.BaseResult;

import java.util.Map;

/**
 * 审计操作业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-03-28
 */
public interface AuditAbleService {

    /**
     * 新增审计
     *
     * @param oldValue 键
     * @param newValue 值
     * @param <T>      值泛型
     * @return 旧值
     */
    <T extends AuditAble> BaseResult<Map<String, Object>> auditLog(T oldValue, T newValue);

}
