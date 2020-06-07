package com.quinn.framework.api.message;

/**
 * 消息模板内容
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageTempContent {

    /**
     * 数据编码（唯一区分）
     *
     * @return 唯一区分
     */
    String sendGroup();

    /**
     * 返回替换类型
     *
     * @return 替换类型
     */
    Integer getPlaceTypes();

    /**
     * 返回内容模板
     *
     * @return 消息模板
     */
    String getContentTemplate();

    /**
     * 主题模板
     *
     * @return 主题模板
     */
    String getSubjectTemplate();

    /**
     * 链接路径模板
     *
     * @return 链接路径模板
     */
    String getUrlTemplate();

    /**
     * 获取附件模板
     *
     * @return 附件模板
     */
    String getAttachmentTemplate();

    /**
     * 获取消息类型
     *
     * @return 消息类型
     */
    String getMessageType();

    /**
     * 获取语言编码
     *
     * @return 语言编码
     */
    String getLangCode();

    /**
     * 获取模板主键
     *
     * @return 模板主键
     */
    Long getTemplateId();

    /**
     * 获取模板编码
     *
     * @return 模板编码
     */
    String getTemplateKey();

}
