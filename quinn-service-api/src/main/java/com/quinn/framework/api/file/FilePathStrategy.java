package com.quinn.framework.api.file;

/**
 * 文件路径生成策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-05
 */
public interface FilePathStrategy {

    /**
     * 策略名称
     *
     * @return 策略名称
     */
    String name();

    /**
     * 修饰文件路径
     *
     * @param fileInfoAdapter 原文件信息
     */
    void decoratePath(FileInfoAdapter fileInfoAdapter);

}
