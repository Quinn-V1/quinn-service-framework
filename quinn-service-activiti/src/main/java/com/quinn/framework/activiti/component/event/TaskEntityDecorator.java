package com.quinn.framework.activiti.component.event;

import com.quinn.framework.activiti.api.EventToBpmTaskDecorator;
import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * TaskEntityImpl 装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public class TaskEntityDecorator implements EventToBpmTaskDecorator<TaskEntity> {

    @Override
    public void decorate(BpmTaskInfo bpmTaskInfo, TaskEntity entity) {
        bpmTaskInfo.setBpmKey(entity.getId());
        bpmTaskInfo.setBpmExecKey(entity.getExecutionId());
        bpmTaskInfo.setBpmInstKey(entity.getProcessInstanceId());

        bpmTaskInfo.setBpmModelKey(entity.getProcessDefinitionId());
        bpmTaskInfo.setNodeCode(entity.getTaskDefinitionKey());
        bpmTaskInfo.setNodeName(entity.getName());
        bpmTaskInfo.setNodeType(entity.getEventName());
        bpmTaskInfo.setTodoType(BpmTodoTypeEnum.AUDIT.name());
    }

    @Override
    public Class<?> getDivClass() {
        return TaskEntity.class;
    }
}
