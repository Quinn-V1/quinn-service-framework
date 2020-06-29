package com.quinn.framework.component;

import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.base.AesEncoder;
import com.quinn.util.constant.OrderedConstant;

/**
 * 配置信息重写类（默认）
 *
 * @author Qunhua.Liao
 * @since 2020-05-10
 */
public class DefaultConfigInfoWriter extends BaseConfigInfoReWriter {

    private String valuePrefix;

    private String valueSuffix;

    private String salt;

    {
        this.valuePrefix = System.getProperty(ConfigConstant.PROP_KEY_OF_ENCRYPT_VALUE_PREFIX, ConfigConstant.ENCRYPT_VALUE_PREFIX);
        this.valueSuffix = System.getProperty(ConfigConstant.PROP_KEY_OF_ENCRYPT_VALUE_SUFFIX, ConfigConstant.ENCRYPT_VALUE_SUFFIX);
        this.salt = System.getProperty(ConfigConstant.PROP_KEY_OF_ENCRYPT_SALT, ConfigConstant.ENCRYPT_SALT);
    }

    @Override
    public int getPriority() {
        return OrderedConstant.MEDIUM;
    }

    @Override
    public String encryptValue(String value) {
        String encode = AesEncoder.encode(value, salt);
        return valuePrefix + encode + valueSuffix;
    }

    @Override
    public String decryptValue(String value) {
        String encode = value.substring(valuePrefix.length(), value.length() - valueSuffix.length());
        return AesEncoder.decode(encode, salt);
    }

    @Override
    public boolean valueMatches(Object value) {
        if (value == null) {
            return false;
        }

        String str = value.toString();
        return str.startsWith(valuePrefix) && str.endsWith(valueSuffix);
    }

}
