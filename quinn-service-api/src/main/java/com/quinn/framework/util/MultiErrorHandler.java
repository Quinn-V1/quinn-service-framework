package com.quinn.framework.util;

import com.quinn.framework.api.ErrorHandler;
import com.quinn.framework.model.DefaultErrorHandler;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.model.BaseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 异常处理分发器
 *
 * @author Qunhua.Liao
 * @since 2020-04-09
 */
public class MultiErrorHandler {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(MultiErrorHandler.class);

    private static Map<Class, ErrorHandler> errorHandlers = new LinkedHashMap<>();

    private static ErrorHandler defaultHandler = DefaultErrorHandler.getInstance();

    /**
     * 处理错误
     *
     * @param e        错误
     * @param request  请求
     * @param response 响应
     * @return 处理结果
     */
    public static BaseResult handleError(Exception e, HttpServletRequest request, HttpServletResponse response) {
        Class exceptionClass = e.getClass();
        while (exceptionClass != Object.class) {
            ErrorHandler errorHandler = errorHandlers.get(exceptionClass);
            if (errorHandler != null) {
                if (errorHandler != null) {
                    LOGGER.errorError("Error {0}[{1}] handle by {2}", e, e.getClass().getName(), e.getMessage(),
                            errorHandler.getClass().getName());
                    return errorHandler.handleError(e, request, response);
                }
            }
            exceptionClass = exceptionClass.getSuperclass();
        }

        Throwable cause = e.getCause();
        if (cause instanceof Exception) {
            return handleError((Exception) cause, request, response);
        }

        LOGGER.errorError("Error {0}[{1}] handle by default", e, e.getClass().getName(),
                e.getMessage());
        return defaultHandler.handleError(e, request, response);
    }

    /**
     * 添加处理器
     *
     * @param handler
     */
    public static void addHandler(ErrorHandler handler) {
        errorHandlers.put(handler.getDivClass(), handler);
    }

    /**
     * 添加处理器
     *
     * @param values 添加处理器
     */
    public static void addHandlers(Collection<ErrorHandler> values) {
        for (ErrorHandler handler : values) {
            addHandler(handler);
        }
    }
}
