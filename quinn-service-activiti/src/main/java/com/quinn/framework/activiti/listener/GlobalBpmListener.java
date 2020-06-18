package com.quinn.framework.activiti.listener;

import com.quinn.framework.api.BpmTaskInfo;
import com.quinn.framework.api.CustomBpmEventListener;
import com.quinn.framework.activiti.model.ActToBpmInfoFactory;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局BPM监听器
 *
 * @author Qunhua.Liao
 * @since 2020-05-06
 */
public class GlobalBpmListener implements ActivitiEventListener {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(GlobalBpmListener.class);

    /**
     * <时间类型, 自定义监听器>
     */
    private final static Map<String, CustomBpmEventListener> CUSTOM_BPM_EVENT_LISTENER_MAP = new HashMap<>();

    @Value("${com.ming-cloud.bpm.global.fail-on-exception:false}")
    private boolean failOnException;

    @Override
    public void onEvent(ActivitiEvent event) {
        LOGGER.error("GlobalBpmListener entry {0}", event.getType());
        CustomBpmEventListener customBpmEventListener = CUSTOM_BPM_EVENT_LISTENER_MAP.get(event.getType().name());
        if (customBpmEventListener == null) {
            return;
        }

        LOGGER.error("GlobalBpmListener continue {0}", event.getType());
        BpmTaskInfo bpmTaskInfo;
        if (event instanceof ActivitiEntityEvent) {
            bpmTaskInfo = ActToBpmInfoFactory.toTaskInfo(((ActivitiEntityEvent) event).getEntity());
        } else {
            bpmTaskInfo = ActToBpmInfoFactory.toTaskInfo(event);
        }

        LOGGER.error("BpmTaskInfo got");
        customBpmEventListener.listen(bpmTaskInfo, event.getExecutionId(), event.getProcessInstanceId(),
                event.getProcessDefinitionId());
    }

    @Override
    public boolean isFailOnException() {
        return failOnException;
    }

    /**
     * 添加自定义
     *
     * @param type                   时间类型
     * @param customBpmEventListener 监听器
     */
    public static void addCustomBpmEventListener(String type, CustomBpmEventListener customBpmEventListener) {
        CUSTOM_BPM_EVENT_LISTENER_MAP.put(type, customBpmEventListener);
    }
}
