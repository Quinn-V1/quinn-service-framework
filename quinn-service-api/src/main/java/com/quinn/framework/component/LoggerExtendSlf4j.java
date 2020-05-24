package com.quinn.framework.component;

import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.handler.MultiMessageResolver;
import org.slf4j.Logger;

import java.util.Locale;

/**
 * 扩展至Sl4f的日志
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public class LoggerExtendSlf4j implements LoggerExtend {

    /**
     * 实际日志对象
     */
    private Logger logger;

    LoggerExtendSlf4j(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public void trace(String format, Object... args) {
        if (logger.isTraceEnabled()) {
            String message = MultiMessageResolver.resolve(Locale.getDefault(), format, args);
            logger.trace(message);
        }
    }

    @Override
    public void traceError(String format, Throwable error, Object... args) {
        if (logger.isTraceEnabled()) {
            String message = MultiMessageResolver.resolve(Locale.getDefault(), format, args);
            logger.trace(message, error);
        }
    }

    @Override
    public void debug(String format, Object... args) {
        if (logger.isDebugEnabled()) {
            String message = MultiMessageResolver.resolve(Locale.getDefault(), format, args);
            logger.debug(message);
        }
    }

    @Override
    public void debugError(String format, Throwable error, Object... args) {
        if (logger.isDebugEnabled()) {
            String message = MultiMessageResolver.resolve(Locale.getDefault(), format, args);
            logger.debug(message, error);
        }
    }

    @Override
    public void info(String format, Object... args) {
        if (logger.isInfoEnabled()) {
            String message = MultiMessageResolver.resolve(Locale.getDefault(), format, args);
            logger.info(message);
        }
    }

    @Override
    public void infoError(String format, Throwable error, Object... args) {
        if (logger.isInfoEnabled()) {
            String message = MultiMessageResolver.resolve(Locale.getDefault(), format, args);
            logger.info(message, error);
        }
    }

    @Override
    public void warn(String format, Object... args) {
        if (logger.isWarnEnabled()) {
            String message = MultiMessageResolver.resolve(Locale.getDefault(), format, args);
            logger.warn(message);
        }
    }

    @Override
    public void warnError(String format, Throwable error, Object... args) {
        if (logger.isWarnEnabled()) {
            String message = MultiMessageResolver.resolve(Locale.getDefault(), format, args);
            logger.warn(message, error);
        }
    }

    @Override
    public void error(String format, Object... args) {
        if (logger.isErrorEnabled()) {
            String message = MultiMessageResolver.resolve(Locale.getDefault(), format, args);
            logger.error(message);
        }
    }

    @Override
    public void errorError(String format, Throwable error, Object... args) {
        if (logger.isErrorEnabled()) {
            String message = MultiMessageResolver.resolve(Locale.getDefault(), format, args);
            logger.error(message, error);
        }
    }

}
