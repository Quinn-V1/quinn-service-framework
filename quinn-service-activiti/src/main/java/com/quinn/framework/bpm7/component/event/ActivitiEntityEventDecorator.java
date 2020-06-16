package com.quinn.framework.bpm7.component.event;

import com.quinn.framework.bpm7.api.EventToBpmTaskDecorator;
import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

/**
 * TaskEntityImpl 装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public class ActivitiEntityEventDecorator implements EventToBpmTaskDecorator<TaskEntityImpl> {

    @Override
    public void decorate(BpmTaskInfo bpmTaskInfo, TaskEntityImpl entity) {
        bpmTaskInfo.setBpmInstKey(entity.getProcessInstanceId());
        bpmTaskInfo.setBpmKey(entity.getId());
        bpmTaskInfo.setParams(entity.getVariables());
        bpmTaskInfo.setTodoType(BpmTodoTypeEnum.AUTO.name());
    }

    @Override
    public Class<?> getDivClass() {
        return TaskEntityImpl.class;
    }
}
