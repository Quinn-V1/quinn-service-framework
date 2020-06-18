package com.quinn.framework.activiti.component;

import com.quinn.framework.api.BpmInstSupplier;
import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.MethodInvokerOneParam;
import com.quinn.util.base.model.BatchResult;
import com.quinn.util.constant.StringConstant;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * 服务任务代理处理类
 * 所有的服务代理类都进入这个方法，然后通过脚本配置进行分发
 *
 * @author Qunhua.Liao
 * @since 2020-05-07
 */
public class ServiceTaskDelegate implements JavaDelegate, Serializable {

    @Resource
    private MethodInvokerOneParam<BpmTaskInfo, BatchResult> serviceTaskDelegateProxy;

    @Resource
    private BpmInstSupplier bpmInstSupplier;

    @Override
    public void execute(DelegateExecution execution) {
        BpmTaskInfo bpmTaskInfo = bpmInstSupplier.newBpmTaskInfo();

        FlowElement currentFlowElement = execution.getCurrentFlowElement();
        bpmTaskInfo.setBpmInstKey(execution.getProcessInstanceId());
        bpmTaskInfo.setBpmModelKey(execution.getProcessDefinitionId());
        bpmTaskInfo.setBpmKey(execution.getCurrentActivityId());
        bpmTaskInfo.setBpmKey(execution.getCurrentActivityId() + StringConstant.CHAR_AT_SING + execution.getId());
        bpmTaskInfo.setNodeCode(execution.getCurrentActivityId());
        bpmTaskInfo.setNodeName(currentFlowElement.getName());
        bpmTaskInfo.setNodeType(StringUtil.firstCharLowercase(currentFlowElement.getClass().getSimpleName()));
        bpmTaskInfo.setParams(execution.getVariables());

        serviceTaskDelegateProxy.invoke(bpmTaskInfo);
    }

}
