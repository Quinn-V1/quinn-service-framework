package com.quinn.framework.activiti;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.ApplicationDefaultEntry;
import com.quinn.framework.ApplicationDefaultRunner;
import com.quinn.framework.model.deal.BpmAgreeParam;
import com.quinn.framework.util.enums.NodeTypeEnum;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = {ApplicationDefaultEntry.class, ActivitiTestResourceConfiguration.class})
@RunWith(ApplicationDefaultRunner.class)
public class BpmInstBaseTest {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(BpmInstBaseTest.class);

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    /**
     * 测试部署
     */
    @Test
    public void testDeploy() {
        // TIPS 模板文件一定需要是bpmn（bpmn2.0.xml）结尾才能被解析
        // TIPS Task Service一定要有 class implementation expression等属性
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("bpmn/holiday001.bpmn")
                .key("holiday-deploy")
                .name("请假流程-部署")
                .deploy();

        LOGGER.debug("Deploy holiday001.bpmn success the deployment info is {}", JSONObject.toJSONString(deploy));
    }

    /**
     * 测试查找流程定义
     */
    @Test
    public void testProcessQuery() {
        // 没有审批人：getIdentityLinks 报空指针异常：阻断Json序列化
        ProcessDefinition holiday001 = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("holiday001")
                .latestVersion()
                .singleResult();

        System.out.println(holiday001.getId());
        System.out.println(holiday001.getKey());
        System.out.println(holiday001.getName());
        System.out.println(holiday001.getVersion());
    }

    /**
     * 测试发起流程
     */
    @Test
    public void testStartProcess() {
        Map<String, Object> cond = new HashMap<>();
        cond.put("var1", "test");
        cond.put("var2", 123);
        cond.put("var3", new BpmAgreeParam());

        ProcessInstance holiday001 = runtimeService.startProcessInstanceByKey("holiday001", cond);
        System.out.println(holiday001.getId());
        System.out.println(holiday001.getDescription());

        // TIPS 此处拿不到变量参数
        System.out.println(holiday001.getProcessVariables());
        System.out.println(holiday001.getStartUserId());
        System.out.println(holiday001.getProcessInstanceId());

        System.out.println(holiday001.getParentId());
        System.out.println(holiday001.getParentProcessInstanceId());
        System.out.println(holiday001.getRootProcessInstanceId());
    }

    /**
     * 查找起始事件
     */
    @Test
    public void testStartEventQuery() {
        HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId("7501")
                .activityType(NodeTypeEnum.startEvent.name())
                .singleResult();

        System.out.println(JSONObject.toJSONString(historicActivityInstance));
    }

