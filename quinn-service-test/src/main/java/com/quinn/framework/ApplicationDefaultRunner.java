package com.quinn.framework;

import com.quinn.framework.annotation.TestEntry;
import com.quinn.framework.component.BaseConfigInfoReWriter;
import com.quinn.framework.model.SpringApplicationFactory;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;

/**
 * 自定义测试启动类
 *
 * @author Qunhua.Liao
 * @since 2020-04-24
 */
public class ApplicationDefaultRunner extends SpringJUnit4ClassRunner {

    public ApplicationDefaultRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        Properties properties = SpringApplicationFactory.collectProperties(new String[0]);
        BaseConfigInfoReWriter.decryptProperties(properties);

        TestEntry annotation = clazz.getAnnotation(TestEntry.class);
        Class entryClass = annotation == null ? ApplicationDefaultEntry.class : annotation.value();
        SpringApplicationFactory.buildApplication(entryClass, properties);
    }

}
