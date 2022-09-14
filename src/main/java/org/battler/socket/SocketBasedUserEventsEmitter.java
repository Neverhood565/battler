package org.battler.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.battler.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

/**
 * Created by romanivanov on 28.08.2022
 */
@Slf4j
@ApplicationScoped
public class SocketBasedUserEventsEmitter implements UserEventsEmitter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    SocketSessionRegistry sessionRegistry;

    @SneakyThrows
    public void emitMessage(Object message, User user) {

        Session session = sessionRegistry.getSessionByUserId(user.getId());

        if (session == null) {
            log.error("Session is null.");
            return;
        }
        String messageAsString = mapper.writeValueAsString(message);
        session.getAsyncRemote().sendObject(
                messageAsString,
                result -> {
                    if (!result.isOK()) {
                        log.error("Fail to send message to session {}", session.getId());
                    } else {
                        log.info("Message sent to user. Userid {}, message {}", user.getId(), messageAsString);
                    }
                }
        );
    }
}
