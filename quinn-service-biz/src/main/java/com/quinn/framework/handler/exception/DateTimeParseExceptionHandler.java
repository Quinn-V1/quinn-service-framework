package com.quinn.framework.handler.exception;

import com.quinn.framework.model.DefaultErrorHandler;
import com.quinn.util.base.model.BaseResult;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期格式不匹配异常
 *
 * @author Qunhua.Liao
 * @since 2020-04-29
 */
@Service("dateTimeParseExceptionHandler")
public class DateTimeParseExceptionHandler extends DefaultErrorHandler {

    private static final Pattern PATTERN = Pattern.compile("(Text ')(.*)(' could not be parsed at index )(\\d*)");

    @Override
    public void generateMessage(Exception e, BaseResult result) {
        Matcher matcher = PATTERN.matcher(e.getMessage());
        if (matcher.find()) {
            result.setMessage("日期【" + matcher.group(2) + "】格式错误，位置【" + matcher.group(4) + "】");
        }
    }

    @Override
    public Class<?> getDivClass() {
        return DateTimeParseException.class;
    }

}
