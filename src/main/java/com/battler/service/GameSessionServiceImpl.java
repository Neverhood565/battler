package com.battler.service;

import com.battler.model.GameSession;
import com.battler.model.User;
import com.battler.repository.entity.GameSessionEntity;
import com.battler.socket.SocketMessageManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by romanivanov on 28.08.2022
 */
@ApplicationScoped
public class GameSessionServiceImpl implements GameSessionService {

    @Inject
    SocketMessageManager socketMessageManager;

    private final List<GameSession> gameSessions = new ArrayList<>();

    @Override
    public void initiateGameSession(final String socketSessionId) {
        if (gameSessions.isEmpty()) {
            
        }
    }

    @Override
    public void closeGameSession(final String socketSessionId) {

    }
}