    /**
     * 测试查询任务
     * e.id: 7501
     * e.name: null
     * e.desc: null
     * e.actId: null
     * e.pid: null
     * e.pppiid: null
     * e.piid: 7501
     * e.rpiid: 7501
     * e.seid: null
     * e.id: 7511
     * e.name: null
     * e.desc: null
     * e.actId: task001
     * e.pid: 7501
     * e.pppiid: null
     * e.piid: 7501
     * e.rpiid: 7501
     * e.seid: null
     * t.dueDate: null
     * t.createTime: Mon May 04 15:09:56 CST 2020
     * t.claimTime: null
     * t.category: null
     * t.delegateState: null
     * t.desc: null
     * t.processVars: {}
     * t.execId: 7511
     * t.assignee: null
     * t.name: 申请人发起申请
     * t.id: 7514
     * t.tdkey: task001
     * t.formKey: null
     * t.owner: null
     * t.ptid: null
     * t.priority: 50
     * t.pdid: holiday001:1:2503
     * t.piid: 7501
     * t.owner: null
     * t.tenantId:
     */
    @Test
    public void testTaskQuery() {
        List<Execution> list = runtimeService.createExecutionQuery().processInstanceId("7501").list();
        if (list != null) {
            for (Execution execution : list) {
                System.out.println("e.id: " + execution.getId());
                System.out.println("e.name: " + execution.getName());
                System.out.println("e.desc: " + execution.getDescription());
                System.out.println("e.actId: " + execution.getActivityId());
                System.out.println("e.pid: " + execution.getParentId());
                System.out.println("e.pppiid: " + execution.getParentProcessInstanceId());
                System.out.println("e.piid: " + execution.getProcessInstanceId());
                System.out.println("e.rpiid: " + execution.getRootProcessInstanceId());
                System.out.println("e.seid: " + execution.getSuperExecutionId());
            }
        }

        List<Task> list1 = taskService.createTaskQuery().processInstanceId("7501").list();
        if (list1 != null) {
            for (Task task : list1) {
                System.out.println("t.dueDate: " + task.getDueDate());
                System.out.println("t.createTime: " + task.getCreateTime());
                System.out.println("t.claimTime: " + task.getClaimTime());
                System.out.println("t.category: " + task.getCategory());
                System.out.println("t.delegateState: " + task.getDelegationState());
                System.out.println("t.desc: " + task.getDescription());
                System.out.println("t.processVars: " + task.getProcessVariables());
                System.out.println("t.execId: " + task.getExecutionId());
                System.out.println("t.assignee: " + task.getAssignee());
                System.out.println("t.name: " + task.getName());
                System.out.println("t.id: " + task.getId());
                System.out.println("t.tdkey: " + task.getTaskDefinitionKey());
                System.out.println("t.formKey: " + task.getFormKey());
                System.out.println("t.owner: " + task.getOwner());
                System.out.println("t.ptid: " + task.getParentTaskId());
                System.out.println("t.priority: " + task.getPriority());
                System.out.println("t.pdid: " + task.getProcessDefinitionId());
                System.out.println("t.piid: " + task.getProcessInstanceId());
                System.out.println("t.owner: " + task.getOwner());
                System.out.println("t.tenantId: " + task.getTenantId());
            }
        }
    }

    /**
     * 测试任务完成
     */
    @Test
    public void testTaskComplete() {
        taskService.complete("7514");
    }

