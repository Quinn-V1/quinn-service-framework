package com.quinn.framework.api.file;

import com.quinn.util.base.api.FileAdapter;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;

/**
 * 文件处理
 *
 * @author Qunhua.Liao
 * @since 2020-04-16
 */
public interface FileHandler {

    /**
     * 上传文件
     *
     * @param fileAdapter 文件适配器
     * @return 文件索引信息
     */
    BaseResult<FileInfoAdapter> upload(FileAdapter fileAdapter);

    /**
     * 文件详细信息
     *
     * @param fileKey 文件编码
     * @return 文件信息
     */
    BaseResult<FileInfoAdapter> fileInfo(String fileKey);

    /**
     * 校验文件是否已经存在
     *
     * @param fileAdapter 文件信息
     * @return 是否已存在
     */
    boolean exists(FileAdapter fileAdapter);

    /**
     * 文件读取为输入流
     *
     * @param fileInfoAdapter 文件对象
     * @return 文件流对象
     */
    BaseResult readAsStream(FileInfoAdapter fileInfoAdapter);

    /**
     * 删除图片
     *
     * @param condition 文件信息
     * @return 文件索引信息
     */
    BatchResult deleteOnDisk(Object condition);

}
