package org.battler.socket;

import org.battler.service.GameSessionService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
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

    @Inject
    SocketMessageManager socketMessageManager;

    @OnMessage
    public void onMessage(String message, Session session) {
        if (message.equalsIgnoreCase("{\"event\":\"check-meeting\"}")) {
            gameSessionService.joinOrCreateSession(session.getId());
        }
    }

    @OnClose
    public void onClose(Session session) {
        gameSessionService.closeGameSession(session.getId());
        socketMessageManager.removeSession(session);
    }

    @OnOpen
    public void saveSession(Session session) {
        socketMessageManager.saveSession(session);
    }
}
