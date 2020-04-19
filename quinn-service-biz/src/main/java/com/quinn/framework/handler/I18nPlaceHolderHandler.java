package com.quinn.framework.handler;

import com.quinn.util.base.handler.DefaultPlaceholderHandler;
import com.quinn.util.base.util.I18nUtil;
import com.quinn.util.constant.StringConstant;

import java.util.Map;

/**
 * 有国际化处理的占位符处理器
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
public class I18nPlaceHolderHandler extends DefaultPlaceholderHandler {

    public I18nPlaceHolderHandler() {
        this(StringConstant.CHAR_OPEN_BRACE, StringConstant.CHAR_CLOSE_BRACE);
    }

    public I18nPlaceHolderHandler(String placeholderPrefix, String placeholderSuffix) {
        super(placeholderPrefix, placeholderSuffix);
    }

    @Override
    public String parseStringWithMap(String format, Map<String, Object> properties) {
        format = I18nUtil.tryGetI18n(format, DefaultMessageResolver.getProperties());
        return super.parseStringWithMap(format, properties);
    }

    @Override
    public String parseStringWithArray(String format, Object... args) {
        format = I18nUtil.tryGetI18n(format, DefaultMessageResolver.getProperties());
        return super.parseStringWithArray(format, args);
    }

}
