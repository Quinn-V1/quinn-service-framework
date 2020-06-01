package com.quinn.framework.util.enums;

import com.quinn.util.base.handler.EnumMessageResolver;
import com.quinn.util.constant.MessageEnumFlag;
import com.quinn.util.constant.StringConstant;

import java.util.Locale;

/**
 * 通用数据类型枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-05-29
 */
public enum CommonDataTypeEnum implements MessageEnumFlag {

    // 租户
    TENANT("OrgInfoVO", "租户"),

    // 授权信息类型
    AUTH_TYPE("AuthTypeEnum", "授权信息类型"),

    // 证书比较类型 (Credentials Matcher)
    CM_TYPE("CmTypeEnum", "证书比较类型"),

    ;

    /**
     * 实际编码（对应数据库实体）
     */
    public final String code;

    /**
     * 默认描述
     */
    public final String defaultDesc;

    CommonDataTypeEnum(String code, String defaultDesc) {
        this.code = code;
        this.defaultDesc = defaultDesc;
    }

    @Override
    public String defaultDesc() {
        return this.defaultDesc;
    }

    @Override
    public String[] paramNames() {
        return null;
    }

    static {
        EnumMessageResolver.addContent(Locale.SIMPLIFIED_CHINESE, CommonDataTypeEnum.values());
    }

    /**
     * 拼接消息主键编码
     *
     * @param dataType 数据类型
     * @return Message Key
     */
    public static final String wrapperKey(String dataType) {
        return "DataTypeEnum" + StringConstant.CHAR_COLON + dataType
                + StringConstant.CHAR_POUND_SIGN + StringConstant.NONE_OF_DATA;
    }

}
