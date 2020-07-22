package com.quinn.framework.activiti.component;

import com.quinn.framework.api.BpmParamValue;
import com.quinn.util.base.api.MethodInvokerOneParam;
import com.quinn.util.base.model.BaseResult;
import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

/**
 * 扩展缺省的流程节点默认工厂类，实现对Activiti节点的执行的默认行为的更改
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public class ActivityBehaviorFactoryExt extends DefaultActivityBehaviorFactory {

    /**
     * 分支网关行为扩展类
     */
    private ExclusiveGatewayActivityBehavior exclusiveGatewayActivityBehaviorExt;

    /**
     * 会签参数处理
     */
    private MethodInvokerOneParam<BpmParamValue, BaseResult> multiInstanceBehaviorProxy;

    /**
     * 通过Spring容器注入新的分支条件行为执行类
     *
     * @param exclusiveGatewayActivityBehaviorExt 分支网关行为扩展类
     */
    public void setExclusiveGatewayActivityBehavior(
            ExclusiveGatewayActivityBehavior exclusiveGatewayActivityBehaviorExt) {
        this.exclusiveGatewayActivityBehaviorExt = exclusiveGatewayActivityBehaviorExt;
    }

    public void setMultiInstanceBehaviorProxy(MethodInvokerOneParam<BpmParamValue, BaseResult> multiInstanceBehaviorProxy) {
        this.multiInstanceBehaviorProxy = multiInstanceBehaviorProxy;
    }

    @Override
    public ExclusiveGatewayActivityBehavior createExclusiveGatewayActivityBehavior(ExclusiveGateway exclusiveGateway) {
        return exclusiveGatewayActivityBehaviorExt;
    }

    @Override
    public SequentialMultiInstanceBehavior createSequentialMultiInstanceBehavior
            (Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior) {
        return new SequentialMultiInstanceBehaviorExt(activity, innerActivityBehavior, multiInstanceBehaviorProxy);
    }

    @Override
    public ParallelMultiInstanceBehavior createParallelMultiInstanceBehavior
            (Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior) {
        return new ParallelMultiInstanceBehaviorExt(activity, innerActivityBehavior, multiInstanceBehaviorProxy);
    }
}
 