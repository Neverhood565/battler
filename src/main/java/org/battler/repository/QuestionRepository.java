package org.battler.repository;

import org.battler.model.question.Question;

import java.util.List;

/**
 * Created by romanivanov on 31.08.2022
 */
public interface QuestionRepository {

    List<Question> getNextRandomQuestions(int amount);

    void persistQuestion(Question question);

    List<Question> findAllQuestions();
}
