package org.battler.repository;

import org.battler.model.question.Question;
import org.battler.model.session.QuestionType;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by romanivanov on 31.08.2022
 */
public interface QuestionRepository {

    CompletionStage<List<Question>> getNextRandomQuestions(int amount, QuestionType questionType);

    void persistQuestion(Question question);

    List<Question> findAllQuestions();
}
