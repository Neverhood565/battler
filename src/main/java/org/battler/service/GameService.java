package org.battler.service;

import org.battler.model.UserId;

/**
 * Service for handling players actions during game session.
 */
public interface GameService {

    void findGame(UserId user);

    void answerQuestion(UserId user, String questionId, Boolean correct);

    void leaveGame(UserId user);
}
