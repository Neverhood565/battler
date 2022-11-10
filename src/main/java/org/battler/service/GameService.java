package org.battler.service;

import org.battler.model.UserId;
import org.battler.model.session.QuestionType;

/**
 * Service for handling players actions during game session.
 */
public interface GameService {

    void findGame(UserId user, QuestionType questionsType);

    void answerQuestion(UserId user, String questionId, Boolean correct);

    void leaveGame(UserId user);
}
