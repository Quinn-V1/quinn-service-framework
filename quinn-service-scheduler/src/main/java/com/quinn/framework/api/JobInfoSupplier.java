package com.quinn.framework.api;

import com.quinn.util.base.api.ClassDivAble;

/**
 * 任务模板、实例供应者
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
public interface JobInfoSupplier<T> extends ClassDivAble {

    /**
     * 生成任务实例
     * 保证结果不为空
     *
     * @param jobTemplate 任务模板
     * @return 任务实例
     */
    JobInstance createJobInstance(T jobTemplate);

}
