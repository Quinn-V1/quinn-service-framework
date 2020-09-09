package com.quinn.framework.component.file;

import com.quinn.util.base.api.FileAdapter;
import com.quinn.framework.api.file.FileInfoAdapter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 示例文件消息适配器
 *
 * @author Qunhua.Liao
 * @since 2020-04-18
 */
public class DemoFileInfoAdapter implements FileInfoAdapter {

    private FileAdapter fileAdapter;

    public DemoFileInfoAdapter(FileAdapter fileAdapter) {
        this.fileAdapter = fileAdapter;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getFileKey() {
        return getFullPath();
    }

    @Override
    public String getFilePath() {
        return this.fileAdapter.getFilePath();
    }

    @Override
    public void setFilePath(String filePath) {

    }

    @Override
    public String getStoreName() {
        return this.fileAdapter.getFilename();
    }

    @Override
    public String getFullPath() {
        return this.fileAdapter.getFilePath();
    }

    @Override
    public void setFullPath(String fullPath) {

    }

    @Override
    public String[] getPathStrategies() {
        return null;
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public InputStream getInputStream() {
        return null;
    }

    @Override
    public void setInputStream(InputStream inputStream) {

    }

    @Override
    public <T> T realFileInfo() {
        return null;
    }

    @Override
    public long getFileSize() {
        return 0;
    }

    @Override
    public String getFileType() {
        return null;
    }

    @Override
    public void setStorageName(String fullPath) {

    }

    @Override
    public String getActualName() {
        return null;
    }

    @Override
    public void setActualName(String actualName) {

    }

}
