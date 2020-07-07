package com.quinn.framework.util.enums;

import com.quinn.util.base.NumberUtil;
import com.quinn.util.base.handler.EnumMessageResolver;
import com.quinn.util.constant.MessageEnumFlag;
import com.quinn.util.constant.NumberConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * BPM 处理类型（某些场景可多选，支持位存储）
 *
 * @author Qunhua.Liao
 * @since 2020-05-12
 */
public enum BpmDealTypeEnum implements MessageEnumFlag {

    // 人工驳回
    REJECT(1, "驳回"),

    // 人工转办
    ASSIGN(2, "转办"),
    // 沟通
    COMMUNICATE(4, "沟通"),

    // 抄送
    COPY(8, "抄送"),

    // 人工撤回
    REVOKE(16, "撤回"),

    // 人工终止
    TERMINATE(32, "终止"),

    // 启动
    START(-16, "启动"),

    // 跳过
    SKIP(-8, "跳过"),

    // 系统自动
    AUTO(-4, "自动"),

    // 结束
    END(-2, "结束"),

    // 系统转办
    ASSIGN_SYS(-1, "转办"),

    // 反馈
    FEEDBACK(0, "反馈"),

    // 阅读
    READ(0, "阅读"),

    // 人工同意
    AGREE(0, "同意"),

    ;

    /**
     * 可选操作
     */
    public final static BpmDealTypeEnum[] OPTION_DEAL_TYPES = new BpmDealTypeEnum[]{REJECT, ASSIGN, COPY, COMMUNICATE,
            REVOKE, TERMINATE,
    };

    /**
     * 同意名称
     */
    public static final String AGREE_NAME = "AGREE";

    /**
     * 阅读名称
     */
    public static final String READ_NAME = "READ";

    /**
     * 反馈名称
     */
    public static final String FEEDBACK_NAME = "FEEDBACK";

    /**
     * 拒绝名称
     */
    public static final String REJECT_NAME = "REJECT";

    /**
     * 转办名称
     */
    public static final String ASSIGN_NAME = "ASSIGN";

    /**
     * 撤回名称
     */
    public static final String REVOKE_NAME = "REVOKE";

    /**
     * 终止名称
     */
    public static final String TERMINATE_NAME = "TERMINATE";

    /**
     * 抄送名称
     */
    public static final String COPY_NAME = "COPY";

    /**
     * 沟通名称
     */
    public static final String COMMUNICATE_NAME = "COMMUNICATE";

    static {
        EnumMessageResolver.addContent(Locale.SIMPLIFIED_CHINESE, BpmDealTypeEnum.values());
    }

    /**
     * 编码
     */
    public final int code;

    /**
     * 默认显示名称
     */
    public final String defaultDesc;

    BpmDealTypeEnum(int code, String defaultDesc) {
        this.code = code;
        this.defaultDesc = defaultDesc;
    }

    /**
     * 变成字符串数组
     *
     * @param dealTypes 支持操作类型的合成整型
     * @return 支持操作类型
     */
    public static List<String> asStrings(Integer dealTypes) {
        if (NumberUtil.isEmptyInFrame(dealTypes)) {
            return new ArrayList<>(0);
        }

        List<String> results = new ArrayList<>();
        for (BpmDealTypeEnum dealTypeEnum : OPTION_DEAL_TYPES) {
            if (dealTypeEnum.accept(dealTypes)) {
                results.add(dealTypeEnum.name());
            }
        }

        return results;
    }

    /**
     * 合成整型
     *
     * @param dealTypesStr 支持操作类型数组
     * @return 合成整型
     */
    public static Integer asInteger(String[] dealTypesStr) {
        if (dealTypesStr == null) {
            return NumberConstant.INT_ZERO;
        }

        Integer result = 0;
        for (String dealType : dealTypesStr) {
            for (BpmDealTypeEnum dealTypeEnum : OPTION_DEAL_TYPES) {
                if (dealTypeEnum.name().equals(dealType)) {
                    result = result | dealTypeEnum.code;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 是否支持本操作
     *
     * @return 是否支持
     */
    public boolean accept(Integer dealTypes) {
        if (code <= 0) {
            return true;
        }
        return (code & dealTypes) > 0;
    }

    /**
     * 处理类型是否被支持
     *
     * @param dealTypeCodes 处理类型（选项位存储）
     * @param dealType      处理类型
     * @return 是否支持
     */
    public static boolean dealTypeSupport(Integer dealTypeCodes, String dealType) {
        int code = BpmDealTypeEnum.valueOf(dealType).code;
        if (code <= 0) {
            return true;
        }
        return (dealTypeCodes & code) > 0;
    }

    @Override
    public String defaultDesc() {
        return defaultDesc;
    }

    @Override
    public String[] paramNames() {
        return null;
    }
}
