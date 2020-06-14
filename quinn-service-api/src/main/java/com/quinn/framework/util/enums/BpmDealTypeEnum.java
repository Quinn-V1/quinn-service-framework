package com.quinn.framework.util.enums;

import com.quinn.util.base.NumberUtil;
import com.quinn.util.constant.NumberConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * BPM 处理类型（某些场景可多选，支持位存储）
 *
 * @author Qunhua.Liao
 * @since 2020-05-12
 */
public enum BpmDealTypeEnum {

    // 人工拒绝
    REJECT(1),

    // 人工转办
    ASSIGN(2),

    // 人工撤回
    REVOKE(4),

    // 人工终止
    TERMINATE(8),

    // 抄送
    COPY(16),

    // 沟通
    COMMUNICATE(32),

    // 启动
    START(-16),

    // 跳过
    SKIP(-8),

    // 系统自动
    AUTO(-4),

    // 结束
    END(-2),

    // 系统转办
    ASSIGN_SYS(-1),

    // 反馈
    FEEDBACK(0),

    // 沟通
    READ(0),

    // 人工同意
    AGREE(0),

    ;

    /**
     * 吸选操作
     */
    public final static BpmDealTypeEnum[] OPTION_DEAL_TYPES = new BpmDealTypeEnum[]{REJECT, ASSIGN, REVOKE, TERMINATE,
            COPY, COMMUNICATE,
    };

    /**
     * 编码
     */
    public final int code;

    BpmDealTypeEnum(int code) {
        this.code = code;
    }

    /**
     * 变成字符串数组
     *
     * @param dealTypes 支持操作类型的合成整型
     * @return 支持操作类型
     */
    public static String[] asStrings(Integer dealTypes) {
        if (NumberUtil.isEmptyInFrame(dealTypes)) {
            return null;
        }

        List<String> results = new ArrayList<>();
        for (BpmDealTypeEnum dealTypeEnum : OPTION_DEAL_TYPES) {
            if (dealTypeEnum.accept(dealTypes)) {
                results.add(dealTypeEnum.name());
            }
        }

        return results.toArray(new String[results.size()]);
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

}
