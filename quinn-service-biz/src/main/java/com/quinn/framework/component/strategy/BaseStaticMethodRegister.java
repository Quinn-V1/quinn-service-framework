package com.quinn.framework.component.strategy;

import com.quinn.framework.api.strategy.StaticMethodRegister;
import com.quinn.framework.component.KeyValueMultiService;
import com.quinn.framework.util.Base64Util;
import com.quinn.framework.util.SessionUtil;

/**
 * Api 模块静态方法注册器
 *
 * @author Qunhua.Liao
 * @since 2020-04-28
 */
public class BaseStaticMethodRegister implements StaticMethodRegister {

    @Override
    public Class[] register() {
        return new Class[]{
                SessionUtil.class,
                KeyValueMultiService.class,
                Base64Util.class,
        };
    }

}
