package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

import java.util.List;
import java.util.Map;

/**
 * 任务执行帮助类
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
public interface JobHelpService<T extends JobTemplate, I extends JobInstance> {

    /**
     * 找出所有可用任务
     *
     * @return 可用任务列表
     */
    BaseResult<List<T>> findAvailableTemplate();

    /**
     * 查找指定任务模板
     *
     * @param templateKey 任务模板编码
     * @return 任务模板
     */
    BaseResult<T> getTemplate(String templateKey);

    /**
     * 查找指定任务模板
     *
     * @param t 任务模板编码
     * @return 任务模板
     */
    BaseResult<T> updateTemplate(T t);

    /**
     * 查找指定任务模板
     *
     * @param i 任务模板编码
     * @return 任务模板
     */
    BaseResult<I> saveInstance(I i);

    /**
     * 查找指定任务模板
     *
     * @param i 任务模板编码
     * @return 任务模板
     */
    BaseResult<I> updateInstance(I i);

    /**
     * 填充运行时参数
     *
     * @param jobInstance 任务实例
     * @return 参数填充结果
     */
    BaseResult<Map<String, Object>> fillRunTimeParam(JobInstance jobInstance);

}
