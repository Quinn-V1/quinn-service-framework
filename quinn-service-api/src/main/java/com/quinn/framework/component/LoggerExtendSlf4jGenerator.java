package com.quinn.framework.component;

import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.api.LoggerGenerator;
import com.quinn.util.base.api.PlaceholderHandler;
import com.quinn.util.base.handler.DefaultPlaceholderHandler;
import com.quinn.util.constant.StringConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 扩展至Sl4f的日志生成器
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public class LoggerExtendSlf4jGenerator implements LoggerGenerator {

    /**
     * 消息解析器
     */
    private PlaceholderHandler placeholderHandler;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public PlaceholderHandler ofPlaceholderHandler(PlaceholderHandler placeholderHandler) {
        if (placeholderHandler == null && this.placeholderHandler == null) {
            this.placeholderHandler = new DefaultPlaceholderHandler(
                    StringConstant.CHAR_OPEN_BRACE, StringConstant.CHAR_CLOSE_BRACE);
        } else if (placeholderHandler != null) {
            this.placeholderHandler = placeholderHandler;
        }
        return this.placeholderHandler;
    }

    @Override
    public LoggerExtend generator(String logName) {
        Logger logger = LoggerFactory.getLogger(logName);
        return new LoggerExtendSlf4j(logger, placeholderHandler);
    }

}
