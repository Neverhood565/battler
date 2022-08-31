package org.battler.model.sessions;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by romanivanov on 28.08.2022
 */
@Data
public class GameSession {

    private final String id = UUID.randomUUID().toString();
    private final Map<String, PlayerSession> playerSessions = new HashMap<>();
    private Meeting meeting;

    public boolean readyToPlay() {
        return playerSessions.size() == 2;
    }

    public void joinPlayer(PlayerSession playerSession) {
        playerSession.setMeeting(meeting);
        this.playerSessions.put(playerSession.getCurrentSocketSession(), playerSession);
    }
}
