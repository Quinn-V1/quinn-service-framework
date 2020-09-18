package com.quinn.framework.util.enums;

/**
 * 候选人类型策略
 *
 * @author Qunhu.Liao
 * @since 2020-05-13
 */
public enum CandidateTypeEnum {

    // 用户
    USER("", ""),

    // 角色
    ROLE("", ""),

    // 角色-直接
    ROLE_DIR("str", "splitByComma"),

    // 用户-直接
    USER_DIR("str", "splitByComma"),

    // 岗位-直接
    USER_STA("stationKey", "selectClosestUserOrgBy"),

    ;

    /**
     * 参数名
     */
    public final String paramName;

    /**
     * 脚本编码
     */
    public final String scriptKey;

    CandidateTypeEnum(String paramName, String scriptKey) {
        this.paramName = paramName;
        this.scriptKey = scriptKey;
    }

}
