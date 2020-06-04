package com.quinn.framework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quinn.framework.api.message.MessageReceiver;
import com.quinn.framework.util.MessageParamName;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.MethodInvokerOneParam;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.CommonParamName;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.ParamTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 消息发送参数
 *
 * @author Qunhua.Liao
 * @since 2020-02-06
 */
@Getter
@Setter
@ApiModel("消息发送参数")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageSendParam {

    /**
     * 参数方案列表
     */
    private static Map<String, MessageSendParam> paramExampleMap = new HashMap<>();

    /**
     * 漠然参数方案
     */
    private static MessageSendParam defaultInstance;

    /**
     * 来源系统
     */
    @ApiModelProperty("消息来源系统")
    private String fromSystem = "DEFAULT_SYSTEM";

    /**
     * 业务主键
     */
    @ApiModelProperty("消息业务主键")
    private String businessKey;

    /**
     * 消息模板编码
     */
    @ApiModelProperty("消息模板编码")
    private String templateKey;

    /**
     * 消息类型
     */
    @ApiModelProperty("消息类型")
    private String messageType;

    /**
     * 服务主键
     */
    @ApiModelProperty("消息服务主键")
    private Long serverId;

    /**
     * 优先级
     */
    @ApiModelProperty("优先级")
    private Integer priority;

    /**
     * 语言编码
     */
    @ApiModelProperty("语言编码")
    private String langCode;

    /**
     * 主题
     */
    @ApiModelProperty("消息主题")
    private String subject;

    /**
     * 内容
     */
    @ApiModelProperty("消息内容")
    private String content;

    /**
     * 链接地址
     */
    @ApiModelProperty("消息连接URL")
    private String url;

    /**
     * 附件地址
     */
    @ApiModelProperty("消息附件地址")
    private String attachment;

    /**
     * 消息参数
     */
    @ApiModelProperty("消息占位参数")
    private Map<String, Object> messageParam;

    /**
     * 收件对象
     */
    @ApiModelProperty("收件对象")
    private List<MessageReceiver> receivers;

    /**
     * 收件对象类型
     */
    @ApiModelProperty(value = "收件对象类型：用户、角色、直接，对应baseType")
    private String receiverType;

    /**
     * 收件对象值
     */
    @ApiModelProperty("收件类型值")
    private String receiverValue;

    /**
     * 发送用户
     */
    @ApiModelProperty("发送用户")
    private String sender;

    /**
     * 发送时间
     */
    @ApiModelProperty("发送时间")
    private LocalDateTime sendTime;

    /**
     * 安全获取用户
     *
     * @return 用户编码
     */
    public String senderOfSave() {
        return StringUtil.isEmptyInFrame(sender) ? SessionUtil.getUserKey() : sender;
    }

    /**
     * 获取参数之后进行初始化
     *
     * @return 校验之后还要返回发送方案
     */
    public BaseResult<Integer> validate() {
        // 是否指定收件人信息
        boolean hasReceivers = !CollectionUtil.isEmpty(receivers) || (StringUtil.isNotEmpty(receiverType)
                && StringUtil.isEmpty(receiverValue));

        // 没有指定消息模板的情况下
        if (StringUtil.isEmptyInFrame(templateKey)) {
            if (!hasReceivers) {
                // FIXME
                return BaseResult.fail("没有收件人信息");
            }

            // 消息内容和主题的判空与相互赋值
            if (StringUtil.isEmptyInFrame(content) || StringUtil.isEmptyInFrame(subject)) {
                // FIXME
                return BaseResult.fail("没有指定消息内容");
            }
        }

        // 如果指定了地址 但是没有 receivers：通过receiverType 和 receiverValue解析
        if (CollectionUtil.isEmpty(receivers) && hasReceivers) {
            String[] rvs = receiverValue.split(StringConstant.CHAR_COMMA);
            if (receivers == null) {
                receivers = new ArrayList<>();
            }

            for (String rv : rvs) {
                MessageReceiver receiver = new DefaultMessageReceiver();
                receiver.setReceiverType(receiverType);
                receiver.setReceiverValue(rv);
                receivers.add(receiver);
            }
        }

        this.sendTime = LocalDateTime.now();
        if (messageParam == null) {
            messageParam = new HashMap<>(NumberConstant.INT_EIGHT);
        }

        messageParam.put(CommonParamName.PARAM_KEY_NOW_TIME, this.sendTime);
        CollectionUtil.nullSafePut(messageParam, MessageParamName.PARAM_KEY_MESSAGE_TEMP_KEY, this.templateKey);
        CollectionUtil.nullSafePut(messageParam, MessageParamName.PARAM_KEY_MESSAGE_MESSAGE_TYPE, this.messageType);

        // FIXME (根据实际参数返回解决方案)
        return BaseResult.success(NumberConstant.INT_ONE);
    }

    /**
     * 从收件对象中获取书香
     *
     * @param oneParamOperation 属性获取器
     * @param <V>               返回对象类型
     * @return 属性值
     */
    public <V> V[] getPropertyFromReceiver(MethodInvokerOneParam<MessageReceiver, V> oneParamOperation) {
        if (receivers == null) {
            return null;
        }

        if (receivers.size() == 1) {
            V res = oneParamOperation.invoke(receivers.get(0));
            if (StringUtils.isEmpty(res)) {
                return null;
            }
            V[] result = (V[]) new Objects[1];
            result[1] = res;
            return result;
        } else {
            Set<V> result = new HashSet<>();
            for (MessageReceiver receiver : receivers) {
                V lc = oneParamOperation.invoke(receiver);
                if (!StringUtils.isEmpty(lc)) {
                    result.add(lc);
                }
            }
            return (V[]) result.toArray(new Objects[result.size()]);
        }
    }

    static {
        defaultInstance = new MessageSendParam();
        defaultInstance.setFromSystem("WORK_ORDER_NOTIFY");
        defaultInstance.setBusinessKey("WO_NO000001");
        defaultInstance.setTemplateKey(StringConstant.NONE_OF_DATA);

        List<MessageReceiver> receiverList = new ArrayList<>();
        defaultInstance.setReceivers(receiverList);
        // FIXME 添加消息接受对象

        Map<String, Object> param = new HashMap<>();
        defaultInstance.setMessageParam(param);

        param.put(CommonParamName.PARAM_KEY_TYPE, ParamTypeEnum.JSON.name());

        List<Map<String, Object>> paramValue = new ArrayList<>();
        param.put(CommonParamName.PARAM_KEY_RUNTIME_PARAM, paramValue);

        Map<String, Object> cond = new HashMap<>();
        paramValue.add(cond);
        cond.put("workNo", "workNo0001");
        cond.put("content", "content0001");
        cond.put("equipment", "equipment0001");
        cond.put("reportUser", "reportUser0001");

        paramExampleMap.put("DEFAULT", defaultInstance);
    }

    /**
     * 消息参赛断裂参数实例
     *
     * @param caseNo 参数方案
     * @return 消息参数实例
     */
    public static MessageSendParam getInstance(String caseNo) {
        if (StringUtils.isEmpty(caseNo)) {
            return defaultInstance;
        }

        MessageSendParam param = paramExampleMap.get(caseNo.toUpperCase());
        if (param == null) {
            return defaultInstance;
        }
        return param;
    }

}
