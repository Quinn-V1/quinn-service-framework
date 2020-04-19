package com.quinn.framework.controller;

import com.quinn.framework.handler.MultiErrorHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 业务跳转层基础类
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public abstract class AbstractController {

    @ExceptionHandler
    public void handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        MultiErrorHandler.handleError(ex, request, response);
    }

}
