package com.quinn.framework.bpm7.api;

import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.util.base.api.ClassDivAble;

/**
 * 事件转任务装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public interface EventToBpmTaskDecorator<T> extends ClassDivAble {

    /**
     * 使用BPM事件装饰任务对象
     *
     * @param bpmTaskInfo 任务对象
     * @param t           BPM事件
     */
    void decorate(BpmTaskInfo bpmTaskInfo, T t);

}
