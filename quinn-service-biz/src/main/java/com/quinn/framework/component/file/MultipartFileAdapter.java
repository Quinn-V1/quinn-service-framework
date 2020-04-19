package com.quinn.framework.component.file;

import com.quinn.framework.api.file.FileAdapter;
import com.quinn.util.base.util.StringUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * MultipartFile 文件适配器
 *
 * @author Qunhua.Liao
 * @since 2020-04-04
 */
public class MultipartFileAdapter implements FileAdapter<MultipartFile> {

    /**
     * 实际文件
     */
    private MultipartFile file;

    /**
     * 存储路径
     */
    private String filePath;

    public MultipartFileAdapter(MultipartFile file) {
        this.file = file;
    }

    public MultipartFileAdapter(MultipartFile file, String filePath) {
        this.file = file;
        this.filePath = filePath;
    }

    @Override
    public byte[] getBytes() {
        try {
            return file.getBytes();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String getFilename() {
        return file.getOriginalFilename();
    }

    @Override
    public Long getSize() {
        byte[] bytes = getBytes();
        return bytes == null ? 0 : bytes.length * 1L;
    }

    @Override
    public String getFileType() {
        String fileName = getFilename();
        if (StringUtil.isEmpty(fileName)) {
            return null;
        }

        return StringUtil.getFileTypeByName(fileName);
    }

    @Override
    public InputStream getInputStream() {
        try {
            return file.getInputStream();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public MultipartFile realObject() {
        return file;
    }

}
