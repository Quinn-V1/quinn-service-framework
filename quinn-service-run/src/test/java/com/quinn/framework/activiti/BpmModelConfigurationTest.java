package com.quinn.framework.activiti;

import com.quinn.framework.ApplicationDefaultEntry;
import com.quinn.framework.ApplicationDefaultRunner;
import org.activiti.engine.ProcessEngine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ApplicationDefaultEntry.class})
@RunWith(ApplicationDefaultRunner.class)
public class BpmModelConfigurationTest {

    @Resource
    private ProcessEngine processEngine;

    @Test
    public void testProcessEngine() {
        Assert.assertNotNull(processEngine);
    }
}
