package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

/**
 * 登录前的准备
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public interface LoginPrevProcessor {

    /**
     * 优先级：越小越优先
     *
     * @return 优先级
     */
    int priority();

    /**
     * 处理逻辑
     *
     * @param tokenInfo 令牌信息
     * @param <T>       权限信息泛型
     * @return 处理结果
     */
    <T extends TokenInfo> BaseResult process(T tokenInfo);

    /**
     * 是否接受这类令牌的增强
     *
     * @param tokenInfo 令牌
     * @param <T>       令牌泛型
     * @return 是否接受
     */
    default <T extends TokenInfo> boolean accept(T tokenInfo) {
        return true;
    }

}
