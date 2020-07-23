package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.StringKeyValue;

import java.util.List;

/**
 * BPM实例帮助业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-07-23
 */
public interface BpmInstHelpService {

    /**
     * 选择当前活动的节点
     *
     * @param bpmInstId BPM流程实例ID
     * @return 活动节点编码
     */
    BaseResult<List<StringKeyValue>> selectActiveNodeCodeAndFlows(String bpmInstId);

}
