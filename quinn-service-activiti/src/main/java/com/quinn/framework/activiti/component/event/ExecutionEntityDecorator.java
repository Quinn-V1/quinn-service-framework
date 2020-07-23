package com.quinn.framework.activiti.component.event;

import com.quinn.framework.activiti.api.EventToBpmTaskDecorator;
import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import com.quinn.util.base.convertor.BaseConverter;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

/**
 * TaskEntityImpl 装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public class ExecutionEntityDecorator implements EventToBpmTaskDecorator<ExecutionEntity> {

    @Override
    public void decorate(BpmTaskInfo bpmTaskInfo, ExecutionEntity executionEntityImpl) {
        bpmTaskInfo.setBpmInstKey(executionEntityImpl.getProcessInstanceId());
        bpmTaskInfo.setBpmKey(executionEntityImpl.getId());
        bpmTaskInfo.setParams(executionEntityImpl.getVariables());
        if (executionEntityImpl.getParent() != null && executionEntityImpl.getParent().getBusinessKey() != null) {
            bpmTaskInfo.setInstanceId(BaseConverter.staticConvert(executionEntityImpl.getParent().getBusinessKey(), Long.class));
        }
        bpmTaskInfo.setBpmExecKey(executionEntityImpl.getId());
        bpmTaskInfo.setParentExecKey(executionEntityImpl.getSuperExecutionId());
        bpmTaskInfo.setTodoType(BpmTodoTypeEnum.AUTO.name());
    }

    @Override
    public Class<?> getDivClass() {
        return ExecutionEntity.class;
    }
}
