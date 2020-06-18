package com.quinn.framework.activiti;

import com.quinn.framework.ApplicationDefaultEntry;
import com.quinn.framework.ApplicationDefaultRunner;
import com.quinn.framework.annotation.TestEntry;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试Demo类
 *
 * @author Qunhua.Liao
 * @since 2020-04-24
 */
@SpringBootTest(classes = {ApplicationDefaultEntry.class, ActivitiTestResourceConfiguration.class})
@RunWith(ApplicationDefaultRunner.class)
@TestEntry(ApplicationDefaultEntry.class)
public class BpmBpm5Test {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(BpmBpm5Test.class);

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Test
    public void testBeanDep() {
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("bpmn/bpmn5/bean.bpmn").deploy();
        LOGGER.debug("Deployment bean.bpmn success, id is {}", deploy.getId());
    }

    @Test
    public void testBeanRun() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("myBean", new MyBean());

        // 启动流程
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("beanTest", vars);
        LOGGER.debug("Instance[beanTest] start success：param[myName] from bean is {}",
                runtimeService.getVariable(pi.getId(), "myName"));
    }

    @Test
    public void testDelegateDep() {
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("bpmn/bpmn5/delegate.bpmn").deploy();
        LOGGER.debug("Deployment delegate.bpmn success, id is {}", deploy.getId());
    }

    @Test
    public void testDelegateRun() {
        MyDelegate de = new MyDelegate();
        Map<String, Object> vars = new HashMap<>();
        vars.put("myDelegate", de);

        // 启动流程
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("delegateTest", vars);
        LOGGER.debug("Instance[delegateTest] start , id is {}, note what to do by MyDelegate", pi.getId());
    }

}
