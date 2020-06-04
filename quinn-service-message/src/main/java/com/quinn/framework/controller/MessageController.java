package com.quinn.framework.controller;

import com.quinn.framework.model.MessageSendParam;
import com.quinn.framework.service.MessageApiService;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息发送相关操作
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
@RestController
@RequestMapping("/core/message/*")
@Api(tags = {"0ZY040通用：消息发送"})
public class MessageController extends AbstractController {

    @Resource
    @Qualifier("messageApiService")
    private MessageApiService messageApiService;

    @PostMapping(value = "send")
    @ApiOperation(value = "发送消息")
    public BaseResult send(
            @ApiParam(name = "messageSendParam", value = "消息发送参数", required = true)
            @RequestBody MessageSendParam messageSendParam
    ) {
        return messageApiService.send(messageSendParam);
    }

    @PostMapping(value = "preview")
    @ApiOperation(value = "消息预览")
    public BaseResult preview(
            @ApiParam(name = "messageSendParam", value = "消息发送参数", required = true)
            @RequestBody MessageSendParam messageSendParam
    ) {
        return messageApiService.preview(messageSendParam);
    }

    @PostMapping(value = "revoke")
    @ApiOperation(value = "消息撤回")
    public BaseResult revokeBySendRecord(
            @ApiParam(name = "sendRecordIds", value = "消息发送记录ID", required = true)
            @RequestBody Long[] sendRecordIds
    ) {
        return messageApiService.revokeBySendIds(sendRecordIds);
    }

}
