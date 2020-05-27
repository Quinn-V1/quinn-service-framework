package com.quinn.framework.rabbitmq.controller;

import com.quinn.framework.api.MqService;
import com.quinn.framework.controller.AbstractController;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 缓存操作
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
@RestController
@RequestMapping("/framework/mq/*")
@Api(tags = {"0ZY060通用：消息队列"})
public class MqController extends AbstractController {

    @Resource
    @Qualifier("mqService")
    private MqService mqService;

    @PostMapping(value = "send-direct")
    @ApiOperation(value = "发送消息")
    public BaseResult sendDirect(
            @ApiParam(name = "message", value = "消息值", required = true)
            @RequestParam(name = "message") String message,

            @ApiParam(name = "queueName", value = "队列名", required = true)
            @RequestParam(name = "queueName") String queueName
    ) {
        return mqService.sendDirect(message, queueName);
    }

    @PostMapping(value = "send")
    @ApiOperation(value = "发送消息")
    public BaseResult send(
            @ApiParam(name = "message", value = "消息值", required = true)
            @RequestParam(name = "message") String message,

            @ApiParam(name = "exchangeType", value = "交换器类型", required = true)
            @RequestParam(name = "exchangeType") String exchangeType,

            @ApiParam(name = "exchangeName", value = "交换器名称", required = true)
            @RequestParam(name = "exchangeName") String exchangeName,

            @ApiParam(name = "routingKey", value = "路由编码", required = true)
            @RequestParam(name = "routingKey") String routingKey,

            @ApiParam(name = "queueNames", value = "队列名称", required = true)
            @RequestParam(name = "queueNames") String[] queueNames
    ) {
        return mqService.send(message, exchangeType, exchangeName, routingKey, queueNames);
    }

}
