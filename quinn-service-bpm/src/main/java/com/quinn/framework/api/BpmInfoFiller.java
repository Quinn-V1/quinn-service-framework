package com.quinn.framework.api;

import com.quinn.util.base.api.FileAdapter;
import com.quinn.util.base.model.BaseResult;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * BPM信息填充
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public interface BpmInfoFiller {

    /**
     * 生成流程模型信息
     *
     * @param adapter   文件适配器
     * @param modelInfo 流程模型信息
     */
    void generateModelInfo(FileAdapter adapter, BpmModelInfo modelInfo);

    /**
     * 部署流程模板
     *
     * @param data 流程模板
     */
    void deployModel(BpmModelInfo data);

    /**
     * 解析节点信息
     *
     * @param data       流程模板信息
     * @param relateInfo 节点关系综合信息
     */
    void resolveNodeInfo(BpmModelInfo data, BpmNodeRelateBO relateInfo);

    /**
     * 下载PNG图片
     *
     * @param response 响应对象
     * @param bpmKey   BPM主键
     */
    void downloadModelPng(HttpServletResponse response, String bpmKey);

    /**
     * 回退
     *
     * @param taskInfo     当前任务信息
     * @param backNodeCode 回退任务编码
     * @return
     */
    BaseResult actBack(BpmTaskInfo taskInfo, String backNodeCode);

    /**
     * 下载PNG图片
     *
     * @param response 响应对象
     * @param bpmKey   BPM主键
     */
    void downloadInstPng(HttpServletResponse response, String bpmKey);

    /**
     * 启动流程
     *
     * @param bpmInstInfo 流程信息
     * @param param       参数
     */
    void start(BpmInstInfo bpmInstInfo, Map<String, Object> param);

    /**
     * 添加候选人
     *
     * @param taskBpmId      BPM内TaskId
     * @param userCandidates 用户候选人
     * @param roleCandidates 角色候选人
     */
    void addCandidates(String taskBpmId, Set<String> userCandidates, Set<String> roleCandidates);

    /**
     * 完成任务向下走
     *
     * @param taskBpmId BPM任务ID
     * @param param     流程参数
     */
    void complete(String taskBpmId, Map<String, Object> param);

    /**
     * 设置审批人
     *
     * @param taskBpmId BPM任务ID
     * @param assignee  流程参数
     */
    void setAssignee(String taskBpmId, String assignee);

    /**
     * 删除流程实例
     *
     * @param bpmKey     流程BPM ID
     * @param suggestion 原因
     */
    void deleteProcessInstance(String bpmKey, String suggestion);

}
