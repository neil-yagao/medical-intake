package com.neil.fpdatabase.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.neil.fpdatabase.fingercore.FingerPrintRegister;
import com.neil.fpdatabase.fingercore.zk.ZKFingerSensorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by nhu on 4/6/2017.
 * push the result of Finger Print scan to front
 */
public class FingerPrintRegisterHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FingerPrintRegisterHandler.class);

    private WebSocketSession connectingSession;

    @Autowired
    private ZKFingerSensorFactory zkFingerSensorFactory;
    @Value("${file.location}")
    private String fileLocation;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.connectingSession = session;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        FingerPrintRegister register = zkFingerSensorFactory.getPrintRegister();
        String identityInfoJSON = message.getPayload();
        JSONObject identity = JSON.parseObject(identityInfoJSON);
        if (identity.getString("op").equals("reg")) {
            register.setIdentity(identity.getString("code"),
                    identity.getString("identity"), identity.getString("head"));
            renameFingerPrintPics((List<String>) identity.get("imgs"),
                    identity.getString("identity"),
                    identity.getString("code"));
            register.doRegister();

        } else if (identity.getString("op").equals("clr")) {
            register.resetRegisterInfo();
        }
    }

    public void sendImage(String imgLoc) throws IOException {
        WebSocketMessage<String> imgMessage = new TextMessage(imgLoc);
        connectingSession.sendMessage(imgMessage);
    }

    private void renameFingerPrintPics(List<String> imgs, String identity, String identityCode) throws IOException {
        String fullPath = fileLocation + "/img/" + identity + "/";
        int i = 1;
        for (String img : imgs) {
            File file = new File(fileLocation + "/" + img);
            File toFile = new File(fullPath + "/" + identityCode + "-" + i + ".png");
            if (toFile.exists()) {
                toFile.delete();
                LOGGER.warn("file already existed, delete origin");
            }
            boolean success = file.renameTo(toFile);
            if (!success) {
                LOGGER.error("rename failed, unknown reason!");
            }
            i++;
        }
    }
}
