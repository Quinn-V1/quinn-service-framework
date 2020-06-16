package com.quinn.framework.bpm7.component.event;

import com.quinn.framework.bpm7.api.EventToBpmTaskDecorator;
import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import com.quinn.util.base.convertor.BaseConverter;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

/**
 * TaskEntityImpl 装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public class ExecutionEntityImplEventDecorator implements EventToBpmTaskDecorator<ExecutionEntityImpl> {

    @Override
    public void decorate(BpmTaskInfo bpmTaskInfo, ExecutionEntityImpl executionEntityImpl) {
        bpmTaskInfo.setBpmInstKey(executionEntityImpl.getProcessInstanceId());
        bpmTaskInfo.setBpmKey(executionEntityImpl.getId());
        bpmTaskInfo.setParams(executionEntityImpl.getVariables());
        bpmTaskInfo.setInstanceId(BaseConverter.staticConvert(executionEntityImpl.getParent().getBusinessKey(), Long.class));
        bpmTaskInfo.setTodoType(BpmTodoTypeEnum.AUTO.name());
    }

    @Override
    public Class<?> getDivClass() {
        return ExecutionEntityImpl.class;
    }
}
