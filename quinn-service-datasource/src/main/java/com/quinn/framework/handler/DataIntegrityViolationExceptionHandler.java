package com.quinn.framework.handler;

import com.quinn.framework.model.DefaultErrorHandler;
import com.quinn.util.base.model.BaseResult;
import org.springframework.dao.DuplicateKeyException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DuplicateKeyException 异常处理
 * Duplicate entry 'string' for key 'BPM_MODEL_INFO_UX1'
 *
 * @author Qunhua.Liao
 * @since 2020-04-09
 */
public class DataIntegrityViolationExceptionHandler extends DefaultErrorHandler {

    private static final Pattern pattern = Pattern.compile("(Column ')(.*)(' cannot be null)");

    @Override
    public void generateMessage(Exception e, BaseResult result) {
        Matcher matcher = pattern.matcher(e.getMessage());
        if (matcher.find()) {
            result.setMessage("字段" + matcher.group(4) + "不可为空重复");
        }
    }

    @Override
    public Class<?> getDivClass() {
        return DataIntegrityViolationExceptionHandler.class;
    }

}
