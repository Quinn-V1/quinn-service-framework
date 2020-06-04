package com.quinn.framework.api.message;

/**
 * 消息模板内容
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageTemp {

    /**
     * 获取模板ID
     *
     * @return 模板ID
     */
    Long getId();

    /**
     * 获取模板编码
     *
     * @return 模板编码
     */
    String getTemplateKey();

    /**
     * 是否有默认地址
     *
     * @return 有默认地址：true
     */
    boolean hasDefaultAddress();

}
