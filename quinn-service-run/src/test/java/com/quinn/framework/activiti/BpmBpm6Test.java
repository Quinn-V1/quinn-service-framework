package com.quinn.framework.activiti;

import com.quinn.framework.ApplicationDefaultEntry;
import com.quinn.framework.ApplicationDefaultRunner;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试Demo类
 *
 * @author Qunhua.Liao
 * @since 2020-04-24
 */
@SpringBootTest(classes = {ApplicationDefaultEntry.class, ActivitiTestResourceConfiguration.class})
@RunWith(ApplicationDefaultRunner.class)
public class BpmBpm6Test {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(BpmBpm6Test.class);

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Test
    public void testForEachServiceDep() {
        Deployment dep = repositoryService.createDeployment()
                .addClasspathResource("bpmn/bpmn6/foreachService.bpmn")
                .deploy();

        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .deploymentId(dep.getId())
                .singleResult();

        LOGGER.debug("Deployment foreachService.bpmn success, key is {}", pd.getKey());
    }

    @Test
    public void testForEachServiceRun() {
        List<String> assigneeList = new ArrayList<>();
        assigneeList.add("a");
        assigneeList.add("b");
        assigneeList.add("c");
        assigneeList.add("d");

        Map<String, Object> vars = new HashMap<>();
        vars.put("assigneeList", assigneeList);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("forEachServiceTest", vars);
        LOGGER.debug("Instance[forEachServiceTest] start success：id is {}, note the handle times", processInstance.getId());
    }

    @Test
    public void testForEachUserDep() {
        Deployment dep = repositoryService.createDeployment()
                .addClasspathResource("bpmn/bpmn6/forEachUser.bpmn")
                .deploy();

        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .deploymentId(dep.getId())
                .singleResult();

        LOGGER.debug("Deployment forEachUser.bpmn success, key is {}" + pd.getKey());
    }

    @Test
    public void testForEachUserRun() {
        List<String> assigneeList = new ArrayList<>();
        assigneeList.add("a");
        assigneeList.add("b");
        assigneeList.add("c");
        assigneeList.add("d");

        Map<String, Object> vars = new HashMap<>();
        vars.put("assigneeList", assigneeList);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("forEachUserTest", vars);
        LOGGER.debug("Instance[forEachUserTest] start success：id is {}," +
                " then find the executions and try complete, watch when go to next", processInstance.getId());
    }

    @Test
    public void testGatewayDep() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("bpmn/bpmn6/gateway.bpmn")
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploy.getId())
                .singleResult();

        LOGGER.debug("Deployment gateway.bpmn success, key is {}" + processDefinition.getKey());
    }

    @Test
    public void testGatewayRunOfLessDay() {
        LOGGER.debug("Instance[gatewayTest] test, the condition days is 2 < 3");
        runGateWayOfDays(2);
    }

    @Test
    public void testGatewayRunOfLargeDay() {
        LOGGER.debug("Instance[gatewayTest] test, the condition days is 5 >= 3");
        runGateWayOfDays(5);
    }

    /**
     * 网关测试流程运行
     *
     * @param days 请假天数
     */
    public void runGateWayOfDays(int days) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("days", days);

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("gatewayTest", vars);

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        LOGGER.debug("Instance[gatewayTest] started first task is {} when days is {}", task.getName(), days);

        taskService.complete(task.getId());

        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        LOGGER.debug("Instance[gatewayTest] started second task is {} when days is {}", task.getName(), days);
    }

    @Test
    public void testListenerDep() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("bpmn/bpmn6/listener.bpmn")
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploy.getId())
                .singleResult();

        LOGGER.debug("Deployment listener.bpmn success, key is {}" + processDefinition.getKey());
    }

    /**
     * 任务监听测试运行
     * TIPS: 创建任务和制定审批人不好控制顺序
     */
    @Test
    public void testListenerRun() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("listenerTest");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        LOGGER.debug("Instance[listenerTest] started first task is {}", task.getName());

        taskService.complete(task.getId());

        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        LOGGER.debug("Instance[listenerTest] started second task is {}", task.getName());
    }

}
