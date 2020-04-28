package com.quinn.framework.api.strategy;

import java.lang.annotation.*;

/**
 * 策略注册标识
 *
 * @author Qunhua.Liao
 * @since 2020-04-27
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Strategy {

    /**
     * 资源唯一定位符
     *
     * @return 唯一路径或者编码
     */
    String value();

}
