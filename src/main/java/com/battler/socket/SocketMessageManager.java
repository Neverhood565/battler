package com.battler.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.codegen.annotations.Nullable;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by romanivanov on 28.08.2022
 */
@Slf4j
@ApplicationScoped
public class SocketMessageManager {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Session> socketSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void saveSession(Session session) {
        socketSessions.put(session.getId(), session);
    }

    @OnClose
    public void removeSession(Session session) {
        socketSessions.remove(session.getId());
    }

    public Session getSessionById(String sessionId) {
        return socketSessions.get(sessionId);
    }

    @SneakyThrows
    public void emitMessage(Object message, String sessionId) {
        Session session = socketSessions.get(sessionId);

        if (session == null) {
            log.error("Session with id {} not found.", sessionId);
            return;
        }
        session.getAsyncRemote().sendObject(
                mapper.writeValueAsString(message),
                result -> {
                    if (!result.isOK()) {
                        log.error("Fail to send message to session {}", sessionId);
                    }
                }
        );
    }
}
