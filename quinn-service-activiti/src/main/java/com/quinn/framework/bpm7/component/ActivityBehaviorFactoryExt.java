package com.quinn.framework.bpm7.component;

import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
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
    private ExclusiveGatewayActivityBehaviorExt exclusiveGatewayActivityBehaviorExt;

    /**
     * 通过Spring容器注入新的分支条件行为执行类
     *
     * @param exclusiveGatewayActivityBehaviorExt 分支网关行为扩展类
     */
    public void setExclusiveGatewayActivityBehaviorExt(
            ExclusiveGatewayActivityBehaviorExt exclusiveGatewayActivityBehaviorExt) {
        this.exclusiveGatewayActivityBehaviorExt = exclusiveGatewayActivityBehaviorExt;
    }

    @Override
    public ExclusiveGatewayActivityBehavior createExclusiveGatewayActivityBehavior(ExclusiveGateway exclusiveGateway) {
        return exclusiveGatewayActivityBehaviorExt;
    }
}  
 