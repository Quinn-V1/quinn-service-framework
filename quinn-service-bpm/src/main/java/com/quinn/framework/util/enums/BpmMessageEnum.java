package com.quinn.framework.util.enums;

import com.quinn.framework.util.BpmParamName;
import com.quinn.util.base.handler.EnumMessageResolver;
import com.quinn.util.constant.CommonParamName;
import com.quinn.util.constant.MessageEnumFlag;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.LanguageEnum;

import static com.quinn.framework.util.BpmMessageConstant.*;

/**
 * 消息枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-05-25
 */
public enum BpmMessageEnum implements MessageEnumFlag {

    // 流程【${instanceId}】非您所有，请核对
    INST_IS_NOT_YOURS(DESC_INST_IS_NOT_YOURS, BpmParamName.INSTANCE_ID),

    // 任务【${instanceId} - ${taskId}】非您所有，请核对
    TASK_IS_NOT_YOURS(DESC_TASK_IS_NOT_YOURS, BpmParamName.INSTANCE_ID, BpmParamName.TASK_ID),

    // 任务【${taskId}】不存在
    TASK_NOT_EXITS(DESC_TASK_NOT_EXITS, BpmParamName.TASK_ID),

    // 流程【${instanceId}】缺失${dataType}数据
    INSTANCE_DATA_LOST(DESC_INSTANCE_DATA_LOST, BpmParamName.INSTANCE_ID, CommonParamName.PARAM_DATA_TYPE),

    // 历史节点【${nodeCode}】不存在已办任务
    NODE_HISTORY_TASK_NOT_EXISTS(DESC_NODE_HISTORY_TASK_NOT_EXISTS, BpmParamName.NODE_CODE),

    // 当前节点不支持【${dealType}】操作
    DEAL_TYPE_NOT_SUPPORT_NOW(DESC_DEAL_TYPE_NOT_SUPPORT_NOW, BpmParamName.DEAL_TYPE),

    // 【${operateType}】流程失败，【${dataType}】数据【${dataKey}】异常【${exceptionMsg}】
    MODEL_INFO_OPERATION_ERROR(DESC_MODEL_INFO_OPERATION_ERROR, CommonParamName.PARAM_OPERATE_TYPE,
            CommonParamName.PARAM_DATA_TYPE, CommonParamName.PARAM_DATA_KEY, CommonParamName.PARAM_EXPECT_CLASS),
    ;

    /**
     * 默认描述
     */
    public final String defaultDesc;

    /**
     * 参数
     */
    public final String[] params;

    BpmMessageEnum(String defaultDesc, String... params) {
        this.defaultDesc = defaultDesc;
        this.params = params;
    }

    @Override
    public String defaultDesc() {
        return defaultDesc;
    }

    @Override
    public String[] paramNames() {
        return params;
    }

    @Override
    public String key() {
        return StringConstant.DATA_TYPE_OF_MESSAGE + StringConstant.CHAR_COLON + name()
                + StringConstant.CHAR_POUND_SIGN + StringConstant.NONE_OF_DATA;
    }

    static {
        EnumMessageResolver.addContent(LanguageEnum.zh_CN.locale, BpmMessageEnum.values());
    }
}
