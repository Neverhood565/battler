package org.battler.model.event;

import org.battler.model.sessions.GameSession;

/**
 * Created by romanivanov on 10.09.2022
 */
public interface GameEvent {

    GameSession getGameSession();
}
