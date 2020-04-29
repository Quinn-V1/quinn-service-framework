package com.quinn.framework.handler;

import com.quinn.framework.model.DefaultErrorHandler;
import com.quinn.util.base.model.BaseResult;
import org.springframework.dao.DuplicateKeyException;
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
@Component("duplicateKeyExceptionHandler")
public class DuplicateKeyExceptionHandler extends DefaultErrorHandler {

    private static final Pattern PATTERN = Pattern.compile("(Duplicate entry ')(.*)(' for key ')(.*)(')");

    @Override
    public void generateMessage(Exception e, BaseResult result) {
        Matcher matcher = PATTERN.matcher(e.getMessage());
        if (matcher.find()) {
            result.setMessage("索引" + matcher.group(4) + "【" + matcher.group(2) + "】重复");
        }
    }

    @Override
    public Class<?> getDivClass() {
        return DuplicateKeyException.class;
    }

}
