package com.quinn.framework.api.message;

import com.quinn.util.base.model.BaseResult;

import java.util.List;
import java.util.Map;

/**
 * 地址解析器（每个实现应该区分"消息类型:收件人类型"）
 *
 * @author Qunhua.Liao
 * @since 2020-02-10
 */
public interface MessageAddressResolver {

    /**
     * 解析动作：生成address、receiver两个属性消息
     *
     * @param resolveValue 解析值
     * @param cond         消息参数
     * @return 消息发送记录
     */
    BaseResult<List<MessageSendRecord>> resolve(String resolveValue, Map<String, Object> cond);

}
