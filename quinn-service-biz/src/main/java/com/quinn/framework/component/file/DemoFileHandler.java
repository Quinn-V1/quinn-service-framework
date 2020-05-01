package com.quinn.framework.component.file;

import com.quinn.util.base.api.FileAdapter;
import com.quinn.framework.api.file.FileHandler;
import com.quinn.framework.api.file.FileInfoAdapter;
import com.quinn.framework.api.file.StorageService;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;

/**
 * 实例文件处理器
 *
 * @author Qunhua.Liao
 * @since 2020-04-18
 */
public class DemoFileHandler implements FileHandler {

    private StorageService storageService;

    public DemoFileHandler(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public BaseResult<FileInfoAdapter> upload(FileAdapter fileAdapter) {
        return storageService.upload(new DemoFileInfoAdapter(fileAdapter));
    }

    @Override
    public BaseResult<FileInfoAdapter> fileInfo(String fileKey) {
        return null;
    }

    @Override
    public boolean exists(FileAdapter fileAdapter) {
        return false;
    }

    @Override
    public BaseResult readAsStream(FileInfoAdapter fileInfoAdapter) {
        return null;
    }

    @Override
    public BatchResult deleteOnDisk(Object condition) {
        return null;
    }
}
