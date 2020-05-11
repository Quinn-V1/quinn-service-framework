package com.quinn.framework.annotation;

import java.lang.annotation.*;

/**
 * 测试类入口
 *
 * @author Qunhua.Liao
 * @since 2020-04-27
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestEntry {

    /**
     * 测试类入口
     *
     * @return 测试类入口
     */
    Class value();

}
