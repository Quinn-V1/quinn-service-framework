package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

/**
 * 登录后的处理
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public interface LoginPostProcessor<A extends AuthInfo, T extends TokenInfo> {

    /**
     * 优先级：越小越优先
     *
     * @return 优先级
     */
    int priority();

    /**
     * 处理逻辑
     *
     * @param authInfo 权限信息
     * @param token    令牌信息
     * @param <A>      权限信息泛型
     * @param <T>      令牌信息泛型
     * @return 处理结果
     */
    BaseResult process(A authInfo, T token, Exception exception);

    /**
     * 是否接受这类令牌的增强
     *
     * @param tokenInfo 令牌
     * @param <T>       令牌泛型
     * @return 是否接受
     */
    default boolean accept(T tokenInfo) {
        return true;
    }

}
