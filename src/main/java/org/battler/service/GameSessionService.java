package org.battler.service;

/**
 * Created by romanivanov on 28.08.2022
 */
public interface GameSessionService {

    void joinOrCreateSession(String socketSessionId);

    void closeGameSession(String socketSessionId);
}
