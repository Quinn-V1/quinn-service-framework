package com.quinn.framework.api.file;

import com.quinn.util.base.model.BaseResult;

import java.io.InputStream;

/**
 * 文件存储业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-03
 */
public interface StorageService {

    /**
     * 上传文件
     *
     * @param fileInfoAdapter 文件信息
     * @return 文件保存结果，同时补全了保存成功后的信息
     */
    BaseResult<FileInfoAdapter> upload(FileInfoAdapter fileInfoAdapter);

    /**
     * 下载文件
     *
     * @param fileInfoAdapter 文件信息
     */
    void download(FileInfoAdapter fileInfoAdapter);

    /**
     * 将文件读如内存
     *
     * @param fileInfoAdapter 文件信息
     * @return 获取文件流
     */
    InputStream readAsStream(FileInfoAdapter fileInfoAdapter);

    /**
     * 判断文件是否存在
     *
     * @param fileInfoAdapter 文件信息
     * @return 文件是否存在
     */
    boolean exists(FileInfoAdapter fileInfoAdapter);

    /**
     * 删除文件
     *
     * @param fileInfoAdapter 文件信息
     * @return 删除文件结果
     */
    BaseResult<FileInfoAdapter> delete(FileInfoAdapter fileInfoAdapter);

    /**
     * 获取服务类型：一般对应一种不同的实现方式
     *
     * @return 存储服务类型
     */
    String storageType();

}
