package com.quinn.framework.component;

import org.jasypt.intf.service.JasyptStatelessService;

import java.util.Properties;

/**
 * Jasypt加密工具类
 *
 * @author Qunhua.Liao
 * @since 2020-04-01
 */
public class JasyptEncryption {

    public static final String DEFAULT_ALGORITHM = "PBEWithMD5AndDES";

    /**
     * 加密
     *
     * @param input             需要加密的源字符串
     * @param password          加密盐
     * @param argumentValues    其他参数
     * @return                  加密后的字符串
     */
    public static String encrypt(String input, String password, Properties argumentValues) {
        try {
            if (argumentValues == null) {
                argumentValues = new Properties();
            }

            String algorithm =  argumentValues.getProperty("ALGORITHM", DEFAULT_ALGORITHM);
            JasyptStatelessService service = new JasyptStatelessService();
            String result = service.encrypt(
                    input,
                    password,
                    null,
                    null,
                    algorithm,
                    null,
                    null,
                    argumentValues.getProperty("keyObtentionIterations"),
                    null,
                    null,
                    argumentValues.getProperty("saltGeneratorClassName"),
                    null,
                    null,
                    argumentValues.getProperty("providerName"),
                    null,
                    null,
                    argumentValues.getProperty("providerClassName"),
                    null,
                    null,
                    argumentValues.getProperty("stringOutputType"),
                    null,
                    null
            );
            return result;
        } catch (Throwable var8) {
        }
        return null;
    }

    /**
     * 解密
     *
     * @param input             加密后的字符串
     * @param password          加密盐
     * @param argumentValues    其它参数
     * @return                  源字符串
     */
    public static String decrypt(String input, String password, Properties argumentValues) {
        try {
            if (argumentValues == null) {
                argumentValues = new Properties();
            }

            String algorithm =  argumentValues.getProperty("ALGORITHM", DEFAULT_ALGORITHM);
            JasyptStatelessService service = new JasyptStatelessService();
            String result = service.decrypt(
                    input,
                    password,
                    null,
                    null,
                    algorithm,
                    null,
                    null,
                    argumentValues.getProperty("keyObtentionIterations"),
                    null,
                    null,
                    argumentValues.getProperty("saltGeneratorClassName"),
                    null,
                    null,
                    argumentValues.getProperty("providerName"),
                    null,
                    null,
                    argumentValues.getProperty("providerClassName"),
                    null,
                    null,
                    argumentValues.getProperty("stringOutputType"),
                    null,
                    null
            );
            return result;
        } catch (Throwable var8) {
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(encrypt("root", "quinn-service", null));
    }

}
