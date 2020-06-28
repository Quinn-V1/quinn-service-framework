package com.quinn.framework.handler.exception;

import com.quinn.framework.model.DefaultErrorHandler;
import com.quinn.util.base.model.BaseResult;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 必填参数为空
 *
 * @author Qunhua.Liao
 * @since 2020-06-28
 */
@Component("missingServletRequestParameterExceptionHandler")
public class MissingServletRequestParameterExceptionHandler extends DefaultErrorHandler<DateTimeParseException> {

    private static final Pattern PATTERN = Pattern.compile("(Required )(.*)( parameter ')(.*)(' is not present)");

    @Override
    public void generateMessage(DateTimeParseException e, BaseResult result) {
        Matcher matcher = PATTERN.matcher(e.getMessage());
        if (matcher.find()) {
            result.setMessage("必填参数【" + matcher.group(4) + "】为空，类型【" + matcher.group(2) + "】");
        }
    }

    @Override
    public Class<?> getDivClass() {
        return DateTimeParseException.class;
    }

}
