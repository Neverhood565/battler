package org.battler.service;

/**
 * Service for handling players actions during game session.
 */
public interface GameService {

    void findGame(String userId);

    void answerQuestion(String userId, String questionId, Boolean correct);

    void leaveGame(String userId);
}
