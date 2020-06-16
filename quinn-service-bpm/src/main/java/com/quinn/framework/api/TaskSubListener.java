package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

import java.util.Map;

/**
 * 任务子监听器
 *
 * @author Qunhua.Liao
 * @since 2020-05-13
 */
public interface TaskSubListener {

    /**
     * 监听动作
     *
     * @param instanceInfo 流程实例信息
     * @param taskInfo     任务信息
     * @param param        参数信息
     * @return 监听结果
     */
    BaseResult listen(BpmInstInfo instanceInfo, BpmTaskInfo taskInfo, Map<String, Object> param);

}
