package com.quinn.framework.controller;

import ch.qos.logback.classic.LoggerContext;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.ILoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * 日志级别控制逻辑
 *
 * @author Qunhua.Liao
 * @since 2020-03-21
 */
@RestController
@RequestMapping("/framework/log/*")
@Api(tags = {"0ZZ030框架：日志设置"})
public class LoggerController extends AbstractController {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(LoggerController.class);

    @PostMapping(value = "config")
    @ApiOperation(value = "配置日志级别", notes = "此日志配置将立即生效")
    public BaseResult changeLogLevel(
            @ApiParam(name = "loggerName", value = "日志路径", required = true)
            @RequestParam(name = "loggerName") String loggerName,

            @ApiParam(name = "level", value = "日志级别", required = true)
            @RequestParam(name = "level") LogLevel level
    ) {
        LogbackLoggingSystem logbackLoggingSystem = new LogbackLoggingSystem(this.getClass().getClassLoader());
        logbackLoggingSystem.setLogLevel(loggerName, level);
        LOGGER.info("logger name is {}, log level changed to {}", loggerName, level);

        BaseResult result = BaseResult.build(true);
        result.setMessage("logger name is [" + loggerName + "], log level changed to [" + level + "]");
        return result;
    }

    @GetMapping(value = "query")
    @ApiOperation(value = "查询日志配置")
    public BaseResult queryLogLevel(@RequestParam String loggerName) {
        return BaseResult.success(findLoggerContext().getLogger(loggerName).getEffectiveLevel().toString());
    }

    /**
     * 获取日志上下文
     *
     * @return 日志上下文
     */
    private LoggerContext findLoggerContext() {
        ILoggerFactory factory = StaticLoggerBinder.getSingleton().getLoggerFactory();
        Assert.isInstanceOf(LoggerContext.class, factory,
                String.format("", factory.getClass(), findLocation(factory)));
        return (LoggerContext) factory;
    }

    /**
     * 获取日志位置
     *
     * @param factory 日志工厂
     * @return 日志位置
     */
    private Object findLocation(ILoggerFactory factory) {
        try {
            ProtectionDomain protectionDomain = factory.getClass().getProtectionDomain();
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource != null) {
                return codeSource.getLocation();
            }
        } catch (SecurityException ex) {
        }
        return "unknown location";
    }
}
