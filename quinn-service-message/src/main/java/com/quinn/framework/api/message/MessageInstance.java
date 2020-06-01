package com.quinn.framework.api.message;

/**
 * 消息实例
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageInstance {

    /**
     * 设置消息批次号（表示一批消息是同次发送的）
     *
     * @param batchNo 批次号
     */
    void setBatchNo(String batchNo);

    /**
     * 数据编码
     *
     * @return 数据编码
     */
    String instanceKey();

}
