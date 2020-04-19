package com.quinn.framework.configuration;

import com.quinn.framework.component.JasyptEncryption;
import com.quinn.util.base.util.StringUtil;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jasypt加密配置类
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
@Configuration
public class JasyptEncryptionConfiguration {

    /**
     * 默认加密盐
     */
    private static final String DEFAULT_SALT = "quinn-service";

    /**
     * 默认加密后字符串冠以前缀，用于区别此项配置是加过密的
     */
    private static final String DEFAULT_ENCRYPT_PREFIX = "ENC(";

    /**
     * 默认加密后字符串冠以前缀，用于区别此项配置是加过密的
     */
    private static final String DEFAULT_ENCRYPT_SUFFIX = ")";

    @Value("${bjqh.framework.crypt.algorithm:" + JasyptEncryption.DEFAULT_ALGORITHM + "}")
    private String algorithm;

    @Value("${bjqh.framework.crypt.salt:" + DEFAULT_SALT + "}")
    private String encryptSalt;

    @Value("${bjqh.framework.crypt.prefix:" + DEFAULT_ENCRYPT_PREFIX + "}")
    private String encryptPrefix;

    @Value("${bjqh.framework.crypt.prefix:" + DEFAULT_ENCRYPT_SUFFIX + "}")
    private String encryptSuffix;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm(algorithm);
        config.setPassword(StringUtil.isNotEmpty(encryptSalt) ? encryptSalt : DEFAULT_SALT);

        encryptor.setConfig(config);
        return encryptor;
    }

    @Bean(name = "encryptablePropertyDetector")
    public EncryptablePropertyDetector encryptablePropertyDetector() {

        final String prefix = StringUtil.isNotEmpty(encryptPrefix) ? encryptPrefix : DEFAULT_ENCRYPT_PREFIX;

        final String suffix = StringUtil.isNotEmpty(encryptSuffix) ? encryptSuffix : DEFAULT_ENCRYPT_SUFFIX;

        return new EncryptablePropertyDetector() {
            @Override
            public boolean isEncrypted(String value) {
                if (value != null) {
                    return value.startsWith(prefix) && value.endsWith(suffix);
                }
                return false;
            }

            @Override
            public String unwrapEncryptedValue(String value) {
                return value.substring(prefix.length(), value.length() - suffix.length());
            }
        };
    }

}
