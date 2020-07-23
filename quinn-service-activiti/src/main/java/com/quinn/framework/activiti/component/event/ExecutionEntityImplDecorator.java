package com.quinn.framework.activiti.component.event;

import com.quinn.framework.activiti.api.EventToBpmTaskDecorator;
import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

/**
 * TaskEntityImpl 装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public class ExecutionEntityImplDecorator implements EventToBpmTaskDecorator<ExecutionEntity> {

    @Override
    public void decorate(BpmTaskInfo bpmTaskInfo, ExecutionEntity entity) {
        bpmTaskInfo.setBpmInstKey(entity.getProcessInstanceId());
        bpmTaskInfo.setBpmKey(entity.getId());
        bpmTaskInfo.setParams(entity.getVariables());
        bpmTaskInfo.setTodoType(BpmTodoTypeEnum.AUTO.name());
        bpmTaskInfo.setBpmExecKey(entity.getId());
        bpmTaskInfo.setParentExecKey(entity.getSuperExecutionId());
    }

    @Override
    public Class<?> getDivClass() {
        return ExecutionEntity.class;
    }
}