    /**
     * 测试驳回
     */
    @Test
    public void testReject() {
        String processInstanceId = "7501";
        String taskId = "10002";

        // 取得所有历史任务按时间降序排序
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .desc()
                .list();
        if (ObjectUtils.isEmpty(htiList) || htiList.size() < 2) {
            return;
        }

        // list里的第二条代表上一个任务
        HistoricTaskInstance lastTask = htiList.get(1);
        // list里第二条代表当前任务
        HistoricTaskInstance curTask = htiList.get(0);
        // 当前节点的executionId
        String curExecutionId = curTask.getExecutionId();
        // 上个节点的taskId
        String lastTaskId = lastTask.getId();
        // 上个节点的executionId
        String lastExecutionId = lastTask.getExecutionId();

        if (null == lastTaskId) {
            throw new RuntimeException("LAST TASK IS NULL");
        }

        String processDefinitionId = lastTask.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        String lastActivityId = null;

        List<HistoricActivityInstance> haiFinishedList = historyService.createHistoricActivityInstanceQuery()
                .executionId(lastExecutionId).finished().list();
        for (HistoricActivityInstance hai : haiFinishedList) {
            if (lastTaskId.equals(hai.getTaskId())) {
                // 得到ActivityId，只有HistoricActivityInstance对象里才有此方法
                lastActivityId = hai.getActivityId();
                break;
            }
        }
        // 得到上个节点的信息
        FlowNode lastFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(lastActivityId);
        // 取得当前节点的信息
        Execution execution = runtimeService.createExecutionQuery().executionId(curExecutionId).singleResult();
        String curActivityId = execution.getActivityId();
        FlowNode curFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(curActivityId);

        //记录当前节点的原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<>();
        oriSequenceFlows.addAll(curFlowNode.getOutgoingFlows());
        //清理活动方向
        curFlowNode.getOutgoingFlows().clear();
        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(curFlowNode);
        newSequenceFlow.setTargetFlowElement(lastFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        curFlowNode.setOutgoingFlows(newSequenceFlowList);
        // 完成任务
        taskService.complete(taskId);
        //恢复原方向
        curFlowNode.setOutgoingFlows(oriSequenceFlows);
        org.activiti.engine.task.Task nextTask = taskService
                .createTaskQuery().processInstanceId(processInstanceId).singleResult();
        // 设置执行人
        if (nextTask != null) {
            taskService.setAssignee(nextTask.getId(), lastTask.getAssignee());
        }
    }

    /**
     * 测试删除-实例
     */
    @Test
    public void testDeleteInstance() {
        String[] ids = new String[]{"517501",
                "520001",
                "522503",
                "527501",
                "532501",
                "535001",
                "537501",
                "540001",
                "542501",
                "592501",
                "632501",
                "632512",
                "632523",
                "632534",
                "635001",
                "635013",
                "637501",
                "637546",
                "640001",
                "642501",
                "642546",
                "642591",
                "645001",
                "647501",
                "647546",
                "647591",
                "647636",
                "647681",
                "647726",
                "647771",
                "647816",
                "650001",
                "650046",
                "650091",
                "652501",
                "652546",
                "652591",
                "655001",
                "665001",
                "665046",
                "667501",
                "670001",
                "672501",
                "672546",
                "675001",
                "675046",
                "677501",
                "677546",
                "677591",
                "680001",
                "680046",
                "682501",
                "682546",
                "682591",
                "682636",
                "682681",
                "685001",
                "685046",
                "685091",
                "685136",
                "687501",
                "690001",
                "692501",
                "695001",
                "695046",
                "697501",
                "697546",
                "700001",
                "700046",
                "700091",
                "702501",
                "705001",
                "707501",
                "710001",
                "710046",
                "712501",
                "712546",
                "715001",
                "715046",
                "717501",
                "720001",
                "722501",
                "725001",
                "727501",
                "730001",
                "732501",
                "735001",
                "737501",
                "737546",
                "745001",
                "747501",
                "750001",
                "752501",
                "755001",
                "755012",
                "757501",
                "760001",
                "762501",
                "762514",
                "765001",
                "767501",
                "770001",
                "772501",
                "775001",
                "777501"};
        for (String id : ids) {
            try {
                runtimeService.deleteProcessInstance(id, "TEST");
            } catch (Exception e) {
                LOGGER.errorError("deleteProcessInstance {0}", e, id);
            }
        }
    }

    /**
     * 测试删除-实例历史
     */
    @Test
    public void testDeleteHisInstance() {
        String[] ids = new String[]{"517501",
                "520001",
                "522503",
                "527501",
                "532501",
                "535001",
                "537501",
                "540001",
                "542501",
                "592501",
                "632501",
                "632512",
                "632523",
                "632534",
                "635001",
                "635013",
                "637501",
                "637546",
                "640001",
                "642501",
                "642546",
                "642591",
                "645001",
                "647501",
                "647546",
                "647591",
                "647636",
                "647681",
                "647726",
                "647771",
                "647816",
                "650001",
                "650046",
                "650091",
                "652501",
                "652546",
                "652591",
                "655001",
                "665001",
                "665046",
                "667501",
                "670001",
                "672501",
                "672546",
                "675001",
                "675046",
                "677501",
                "677546",
                "677591",
                "680001",
                "680046",
                "682501",
                "682546",
                "682591",
                "682636",
                "682681",
                "685001",
                "685046",
                "685091",
                "685136",
                "687501",
                "690001",
                "692501",
                "695001",
                "695046",
                "697501",
                "697546",
                "700001",
                "700046",
                "700091",
                "702501",
                "705001",
                "707501",
                "710001",
                "710046",
                "712501",
                "712546",
                "715001",
                "715046",
                "717501",
                "720001",
                "722501",
                "725001",
                "727501",
                "730001",
                "732501",
                "735001",
                "737501",
                "737546",
                "745001",
                "747501",
                "750001",
                "752501",
                "755001",
                "755012",
                "757501",
                "760001",
                "762501",
                "762514",
                "765001",
                "767501",
                "770001",
                "772501",
                "775001",
                "777501"};
        for (String id : ids) {
            try {
                historyService.deleteHistoricProcessInstance(id);
            } catch (Exception e) {
                LOGGER.errorError("deleteProcessInstance {0}", e, id);
            }
        }
    }

}
