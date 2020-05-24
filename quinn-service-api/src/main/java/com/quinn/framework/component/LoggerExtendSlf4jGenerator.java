package com.quinn.framework.component;

import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.api.LoggerGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 扩展至Sl4f的日志生成器
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public class LoggerExtendSlf4jGenerator implements LoggerGenerator {

    @Override
    public LoggerExtend generate(String logName) {
        Logger logger = LoggerFactory.getLogger(logName);
        return new LoggerExtendSlf4j(logger);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

}
