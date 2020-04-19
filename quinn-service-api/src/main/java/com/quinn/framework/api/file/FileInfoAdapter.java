package com.quinn.framework.api.file;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件信息适配器
 *
 * @author Qunhua.Liao
 * @since 2020-04-16
 */
public interface FileInfoAdapter {

    /**
     * 获取文件相对路径
     *
     * @return 相对路径
     */
    String getFilePath();

    /**
     * 设置文件相对路径
     *
     * @param filePath 文件相对路径
     */
    void setFilePath(String filePath);

    /**
     * 获取文件保存名称
     *
     * @return 文件保存名称
     */
    String getStoreName();

    /**
     * 获取全路径
     *
     * @return 全路径
     */
    String getFullPath();

    /**
     * 设置前路径
     *
     * @param fullPath 全路径
     */
    void setFullPath(String fullPath);

    /**
     * 获取文件路径装饰策略
     *
     * @return 路径装饰策略
     */
    String[] getPathStrategies();

    /**
     * 获取输出流
     *
     * @return 输出流
     */
    OutputStream getOutputStream();

    /**
     * 获取输入流
     *
     * @return 输入流
     */
    InputStream getInputStream();

    /**
     * 获取输入流
     *
     * @param inputStream 输入流设置
     */
    void setInputStream(InputStream inputStream);

    /**
     * 实际文件信息
     *
     * @return
     */
    <T> T realFileInfo();

    /**
     * 文件大小
     *
     * @return  文件大小
     */
    long getFileSize();

    /**
     * 文件类型
     *
     * @return  文件类型
     */
    String getFileType();

    /**
     * 设置保存名称
     *
     * @param fullPath
     */
    void setStorageName(String fullPath);

}
