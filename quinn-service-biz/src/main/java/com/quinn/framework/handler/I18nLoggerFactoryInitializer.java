package com.quinn.framework.handler;

import com.quinn.util.base.api.LoggerGenerator;
import com.quinn.util.base.handler.DefaultLoggerFactoryInitializer;

/**
 * 国际化日志工厂
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
public class I18nLoggerFactoryInitializer extends DefaultLoggerFactoryInitializer {

    @Override
    public void placeholderHandlerOf(LoggerGenerator loggerGenerator) {
        loggerGenerator.ofPlaceholderHandler(new I18nPlaceHolderHandler());
    }

}
