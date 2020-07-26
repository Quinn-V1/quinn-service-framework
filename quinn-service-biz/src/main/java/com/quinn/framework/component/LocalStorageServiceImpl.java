package com.quinn.framework.component;

import com.quinn.framework.api.file.FileInfoAdapter;
import com.quinn.framework.api.file.StorageService;
import com.quinn.framework.util.FilePathStrategyUtil;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.FileUtil;
import com.quinn.util.base.StreamUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.enums.CommonMessageEnum;
import com.quinn.util.constant.enums.StorageTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * 本地文件上传下载类
 *
 * @author Qunhua.Liao
 * @since 2020-04-03
 */
public class LocalStorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalStorageServiceImpl.class);

    @Value("${com.quinn-service.file-upload-base-path:upload/}")
    private String basePath;

    @Override
    public BaseResult<FileInfoAdapter> upload(FileInfoAdapter fileInfoAdapter) {
        InputStream in = fileInfoAdapter.getInputStream();
        String fullPath = fillFilePath(fileInfoAdapter);

        // 判断参数是否完整
        if (StringUtils.isEmpty(fullPath) || in == null) {
            return BaseResult.build(false)
                    .buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.key(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "fullPath or inputStream")
                    .result();
        }


        // 判断文件是否存在--存在则删除
        File file = new File(fullPath);
        if (file.exists()) {
            if (file.isFile()) {
                if (!file.delete()) {
                    return BaseResult.build(false)
                            .buildMessage(CommonMessageEnum.FILE_DELETE_FAIL.key(), 1, 0)
                            .addParam(CommonMessageEnum.FILE_DELETE_FAIL.paramNames[0], fullPath)
                            .result();
                }
            } else {
                return BaseResult.build(false)
                        .buildMessage(CommonMessageEnum.FILE_OCCUPIED_BY_DIRECTORY.key(), 1, 0)
                        .addParam(CommonMessageEnum.FILE_OCCUPIED_BY_DIRECTORY.paramNames[0], fullPath)
                        .result();
            }
        }

        // 判断文件夹是否存在--不存在则创建
        String filePath = fileInfoAdapter.getFilePath();
        File dir = new File(filePath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return BaseResult.build(false)
                        .buildMessage(CommonMessageEnum.FILE_DIRECTORY_CREATE_FAIL.key(), 1, 0)
                        .addParam(CommonMessageEnum.FILE_DIRECTORY_CREATE_FAIL.paramNames[0], filePath)
                        .result();
            }
        } else if (dir.isFile()) {
            return BaseResult.build(false)
                    .buildMessage(CommonMessageEnum.FILE_DIRECTORY_OCCUPIED_BY_FILE.key(), 1, 0)
                    .addParam(CommonMessageEnum.FILE_DIRECTORY_OCCUPIED_BY_FILE.paramNames[0], filePath)
                    .result();
        }

        // 文件写入
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            StreamUtils.copy(in, os);
            fileInfoAdapter.setFullPath(fullPath);
        } catch (IOException e) {
            LOGGER.error("Error occurs when upload File", e);
            return BaseResult.build(false)
                    .buildMessage(CommonMessageEnum.FILE_STREAM_OPERATION_FAIL.key(), 1, 0)
                    .addParam(CommonMessageEnum.FILE_STREAM_OPERATION_FAIL.paramNames[0], fullPath)
                    .result();
        } finally {
            StreamUtil.closeQuietly(in, os);
        }

        return BaseResult.SUCCESS;
    }

    @Override
    public BaseResult<FileInfoAdapter> delete(FileInfoAdapter fileInfoAdapter) {
        File file = new File(fileInfoAdapter.getFullPath());
        if (file.exists()) {
            FileUtil.deleteDir(file);
        }
        return BaseResult.SUCCESS;
    }

    @Override
    public boolean exists(FileInfoAdapter fileInfoAdapter) {
        return new File(fileInfoAdapter.getFullPath()).exists();
    }

    @Override
    public InputStream readAsStream(FileInfoAdapter fileInfoAdapter) {
        try {
            return new FileInputStream(fileInfoAdapter.getFullPath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error occurs when read file, not found", e);
        }
    }

    @Override
    public void download(FileInfoAdapter fileInfoAdapter) {
        InputStream in = null;
        OutputStream os = fileInfoAdapter.getOutputStream();
        try {
            in = new FileInputStream(fileInfoAdapter.getFullPath());
            StreamUtils.copy(in, os);
        } catch (IOException e) {
            LOGGER.error("Error occurs when download File", e);
        } finally {
            StreamUtil.closeQuietly(in, os);
        }
    }

    @Override
    public String storageType() {
        return StorageTypeEnum.LOCAL.name();
    }

    /**
     * 补充全路径
     *
     * @param fileInfoAdapter 文件信息
     */
    private String fillFilePath(FileInfoAdapter fileInfoAdapter) {
        FilePathStrategyUtil.decoratePath(fileInfoAdapter);

        String fullPath = fileInfoAdapter.getFullPath();
        if (StringUtil.isNotEmpty(fullPath)) {
            return fullPath;
        }

        fullPath = FileUtil.appendFilePath(basePath, fileInfoAdapter.getFilePath());
        fileInfoAdapter.setFilePath(fullPath);

        fullPath = FileUtil.appendFilePath(fullPath, fileInfoAdapter.getStoreName());
        fileInfoAdapter.setFullPath(fullPath);

        return fullPath;
    }

}
