package org.battler.service;

import org.battler.model.sessions.GameSession;
import org.battler.model.sessions.PlayerSession;
import org.battler.service.meeting.MeetingService;
import org.battler.socket.SocketMessageManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

/**
 * Created by romanivanov on 28.08.2022
 */
@ApplicationScoped
public class GameSessionServiceImpl implements GameSessionService {

    @Inject
    SocketMessageManager socketMessageManager;

    @Inject
    QuestionService questionService;

    @Inject
    MeetingService meetingService;

    private final List<GameSession> gameSessions = new ArrayList<>();

    @Override
    public void joinOrCreateSession(final String socketSessionId) {
        getGameSession().thenAccept(gameSession -> {
            gameSession.joinPlayer(createPlayerSession(socketSessionId));
            if (gameSession.readyToPlay()) {
                gameSession.getPlayerSessions().values().forEach(playerSession -> socketMessageManager.emitMessage(
                        playerSession,
                        playerSession.getCurrentSocketSession()
                ));
            }
        });
    }

    @Override
    public void closeGameSession(final String socketSessionId) {
        gameSessions.clear();
    }

    private CompletionStage<GameSession> getGameSession() {
        if (!this.gameSessions.isEmpty()) {
            CompletableFuture<GameSession> stage = new CompletableFuture<>();
            stage.complete(gameSessions.get(0));
            return stage;
        }
        return createNewGameSession();
    }

    private CompletionStage<GameSession> createNewGameSession() {
        return meetingService.createMeeting().thenApply(meeting -> {
            GameSession gameSession = new GameSession();
            gameSession.setMeeting(meeting);
            gameSessions.add(gameSession);
            return gameSession;
        });
    }

    private PlayerSession createPlayerSession(final String socketSessionId) {
        PlayerSession playerSession = new PlayerSession();
        playerSession.setCurrentSocketSession(socketSessionId);
        playerSession.setQuestions(questionService.getQuestions());
        return playerSession;
    }
}
