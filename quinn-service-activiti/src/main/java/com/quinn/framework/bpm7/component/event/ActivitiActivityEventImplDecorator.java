package com.quinn.framework.bpm7.component.event;

import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.framework.bpm7.api.EventToBpmTaskDecorator;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import com.quinn.util.constant.StringConstant;
import org.activiti.engine.delegate.event.ActivitiActivityEvent;

/**
 * TaskEntityImpl 装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public class ActivitiActivityEventImplDecorator implements EventToBpmTaskDecorator<ActivitiActivityEvent> {

    @Override
    public void decorate(BpmTaskInfo bpmTaskInfo, ActivitiActivityEvent entity) {
        bpmTaskInfo.setBpmKey(entity.getActivityId() + StringConstant.CHAR_COLON + entity.getExecutionId());
        bpmTaskInfo.setBpmExecKey(entity.getExecutionId());
        bpmTaskInfo.setNodeCode(entity.getActivityId());
        bpmTaskInfo.setNodeName(entity.getActivityName());
        bpmTaskInfo.setNodeType(entity.getActivityType());
        bpmTaskInfo.setBpmInstKey(entity.getProcessInstanceId());
        bpmTaskInfo.setTodoType(BpmTodoTypeEnum.AUDIT.name());
    }

    @Override
    public Class<?> getDivClass() {
        return ActivitiActivityEvent.class;
    }
}
