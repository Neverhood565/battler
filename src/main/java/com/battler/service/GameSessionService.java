package com.battler.service;

import com.battler.model.GameSession;

/**
 * Created by romanivanov on 28.08.2022
 */
public interface GameSessionService {

    void initiateGameSession(String socketSessionId);

    void closeGameSession(String socketSessionId);
}
