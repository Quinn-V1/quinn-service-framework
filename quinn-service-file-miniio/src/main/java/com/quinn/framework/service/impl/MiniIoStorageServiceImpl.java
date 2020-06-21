package com.quinn.framework.service.impl;

import com.quinn.framework.api.file.FileInfoAdapter;
import com.quinn.framework.api.file.StorageService;
import com.quinn.util.base.StreamUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.exception.file.FileOperationException;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.MimeMapper;
import com.quinn.util.constant.enums.StorageTypeEnum;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;

public class MiniIoStorageServiceImpl implements StorageService {

    @Value("${bjqh.framework.file.minio.url:}")
    private String url;

    @Value("${bjqh.framework.file.minio.name:}")
    private String name;

    @Value("${bjqh.framework.file.minio.pass:}")
    private String pass;

    @Value("${bjqh.framework.file.minio.bucket-name:}")
    private String defaultBucketName;

    @Override
    public String storageType() {
        return StorageTypeEnum.MINI_IO.name();
    }

    @Override
    public BaseResult<FileInfoAdapter> upload(FileInfoAdapter appFileDto) {
        InputStream inputStream = appFileDto.getInputStream();
        if (inputStream == null) {
            return null;
        }

        BaseResult<FileInfoAdapter> result = BaseResult.success(appFileDto);
        String fullPath = appFileDto.getFullPath();
        String bucketName = StringUtil.isEmpty(fullPath) ? defaultBucketName : fullPath;

        try {
            MinioClient minioClient = new MinioClient(url, name, pass);
            minioClient.putObject(bucketName, appFileDto.getFileKey(), inputStream, contentType(appFileDto.getFileType()));
            String filePath = minioClient.getObjectUrl(bucketName, appFileDto.getFileKey());
            appFileDto.setFilePath(filePath);
        } catch (Exception e) {
            throw new FileOperationException();
        }

        return result;
    }

    @Override
    public void download(FileInfoAdapter appFileDto) {
        String fullPath = appFileDto.getFullPath();
        String bucketName = StringUtil.isEmpty(fullPath) ? defaultBucketName : fullPath;

        try {
            MinioClient minioClient = new MinioClient(url, name, pass);
            StreamUtil.copy(minioClient.getObject(bucketName, appFileDto.getFileKey()), appFileDto.getOutputStream());
        } catch (Exception e) {
            throw new FileOperationException();
        }
    }

    @Override
    public BaseResult<FileInfoAdapter> delete(FileInfoAdapter appFileDto) {
        BaseResult<FileInfoAdapter> result = BaseResult.success(appFileDto);

        String fullPath = appFileDto.getFullPath();
        String bucketName = StringUtil.isEmpty(fullPath) ? defaultBucketName : fullPath;

        try {
            MinioClient minioClient = new MinioClient(url, name, pass);
            minioClient.removeObject(bucketName, appFileDto.getFileKey());
        } catch (Exception e) {
            throw new FileOperationException();
        }
        return result;
    }

    @Override
    public boolean exists(FileInfoAdapter appFileDto) {
        String fullPath = appFileDto.getFullPath();
        String bucketName = StringUtil.isEmpty(fullPath) ? defaultBucketName : fullPath;

        try {
            MinioClient minioClient = new MinioClient(url, name, pass);
            return minioClient.getObject(bucketName, appFileDto.getFileKey()) != null;
        } catch (Exception e) {
            throw new FileOperationException();
        }
    }

    @Override
    public InputStream readAsStream(FileInfoAdapter appFileDto) {
        String fullPath = appFileDto.getFullPath();
        String bucketName = StringUtil.isEmpty(fullPath) ? defaultBucketName : fullPath;

        try {
            MinioClient minioClient = new MinioClient(url, name, pass);
            return minioClient.getObject(bucketName, appFileDto.getFileKey());
        } catch (Exception e) {
            throw new FileOperationException();
        }
    }

    public static final String contentType(String docSuffix) {
        String mime = MimeMapper.getMimeType(docSuffix);
        if (mime == null) {
            mime = MimeMapper.DEFAULT_MIME_TYPE;
        }
        return mime;
    }

}