package com.quinn.framework.model;

import com.quinn.framework.api.message.MessageInstance;
import com.quinn.framework.api.message.MessageTempContent;

import java.util.Map;

/**
 * 消息信息工厂
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public class MessageInfoFactory {

    /**
     * 创建消息实例
     *
     * @param content      消息内容
     * @param messageParam 消息参数
     * @param fromSystem   来源系统
     * @param businessKey  业务主键
     * @return
     */
    public static MessageInstance createInstance(MessageTempContent content, Map<String, Object> messageParam,
                                                 String fromSystem, String businessKey) {
        return null;
    }
}
