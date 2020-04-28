package com.quinn.framework.component;

import com.quinn.framework.api.strategy.StaticMethodRegister;
import com.quinn.framework.util.SessionUtil;

/**
 * Api 模块静态方法注册器
 *
 * @author Qunhua.Liao
 * @since 2020-04-28
 */
public class ApiStaticMethodRegister implements StaticMethodRegister {

    @Override
    public Class[] register() {
        return new Class[]{
                SessionUtil.class
        };
    }

}
