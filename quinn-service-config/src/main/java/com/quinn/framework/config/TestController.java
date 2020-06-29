package com.quinn.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author 测试
 */
@RefreshScope
@RestController
public class TestController {

    @Value("${name}")
    private String name;

    @RequestMapping(value = "/test", method = GET)
    public String test() {
        return name;
    }

}