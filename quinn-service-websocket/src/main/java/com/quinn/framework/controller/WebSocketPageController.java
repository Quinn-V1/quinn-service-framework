package com.quinn.framework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 消息页面控制层
 *
 * @author Qunhua.Liao
 * @since 2020-02-20
 */
@Controller
@RequestMapping("/websocket/*")
public class WebSocketPageController {

    @GetMapping("index")
    public String index() {
        return "messageIndex";
    }

}
