package com.quinn.framework.bpm7.component.event;

import com.quinn.framework.bpm7.api.EventToBpmTaskDecorator;
import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

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
    }

    @Override
    public Class<?> getDivClass() {
        return ExecutionEntity.class;
    }
}
