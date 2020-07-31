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

    /**
     * 样式-不可为空
     */
    private static final Pattern PATTERN_NOT_NULL = Pattern.compile("(Column ')(.*)(' cannot be null)");

    /**
     * 样式
     */
    private static final Pattern PATTERN_STYLE_NOT_MATCH =
            Pattern.compile("(Data truncation: Incorrect )(.*)( value: ')(.*)(' for column ')(.*)(' at row)");


    @Override
    public void generateMessage(DataIntegrityViolationException e, BaseResult result) {
        Matcher matcher = PATTERN_NOT_NULL.matcher(e.getMessage());
        if (matcher.find()) {
            result.setMessage("字段" + matcher.group(2) + "不可为空");
            return;
        }

        matcher = PATTERN_STYLE_NOT_MATCH.matcher(e.getMessage());
        if (matcher.find()) {
            result.setMessage(("数据【" + matcher.group(4) + "】（" + matcher.group(2) + "）格式错误-或超出有效区间，列名【"
                    + matcher.group(6) + "】"));
        }
    }

    @Override
    public Class<?> getDivClass() {
        return DataIntegrityViolationException.class;
    }

}
