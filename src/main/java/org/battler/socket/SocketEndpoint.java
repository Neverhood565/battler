package org.battler.socket;

import lombok.extern.slf4j.Slf4j;
import org.battler.service.GameService;
import org.battler.socket.dto.request.Request;
import org.battler.socket.dto.responce.ErrorResponse;
import org.battler.urils.JsonUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import static org.battler.socket.dto.request.Request.ANSWER_QUESTION;
import static org.battler.socket.dto.request.Request.FIND_GAME;
import static org.battler.socket.dto.request.Request.LEAVE_GAME;

/**
 * Created by romanivanov on 28.08.2022
 */
@Slf4j
@ApplicationScoped
@ServerEndpoint("/battler")
public class SocketEndpoint {

    @Inject
    GameService gameService;

    @Inject
    SocketSessionRegistry socketSessionRegistry;

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("token") String user) {

        log.info("Incoming socket message. Session ID: {}, message: {}, userId {}", session.getId(), message, user);

        Request request;
        String userId = socketSessionRegistry.getUserIdBySessionId(session.getId());

        try {
            request = JsonUtils.toObject(message, Request.class);
            handleRequest(request, userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.getAsyncRemote().sendObject(new ErrorResponse(e.getMessage()));
        }
    }

    public void handleRequest(Request request, String userId) {
        switch (request.getEvent()) {
            case FIND_GAME:
                gameService.findGame(userId);
                break;
            case ANSWER_QUESTION:
                gameService.answerQuestion(userId, request.getQuestionId(), request.getCorrect());
                break;
            case LEAVE_GAME:
                gameService.leaveGame(userId);
                break;
            default:
                log.error("Unknown event type: {}", request.getEvent());
                break;
        }
    }

    @OnClose
    public void onClose(Session session) {
        String userId = socketSessionRegistry.getUserIdBySessionId(session.getId());
        log.info("Socket session closed. Session ID {}, userid {}", session.getId(), userId);
        socketSessionRegistry.removeSession(session.getId());
    }

    @OnOpen
    public void saveSession(Session session, @PathParam("token") String userId) {
        log.info("Socket session opened. Session ID {}, userid {}", session.getId(), userId);
        //TODO: тут необходимо принимать jwt токен с данными пользователя. Временно просто принимаем ID
        socketSessionRegistry.registerSession(session, session.getId());
    }
}
