package com.quinn.framework.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quinn.framework.api.ErrorHandler;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.handler.MultiMessageResolver;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.StringConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 默认错误处理器
 *
 * @author Qunhua.Liao
 * @since 2020-04-09
 */
public class DefaultErrorHandler<T extends Exception> implements ErrorHandler<T> {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(DefaultErrorHandler.class);

    private static final ObjectMapper objectMapper = ObjectMapperFactory.defaultObjectMapper();

    private static DefaultErrorHandler instance = new DefaultErrorHandler();

    public DefaultErrorHandler() {
    }

    public static DefaultErrorHandler getInstance() {
        return instance;
    }

    @Override
    public Class getDivClass() {
        return Throwable.class;
    }

    @Override
    public BaseResult handleError(T e, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = BaseResult.build(false);
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out;

        if (!response.isCommitted()) {
            try {
                out = response.getWriter();
                result.setSuccess(false);

                generateMessage(e, result);

                String msg = MultiMessageResolver.resolveMessageProp(SessionUtil.getLocale(), result.getMessageProp());
                if (StringUtil.isEmpty(msg)) {
                    msg = result.getMessage();
                    if (StringUtil.isEmpty(msg)) {
                        msg = generateMessageStatic(e);
                    }
                }

                objectMapper.writeValue(out, result.ofMessage(msg));

                out.flush();
            } catch (Exception e1) {
                LOGGER.error("Error occurs When write error info {0}[{1}]", e1, true,
                        e.getClass().getName(), e.getMessage());
            }
        }
        return result;
    }

    /**
     * 生成消息
     *
     * @param e      错误
     * @param result 结果
     */
    public void generateMessage(T e, BaseResult result) {
        result.setMessage(generateMessageStatic(e));
    }

    /**
     * 生成消息
     *
     * @param e 错误
     * @return 简单消息
     */
    public static String generateMessageStatic(Exception e) {
        return e.getClass().getName() + StringConstant.CHAR_COLON + e.getMessage();
    }

}
