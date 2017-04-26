package com.neil.fpdatabase.controller;

import com.alibaba.fastjson.JSONObject;
import com.neil.fpdatabase.fingercore.fingerprint.CachedFingerPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhu on 4/18/2017.
 */
public class FingerPrintIdentityHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FingerPrintIdentityHandler.class);

    private List<WebSocketSession> connectingSessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.connectingSessions.add(session);
    }

    public void sendIdentity(CachedFingerPrint cachedFingerPrint) {
        JSONObject idJSON = new JSONObject()
                .fluentPut("code", cachedFingerPrint.getIdentityCode())
                .fluentPut("identity", cachedFingerPrint.getIdentity());
        LOGGER.info("sending identity:" + idJSON.toJSONString());
        TextMessage idInfo = new TextMessage(idJSON.toJSONString());
        for (WebSocketSession connectingSession : connectingSessions) {
            try {
                if (connectingSession != null && connectingSession.isOpen()) {
                    connectingSession.sendMessage(idInfo);
                }
            } catch (IOException e) {
                LOGGER.error("catch error during sending:", e);
            }
        }
    }

}
