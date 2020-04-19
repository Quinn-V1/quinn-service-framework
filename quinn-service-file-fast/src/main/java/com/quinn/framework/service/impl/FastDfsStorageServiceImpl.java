package com.quinn.framework.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.GenerateStorageClient;
import com.quinn.framework.api.file.FileInfoAdapter;
import com.quinn.framework.api.file.StorageService;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.util.StreamUtil;
import com.quinn.util.base.util.StringUtil;
import com.quinn.util.constant.enums.StorageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;

/**
 * FastDfs文件服务存储服务
 * http://212.64.54.213/group0/M00/00/00/rBEAAl6DtJmAQaHNAAAK1s5iPYU78_big.conf
 *
 * @author Qunhua.Liao
 * @since 2020-04-17
 */
public class FastDfsStorageServiceImpl implements StorageService {

    @Value("${com.quinn-service.file.fast.group:group1}")
    private String groupName;

    @Autowired
    @Qualifier("defaultGenerateStorageClient")
    private GenerateStorageClient generateStorageClient;

    @Override
    public BaseResult<FileInfoAdapter> upload(FileInfoAdapter fileInfoAdapter) {
        String realGroup = StringUtil.isEmpty(fileInfoAdapter.getFilePath()) ? groupName : fileInfoAdapter.getFilePath();

        StorePath storePath = generateStorageClient.uploadFile(realGroup, fileInfoAdapter.getInputStream(),
                fileInfoAdapter.getFileSize(), fileInfoAdapter.getFileType());

        fileInfoAdapter.setFilePath(storePath.getGroup());
        fileInfoAdapter.setStorageName(storePath.getPath());

        return BaseResult.success(fileInfoAdapter);
    }

    @Override
    public void download(FileInfoAdapter fileInfoAdapter) {
        String realGroup = StringUtil.isEmpty(fileInfoAdapter.getFilePath()) ? groupName : fileInfoAdapter.getFilePath();

        generateStorageClient.downloadFile(realGroup, fileInfoAdapter.getStoreName(), ins -> {
            StreamUtil.copy(ins, fileInfoAdapter.getOutputStream());
            return null;
        });
    }

    @Override
    public InputStream readAsStream(FileInfoAdapter fileInfoAdapter) {
        String realGroup = StringUtil.isEmpty(fileInfoAdapter.getFilePath()) ? groupName : fileInfoAdapter.getFilePath();
        return generateStorageClient.downloadFile(realGroup, fileInfoAdapter.getStoreName(), ins -> ins);
    }

    @Override
    public boolean exists(FileInfoAdapter fileInfoAdapter) {
        String realGroup = StringUtil.isEmpty(fileInfoAdapter.getFilePath()) ? groupName : fileInfoAdapter.getFilePath();
        return generateStorageClient.queryFileInfo(realGroup, fileInfoAdapter.getStoreName()) != null;
    }

    @Override
    public BaseResult<FileInfoAdapter> delete(FileInfoAdapter fileInfoAdapter) {
        String realGroup = StringUtil.isEmpty(fileInfoAdapter.getFilePath()) ? groupName : fileInfoAdapter.getFilePath();
        generateStorageClient.deleteFile(realGroup, fileInfoAdapter.getStoreName());
        return BaseResult.SUCCESS;
    }

    @Override
    public String storageType() {
        return StorageTypeEnum.FAST_DFS.name();
    }

}
