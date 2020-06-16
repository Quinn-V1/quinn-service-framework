package com.quinn.framework.bpm7.component.event;

import com.quinn.framework.bpm7.api.EventToBpmTaskDecorator;
import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.framework.util.enums.BpmTodoTypeEnum;
import com.quinn.util.constant.StringConstant;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

/**
 * TaskEntityImpl 装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public class HistoricActivityInstanceEntityDecorator implements
        EventToBpmTaskDecorator<HistoricActivityInstanceEntity> {

    @Override
    public void decorate(BpmTaskInfo bpmTaskInfo, HistoricActivityInstanceEntity entity) {
        bpmTaskInfo.setBpmInstKey(entity.getProcessInstanceId());
        bpmTaskInfo.setBpmKey(entity.getId());
        bpmTaskInfo.setBpmExecKey(entity.getExecutionId());
        bpmTaskInfo.setBpmInstKey(entity.getProcessInstanceId());
        bpmTaskInfo.setBpmModelKey(entity.getProcessDefinitionId());

        bpmTaskInfo.setNodeType(entity.getActivityType());
        bpmTaskInfo.setNodeCode(entity.getActivityId());
        bpmTaskInfo.setNodeName(entity.getActivityName());
        bpmTaskInfo.setAssignee(StringConstant.NONE_OF_DATA);
        bpmTaskInfo.setTodoType(BpmTodoTypeEnum.AUTO.name());
    }

    @Override
    public Class<?> getDivClass() {
        return HistoricActivityInstanceEntity.class;
    }
}
