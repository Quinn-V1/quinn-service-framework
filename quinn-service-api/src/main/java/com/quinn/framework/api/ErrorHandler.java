package com.quinn.framework.api;

import com.quinn.util.base.api.ClassDivAble;
import com.quinn.util.base.model.BaseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理器
 *
 * @author Qunhua.Liao
 * @since 2020-04-09
 */
public interface ErrorHandler extends ClassDivAble {

    /**
     * 处理异常
     *
     * @param e         异常
     * @param request   请求
     * @param response  响应
     * @return
     */
    BaseResult handleError(Exception e, HttpServletRequest request, HttpServletResponse response);

}
