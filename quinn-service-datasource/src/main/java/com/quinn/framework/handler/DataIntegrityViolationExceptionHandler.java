package com.quinn.framework.handler;

import com.quinn.framework.model.DefaultErrorHandler;
import com.quinn.util.base.model.BaseResult;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DuplicateKeyException 异常处理
 * Duplicate entry 'string' for key 'BPM_MODEL_INFO_UX1'
 *
 * @author Qunhua.Liao
 * @since 2020-04-09
 */
@Component("dataIntegrityViolationExceptionHandler")
public class DataIntegrityViolationExceptionHandler extends DefaultErrorHandler<DataIntegrityViolationException> {

    private static final Pattern PATTERN = Pattern.compile("(Column ')(.*)(' cannot be null)");

    @Override
    public void generateMessage(DataIntegrityViolationException e, BaseResult result) {
        Matcher matcher = PATTERN.matcher(e.getMessage());
        if (matcher.find()) {
            result.setMessage("字段" + matcher.group(2) + "不可为空");
        }
    }

    @Override
    public Class<?> getDivClass() {
        return DataIntegrityViolationException.class;
    }

}
