package com.quinn.framework.bpm7.component;

import com.quinn.util.base.api.MethodInvokerTwoParam;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
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

    private MethodInvokerTwoParam<String, Map<String, Object>, Boolean> effectMethod;

    @Override
    public void leave(DelegateExecution execution) {
        // 执行父类的写法，以使其还是可以支持旧式的在跳出线上写条件的做法
        if (effectMethod == null) {
            super.leave(execution);
            return;
        }

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) execution.getCurrentFlowElement();
        List<SequenceFlow> outgoingFlows = exclusiveGateway.getOutgoingFlows();
        Map<String, Object> cond = new HashMap<>();

        for (SequenceFlow sequenceFlow : outgoingFlows) {
            if (effectMethod.invoke(sequenceFlow.getTargetRef(), cond)) {
                execution.setCurrentFlowElement(sequenceFlow);
                break;
            }
        }

        bpmnActivityBehavior.performDefaultOutgoingBehavior((ExecutionEntity) execution);
        // ?? 网上例子是这样 g感觉有问题，再验证
        // super.leave(execution);
    }

    /**
     * 设置判断器
     *
     * @param effectMethod 生效策略
     */
    public void setEffectMethod(MethodInvokerTwoParam<String, Map<String, Object>, Boolean> effectMethod) {
        this.effectMethod = effectMethod;
    }
}