package org.battler.model.event;

import lombok.RequiredArgsConstructor;
import org.battler.model.sessions.GameSession;

/**
 * Created by romanivanov on 14.09.2022
 */
@RequiredArgsConstructor
public abstract class AbstractGameEvent implements GameEvent {

    private final GameSession gameSession;

    @Override
    public GameSession getGameSession() {
        return gameSession;
    }
}
