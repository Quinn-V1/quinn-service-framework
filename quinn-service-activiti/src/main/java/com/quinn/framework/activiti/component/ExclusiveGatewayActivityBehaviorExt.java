package com.quinn.framework.activiti.component;

import com.quinn.framework.activiti.model.ActToBpmInfoFactory;
import com.quinn.framework.api.BpmNodeRelateInfo;
import com.quinn.framework.util.BpmParamName;
import com.quinn.framework.util.enums.NodeRelateTypeEnum;
import com.quinn.util.base.api.MethodInvokerTwoParam;
import com.quinn.util.constant.NumberConstant;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对网关的条件判断，优先使用扩展的配置
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public class ExclusiveGatewayActivityBehaviorExt extends ExclusiveGatewayActivityBehavior {

    private MethodInvokerTwoParam<BpmNodeRelateInfo, String, String> exclusiveGatewayDelegateProxy;

    @Override
    public void leave(DelegateExecution execution) {
        // 执行父类的写法，以使其还是可以支持旧式的在跳出线上写条件的做法
        if (exclusiveGatewayDelegateProxy == null) {
            super.leave(execution);
            return;
        }

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) execution.getCurrentFlowElement();
        BpmNodeRelateInfo bpmNodeRelateInfo = ActToBpmInfoFactory.createBpmNodeRelateInfo();
        bpmNodeRelateInfo.setRelateType(NodeRelateTypeEnum.CONDITION.name());
        bpmNodeRelateInfo.setLeadNodeCode(exclusiveGateway.getId());
        bpmNodeRelateInfo.setModelKey(execution.getProcessDefinitionId());
        String matchNodeCode = exclusiveGatewayDelegateProxy.invoke(bpmNodeRelateInfo, execution.getProcessInstanceId());

        List<SequenceFlow> outgoingFlows = exclusiveGateway.getOutgoingFlows();
        SequenceFlow sequenceFlowMath = null;
        for (SequenceFlow sequenceFlow : outgoingFlows) {
            sequenceFlowMath = sequenceFlow;
            if (sequenceFlow.getTargetRef().equals(matchNodeCode)) {
                break;
            }
        }

        Context.getCommandContext().getHistoryManager().recordActivityEnd((ExecutionEntity) execution, null);
        execution.setCurrentFlowElement(sequenceFlowMath);

        bpmnActivityBehavior.performDefaultOutgoingBehavior((ExecutionEntity) execution);
        // ?? 网上例子是这样 g感觉有问题，再验证
        // super.leave(execution);
    }

    /**
     * 设置判断器
     *
     * @param exclusiveGatewayDelegateProxy 生效策略
     */
    public void setExclusiveGatewayDelegateProxy(MethodInvokerTwoParam<BpmNodeRelateInfo,
            String, String> exclusiveGatewayDelegateProxy) {
        this.exclusiveGatewayDelegateProxy = exclusiveGatewayDelegateProxy;
    }
}