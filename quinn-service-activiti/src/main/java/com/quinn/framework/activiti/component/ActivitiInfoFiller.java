package com.quinn.framework.activiti.component;

import com.quinn.framework.activiti.model.CustomProcessDiagramGenerator;
import com.quinn.framework.activiti.util.ImageUtil;
import com.quinn.framework.api.*;
import com.quinn.framework.util.SessionUtil;
import com.quinn.framework.util.enums.*;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StreamUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.FileAdapter;
import com.quinn.util.base.enums.CommonMessageEnum;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.base.exception.file.FileFormatException;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.CharConstant;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.StringConstant;
import lombok.SneakyThrows;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 流程模板工具类
 *
 * @author Simon.z
 * @since 2020/5/29
 */
public final class ActivitiInfoFiller implements BpmInfoFiller {

    @Value("${com.ming-cloud.bpm.diagram.font-name.activity:宋体}")
    private String activityFontName;

    @Value("${com.ming-cloud.bpm.diagram.font-name.label:宋体}")
    private String labelFontName;

    @Value("${com.ming-cloud.bpm.diagram.font-name.annotation:宋体}")
    private String annotationFontName;

    @Value("${com.ming-cloud.bpm.diagram.image.type:png}")
    private String imageType;

    @Value("${com.ming-cloud.bpm.diagram.color.active:-7667712}")
    private int activeColor;

    @Value("${com.ming-cloud.bpm.diagram.color.history:-12799119}")
    private int historyColor;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private HistoryService historyService;

    @Resource
    private TaskService taskService;

    @Resource
    private BpmModelSupplier modelSupplier;

    @Resource
    private BpmInstSupplier instSupplier;

    @Override
    public void generateModelInfo(FileAdapter adapter, BpmModelInfo modelInfo) {
        // 解析获取BpmModel
        String designContent = StringUtil.forBytes(adapter.getBytes());
        try {
            // STEP1：创建转换对象
            BpmnXMLConverter converter = new BpmnXMLConverter();

            // STEP2：读取xml文件
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(adapter.getInputStream());

            // STEP3：将xml文件转换成BpmModel
            BpmnModel bpmnModel = converter.convertToBpmnModel(streamReader);
            Process mainProcess = bpmnModel.getMainProcess();
            String modelKey = mainProcess.getId();
            String modelName = mainProcess.getName();
            if (StringUtil.isEmpty(modelName)) {
                modelName = modelKey;
            }

            modelInfo.setModelKey(modelKey);
            modelInfo.setModelName(modelName);
            modelInfo.setDesignContent(designContent);
        } catch (XMLStreamException e) {
            throw new FileFormatException()
                    .addParam(CommonMessageEnum.FILE_FORMAT_NOT_CORRECT.paramNames[0], "xml")
                    .exception();
        }
    }

    @Override
    public void deployModel(BpmModelInfo data) {
        // 使用输入流部署流程定义
        String fileName = data.fileName();

        Deployment deployment = repositoryService.createDeployment()
                .addInputStream(fileName, StreamUtil.asStream(data.getDesignContent()))
                .name(data.getModelName())
                .key(data.getModelKey())
                .deploy();

        // 根据deploymentId查询单个流程实例
        String deploymentId = deployment.getId();
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();

        data.setBpmDeployKey(deploymentId);
        data.setBpmKey(processDefinition.getId());
        data.setBpmVersion(processDefinition.getVersion());
    }

