package com.quinn.framework.activiti.component;

import com.quinn.framework.activiti.model.ActToBpmInfoFactory;
import com.quinn.framework.api.BpmParamValue;
import com.quinn.util.base.api.MethodInvokerOneParam;
import com.quinn.util.base.api.MethodInvokerTwoParam;
import com.quinn.util.base.model.BaseResult;
import org.activiti.bpmn.model.Activity;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;

/**
 * 串行会签行为扩展类
 *
 * @author Qunhua.Liao
 * @since 2020-07-22
 */
public class SequentialMultiInstanceBehaviorExt extends SequentialMultiInstanceBehavior {

    private MethodInvokerOneParam<BpmParamValue, BaseResult> multiInstanceBehaviorProxy;

    public SequentialMultiInstanceBehaviorExt(
            Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior,
            MethodInvokerOneParam<BpmParamValue, BaseResult> multiInstanceBehaviorProxy) {
        super(activity, innerActivityBehavior);
        this.multiInstanceBehaviorProxy = multiInstanceBehaviorProxy;
    }

    @Override
    protected void setLoopVariable(DelegateExecution execution, String variableName, Object value) {
        super.setLoopVariable(execution, variableName, value);
        BpmParamValue bpmParamValue = ActToBpmInfoFactory.createBpmParamValue(execution, variableName, value);
        bpmParamValue.setNodeCode(activity.getId());
        multiInstanceBehaviorProxy.invoke(bpmParamValue);
    }
}
