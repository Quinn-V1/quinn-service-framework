package com.quinn.framework.api.file;

import java.io.InputStream;

/**
 * 文件适配器（将不同的文件对对象转换为系统识别的对象）
 *
 * @author Qunhua.Liao
 * @since 2020-04-03
 */
public interface FileAdapter<T> {

    /**
     * 获取字饥饿数组
     *
     * @return 文件字节数据
     */
    byte[] getBytes();

    /**
     * 获取原文件名
     *
     * @return  文件原名称
     */
    String getFilename();

    /**
     * 获取文件大小
     *
     * @return  文件大小
     */
    Long getSize();

    /**
     * 获取文件类型
     *
     * @return  文件类型
     */
    String getFileType();

    /**
     * 获取文件输入流
     *
     * @return  输入流
     */
    InputStream getInputStream();

    /**
     * 文件存储路径（文件夹）
     *
     * @return  文件夹路径
     */
    String getFilePath();

    /**
     * 真实对象
     *
     * @return 真实对象
     */
    T realObject();

}
