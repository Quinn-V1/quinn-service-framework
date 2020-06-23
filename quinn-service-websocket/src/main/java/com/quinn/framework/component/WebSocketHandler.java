package com.quinn.framework.component;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.model.WebSocketMessage;
import org.springframework.util.CollectionUtils;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket消息处理类
 *
 * @author Qunhua.Liao
 * @since 2020-02-21
 */
public class WebSocketHandler {

    /**
     * 会话
     */
    public static Map<String, Map<String, Session>> sessionMapMap = new ConcurrentHashMap<>();

    /**
     * 添加会话
     *
     * @param userKey 用户编码
     * @param address 地址
     * @param session 会话
     */
    public static void addSession(String userKey, String address, Session session) {
        Map<String, Session> sessionMap = sessionMapMap.get(userKey);
        if (sessionMap == null) {
            sessionMap = new ConcurrentHashMap<>(1);
            sessionMapMap.put(userKey, sessionMap);
        }

        sessionMap.put(address, session);
    }

    /**
     * 移除会话
     *
     * @param userKey 用户编码
     * @param address 地址
     */
    public static void removeSession(String userKey, String address) {
        Map<String, Session> sessionMap = sessionMapMap.get(userKey);
        if (sessionMap == null) {
            return;
        }

        sessionMap.remove(address);
        if (CollectionUtils.isEmpty(sessionMap)) {
            sessionMapMap.remove(userKey);
        }
    }

    /**
     * 向内处理
     *
     * @param param   参数
     * @param session 会话
     * @return 处理结果
     */
    public static boolean handleInn(WebSocketMessage param, Session session) {
        return false;
    }

    /**
     * 向外处理
     *
     * @param webSocketMessage 参数
     * @param session          用户名
     * @return 处理结果
     */
    public static boolean handleOut(WebSocketMessage webSocketMessage, Session session) {
        try {
            session.getBasicRemote().sendText(JSONObject.toJSONString(webSocketMessage));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 向外处理
     *
     * @param webSocketMessage 参数
     * @param userKey          用户名
     * @return 处理结果
     */
    public static boolean handleOut(WebSocketMessage webSocketMessage, String userKey) {
        Map<String, Session> sessionMap = sessionMapMap.get(userKey);
        if (sessionMap == null) {
            return false;
        }

        String text = JSONObject.toJSONString(webSocketMessage);
        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
            try {
                entry.getValue().getBasicRemote().sendText(text);
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 广播消息
     *
     * @param webSocketMessage  消息
     */
    public static void boardCast(WebSocketMessage webSocketMessage) {
        String text = JSONObject.toJSONString(webSocketMessage);
        for (Map.Entry<String, Map<String, Session>> mapEntry : sessionMapMap.entrySet()) {
            Map<String, Session> sessionMap = mapEntry.getValue();
            if (sessionMap != null) {
                for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
                    try {
                        entry.getValue().getBasicRemote().sendText(text);
                    } catch (IOException e) {
                        // DO NOTHING
                    }
                }
            }
        }
    }

}
