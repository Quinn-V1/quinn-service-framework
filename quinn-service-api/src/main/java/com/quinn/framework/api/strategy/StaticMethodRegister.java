package com.quinn.framework.api.strategy;

/**
 * 静态方法注册器：为扩展模块增加入口
 *
 * @author Qunhua.Liao
 * @since 2020-04-27
 */
public interface StaticMethodRegister {

    /**
     * 注册静态类
     *
     * @return 需要注册策略的静态方法类数组
     */
    Class[] register();

}
