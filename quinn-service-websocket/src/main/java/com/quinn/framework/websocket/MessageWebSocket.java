package com.quinn.framework.websocket;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.component.WebSocketHandler;
import com.quinn.framework.model.WebSocketMessage;
import com.quinn.framework.util.enums.WebSocketCommandOuter;
import com.quinn.util.base.BaseUtil;
import com.quinn.util.constant.enums.MessageLevelEnum;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.net.InetSocketAddress;

/**
 * 消息前端交互WebSocket
 *
 * @author Qunhua.Liao
 * @since 2020-02-20
 */
@ServerEndpoint("/websocket/{userKey}")
@Component
public class MessageWebSocket {

    /**
     * 用户编码（确定不同的连接是不同的当前对象）
     */
    private String userKey;

    /**
     * 客户端地址（确定不同的连接是不同的当前对象）
     */
    private String address;

    @OnOpen
    public void onOpen(Session session, @PathParam("userKey") String userKey) {
        this.userKey = userKey;
        this.address = getRemoteAddress(session);
        WebSocketHandler.addSession(this.userKey, this.address, session);
    }

    @OnClose
    public void onClose() {
        WebSocketHandler.removeSession(this.userKey, this.address);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        WebSocketMessage param = JSONObject.parseObject(message, WebSocketMessage.class);
        WebSocketHandler.handleInn(param, session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        if (!session.isOpen()) {
            WebSocketHandler.removeSession(this.userKey, this.address);
        } else {
            WebSocketHandler.handleOut(new WebSocketMessage(WebSocketCommandOuter.MESSAGE.name(),
                    MessageLevelEnum.ERROR.status, error.getMessage()), session);
        }
    }

    /**
     * 获取远程IP地址
     *
     * @param session 会话
     * @return IP地址
     */
    private static String getRemoteAddress(Session session) {
        RemoteEndpoint.Async async = session.getAsyncRemote();
        InetSocketAddress addr =
                (InetSocketAddress) BaseUtil.getFieldInstance(async, "base#socketWrapper#socket#sc#remoteAddress");
        if (addr == null) {
            addr = (InetSocketAddress)
                    BaseUtil.getFieldInstance(async, "base#sos#socketWrapper#socket#sc#remoteAddress");
        }
        return addr == null ? "DEFAULT" : addr.toString();
    }

}
