package com.battler.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by romanivanov on 28.08.2022
 */
@Data
public class GameSession {

    private final String id = UUID.randomUUID().toString();
    private final List<String> socketSessions = new ArrayList<>();
    private String meetingId;
    private String roomName;
}
