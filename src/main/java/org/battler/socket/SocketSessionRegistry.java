package org.battler.socket;

import javax.websocket.Session;

/**
 * Registry for storing active websocket sessions.
 */
public interface SocketSessionRegistry {

    void registerSession(Session session, String userId);

    Session getSessionByUserId(String userId);

    String getUserIdBySessionId(String sessionId);

    void removeSession(String sessionId);
}
