package com.battler.socket;

import com.battler.service.GameSessionService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by romanivanov on 28.08.2022
 */
@Slf4j
@ApplicationScoped
@ServerEndpoint("/battler")
public class SocketEndpoint {

    @Inject
    GameSessionService gameSessionService;

    @OnMessage
    public void onMessage(String message, Session session) {
        if (message.equalsIgnoreCase("check-meeting")) {
            gameSessionService.initiateGameSession(session.getId());
        }
    }

    @OnClose
    public void onClose(Session session) {
        gameSessionService.closeGameSession(session.getId());
    }
}
