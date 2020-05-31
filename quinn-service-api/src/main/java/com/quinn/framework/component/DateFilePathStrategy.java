package com.quinn.framework.component;

import com.quinn.framework.api.file.FileInfoAdapter;
import com.quinn.framework.api.file.FilePathStrategy;
import com.quinn.util.base.FileUtil;
import com.quinn.util.constant.DateConstant;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 日期修饰策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-05
 */
public class DateFilePathStrategy implements FilePathStrategy {

    @Override
    public String name() {
        return DateFilePathStrategy.class.getSimpleName();
    }

    @Override
    public void decoratePath(FileInfoAdapter fileInfoVO) {
        String realPath = FileUtil.appendFilePath(fileInfoVO.getFilePath(), DateTimeFormatter
                        .ofPattern(DateConstant.DATE_PATTEN_YYYY_MM_DD_NO_SEPARATOR)
                        .format(LocalDate.now()), File.separator);
        fileInfoVO.setFilePath(realPath);
    }

}
