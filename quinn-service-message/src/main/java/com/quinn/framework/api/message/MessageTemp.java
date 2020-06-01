package com.quinn.framework.api.message;

/**
 * 消息模板内容
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageTemp {

    /**
     * 数据编码（唯一区分）
     *
     * @return 唯一区分
     */
    String instanceKey();

    /**
     * 是否有默认地址
     *
     * @return 有默认地址：true
     */
    boolean hasDefaultAddress();

}