    @Override
    public void downloadModelPng(HttpServletResponse response, String bpmKey) {
        // 获取Bpm Model
        InputStream inputStream;
        BpmnModel model = repositoryService.getBpmnModel(bpmKey);
        if (model == null || model.getLocationMap().size() == 0) {
            throw new BaseBusinessException();
        }
        ProcessDiagramGenerator generator = new CustomProcessDiagramGenerator(activeColor, historyColor);

        // 生成流程图 都不高亮
        inputStream = generator.generateDiagram(model, activityFontName, labelFontName, annotationFontName);
        response.setContentType("application/octet-stream; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + bpmKey + CharConstant.DOT + imageType);

        try {
            ImageUtil.convertSvg2Png(inputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new BaseBusinessException();
        } finally {
            StreamUtil.closeQuietly(inputStream);
        }
    }

    @Override
    public void resolveNodeInfo(BpmModelInfo data, BpmNodeRelateBO relateInfo) {
        String processDefinitionId = data.getBpmKey();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Collection<FlowElement> elements = bpmnModel.getMainProcess().getFlowElements();
        Long modelId = data.getId();
        String modelKey = data.getModelKey();
        Integer modelVersion = data.getModelVersion();

        for (FlowElement element : elements) {
            if (element instanceof SequenceFlow) {
                SequenceFlow sequence = (SequenceFlow) element;
                BpmNodeRelateInfo relate = generateNodeRelate(sequence);
                relate.setModelId(modelId);
                relate.setModelKey(modelKey);
                relate.setModelVersion(modelVersion);
                relateInfo.addRelate(relate);
            } else {
                BpmNodeInfo nodeInfo = generateNodeInfo(element);
                nodeInfo.setModelId(modelId);
                nodeInfo.setModelKey(modelKey);
                nodeInfo.setModelVersion(modelVersion);
                nodeInfo.initForSeq();

                relateInfo.addNode(nodeInfo);
                if (element instanceof StartEvent) {
                    relateInfo.setStartNode(nodeInfo);
                }
            }
        }
    }

    @Override
    public BaseResult start(BpmInstInfo bpmInstInfo, Map<String, Object> param) {
        Authentication.setAuthenticatedUserId(bpmInstInfo.getStartUser());
        ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
                .processDefinitionId(bpmInstInfo.getBpmModelKey())
                .businessKey(bpmInstInfo.getId() + "")
                .tenantId(SessionUtil.getOrgKey())
                .processDefinitionKey(bpmInstInfo.getModelKey())
                .name(bpmInstInfo.getSubject())
                .variables(param)
                .start();

        // 信心流程状态-关联BPM和Act (数据库的关联动作交给监听机制)
        String processInstanceId = processInstance.getProcessInstanceId();
        bpmInstInfo.setInstStatus(BpmInstStatusEnum.RUNNING.name());
        bpmInstInfo.setBpmKey(processInstanceId);
        return BaseResult.SUCCESS;
    }

    @Override
    public void addCandidates(String taskBpmId, Set<String> userCandidates, Set<String> roleCandidates) {
        if (!CollectionUtil.isEmpty(userCandidates)) {
            if (userCandidates.size() == NumberConstant.INT_ONE && CollectionUtil.isEmpty(roleCandidates)) {
                taskService.setAssignee(taskBpmId, userCandidates.iterator().next());
            } else {
                for (String candidate : userCandidates) {
                    taskService.addCandidateUser(taskBpmId, candidate);
                }
            }
        }

        if (!CollectionUtil.isEmpty(roleCandidates)) {
            for (String candidate : roleCandidates) {
                taskService.addCandidateGroup(taskBpmId, candidate);
            }
        }
    }

    @Override
    public void complete(String taskBpmId, Map<String, Object> param) {
        taskService.complete(taskBpmId, param);
    }

    @Override
    public void setAssignee(String taskBpmId, String assignee) {
        taskService.setAssignee(taskBpmId, assignee);
    }

    @Override
    public void deleteProcessInstance(String bpmKey, String suggestion) {
        runtimeService.deleteProcessInstance(bpmKey, suggestion);
    }

    @Override
    @SneakyThrows
    public BaseResult validate(String designContent) {
        if (StringUtil.isEmptyInFrame(designContent)) {
            return BaseResult.fail()
                    .buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.name(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "designContent")
                    .result();
        }
        BpmnXMLConverter converter = new BpmnXMLConverter();
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();

        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(StreamUtil.asStream(designContent));
        BpmnModel bpmnModel = converter.convertToBpmnModel(streamReader);
        return BaseResult.success(bpmnModel);
    }

    @Override
    public BaseResult actBack(BpmTaskInfo taskInfo, String backNodeCode) {

        // 取得所有历史任务按时间降序排序
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(taskInfo.getBpmInstKey())
                .orderByTaskCreateTime()
                .desc()
                .list();

        if (ObjectUtils.isEmpty(htiList) || htiList.size() < NumberConstant.INT_TWO) {
            throw new BaseBusinessException()
                    .buildParam(BpmMessageEnum.INSTANCE_DATA_LOST.key(), 1, 1)
                    .addParam(BpmMessageEnum.INSTANCE_DATA_LOST.params[0], taskInfo.getInstanceId())
                    .addParamI8n(BpmMessageEnum.INSTANCE_DATA_LOST.params[1], BpmInstanceDataTypeEnum.TaskInfoVO.key())
                    .exception();
        }

        HistoricTaskInstance lastActNode = null;
        for (HistoricTaskInstance historicTaskInstance : htiList) {
            if (historicTaskInstance.getTaskDefinitionKey().equals(backNodeCode)) {
                lastActNode = historicTaskInstance;
                break;
            }
        }

        if (lastActNode == null) {
            throw new BaseBusinessException()
                    .buildParam(BpmMessageEnum.NODE_HISTORY_TASK_NOT_EXISTS.key(), 1, 0)
                    .addParam(BpmMessageEnum.NODE_HISTORY_TASK_NOT_EXISTS.params[0], backNodeCode)
                    .exception();
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(taskInfo.getBpmModelKey());
        FlowNode lastFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(backNodeCode);
        FlowNode curFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(taskInfo.getNodeCode());

        //记录当前节点的原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<>();
        oriSequenceFlows.addAll(curFlowNode.getOutgoingFlows());

        //清理活动方向
        curFlowNode.getOutgoingFlows().clear();

        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
        curFlowNode.setOutgoingFlows(newSequenceFlowList);

        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlowList.add(newSequenceFlow);

        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(curFlowNode);
        newSequenceFlow.setTargetFlowElement(lastFlowNode);

        // 完成任务
        taskService.complete(taskInfo.getBpmKey());

        //恢复原方向
        curFlowNode.setOutgoingFlows(oriSequenceFlows);

        return BaseResult.SUCCESS;

    }

    @Override
    public void downloadInstPng(HttpServletResponse response, String bpmKey) {

        // 获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(bpmKey)
                .singleResult();

        // 获取流程中已经执行的节点，按照执行先后顺序排序
        List<HistoricActivityInstance> historicInstanceList = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(bpmKey)
                .orderByHistoricActivityInstanceId()
                .asc().list();

        // 高亮已经执行流程节点ID集合
        List<String> highLightedActivityIds = new ArrayList<>();
        int index = 1;
        for (HistoricActivityInstance historicInstance : historicInstanceList) {
            highLightedActivityIds.add(historicInstance.getActivityId());
            index++;
        }

        // 使用默认的程序图片生成器
        ProcessDiagramGenerator generator = new CustomProcessDiagramGenerator(activeColor, historyColor);

        // 获取bpmnModel
        String definitionId = historicProcessInstance.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);

        // 高亮流程已发生流转的线id集合
        List<String> highLightedFlowIds = this.getHighLightedFlows(bpmnModel, historicInstanceList);
        InputStream inputStream = generator.generateDiagram(bpmnModel, highLightedActivityIds, highLightedFlowIds,
                activityFontName, labelFontName, annotationFontName);

        response.setContentType("application/octet-stream; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename="
                + definitionId + CharConstant.DOT + imageType);

        try {
            ImageUtil.convertSvg2Png(inputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new BaseBusinessException();
        } finally {
            StreamUtil.closeQuietly(inputStream);
        }

    }


    /**
     * 获取高亮线
     *
     * @param bpmModel             流程模型
     * @param historicInstanceList 历史节点
     * @return
     */
    private List<String> getHighLightedFlows(BpmnModel bpmModel, List<HistoricActivityInstance> historicInstanceList) {
        // 流转线ID集合
        List<String> flowIdList = new ArrayList<>();

        // 全部活动实例
        List<FlowNode> historicFlowNodeList = new LinkedList<>();

        // 已完成的历史活动节点
        List<HistoricActivityInstance> finishedActivityInstanceList = new LinkedList<>();
        for (HistoricActivityInstance historicInstance : historicInstanceList) {
            historicFlowNodeList.add((FlowNode) bpmModel.getMainProcess()
                    .getFlowElement(historicInstance.getActivityId(), true));
            if (historicInstance.getEndTime() != null) {
                finishedActivityInstanceList.add(historicInstance);
            }
        }

        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        FlowNode currentFlowNode = null;
        for (HistoricActivityInstance currentActivityInstance : finishedActivityInstanceList) {
            // 获得当前活动对应的节点信息及outgoingFlows信息
            currentFlowNode = (FlowNode) bpmModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> sequenceFlowList = currentFlowNode.getOutgoingFlows();

            /**
             * 遍历outgoingFlows并找到已流转的
             * 满足如下条件认为已流转：
             * 1.当前节点是并行网关或包含网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转
             * 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最近的流转节点视为有效流转
             */
            FlowNode targetFlowNode = null;

            if (NodeTypeEnum.parallelGateway.toString().equals(currentActivityInstance.getActivityType())
                    || NodeTypeEnum.inclusiveGateway.toString().equals(currentActivityInstance.getActivityType())) {
                // 遍历历史活动节点，找到匹配Flow目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlowList) {
                    targetFlowNode = (FlowNode) bpmModel.getMainProcess()
                            .getFlowElement(sequenceFlow.getTargetRef(), true);
                    if (historicFlowNodeList.contains(targetFlowNode)) {
                        flowIdList.add(sequenceFlow.getId());
                    }
                }
            } else {
                List<Map<String, String>> tempMapList = new LinkedList<>();
                // 遍历历史活动节点，找到匹配Flow目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlowList) {
                    for (HistoricActivityInstance historicInstance : historicInstanceList) {
                        if (historicInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                            tempMapList.add(CollectionUtil.toMap(
                                    "flowId", sequenceFlow.getId(), "activityStartTime",
                                    String.valueOf(historicInstance.getStartTime().getTime())));
                        }
                    }
                }

                // 遍历匹配的集合，取得开始时间最早的一个
                long earliestStamp = 0L;
                String flowId = null;
                for (Map<String, String> map : tempMapList) {
                    long activityStartTime = Long.parseLong(map.get("activityStartTime"));
                    if (earliestStamp == 0 || earliestStamp >= activityStartTime) {
                        earliestStamp = activityStartTime;
                        flowId = map.get("flowId");
                    }
                }
                flowIdList.add(flowId);
            }
        }
        return flowIdList;
    }

    /**
     * 节点连线解析
     *
     * @param sequence 节点连线实体类
     * @return map
     */
    public BpmNodeRelateInfo generateNodeRelate(SequenceFlow sequence) {
        BpmNodeRelateInfo relateInfo = modelSupplier.newBpmNodeRelateInfo();
        String leadNodeCode = sequence.getSourceRef();
        String followNodeCode = sequence.getTargetRef();
        FlowElement sourceElement = sequence.getSourceFlowElement();

        relateInfo.setPriority(NumberConstant.INT_ONE);
        relateInfo.setEffectStrategy(StringConstant.NONE_OF_DATA);
        relateInfo.setLeadNodeCode(leadNodeCode);
        relateInfo.setFollowNodeCode(followNodeCode);

        // 判断节点关系类型
        if (sourceElement instanceof ExclusiveGateway) {
            relateInfo.setRelateType(NodeRelateTypeEnum.CONDITION.name());
        } else {
            relateInfo.setRelateType(NodeRelateTypeEnum.AGREE.name());
        }
        return relateInfo;
    }

    /**
     * 节点解析
     *
     * @param flowElement 流程节点实体类（非连线）
     * @return map
     */
    public BpmNodeInfo generateNodeInfo(FlowElement flowElement) {
        BpmNodeInfo nodeInfo = modelSupplier.newBpmNodeInfo();
        nodeInfo.setNodeName(flowElement.getName());
        nodeInfo.setNodeType(StringUtil.firstCharUppercase(flowElement.getClass().getSimpleName()));
        nodeInfo.setNodeCode(flowElement.getId());
        return nodeInfo;
    }

}
