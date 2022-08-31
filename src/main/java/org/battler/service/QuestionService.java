package org.battler.service;

import org.battler.model.question.Question;

import java.util.List;

/**
 * Created by romanivanov on 31.08.2022
 */
public interface QuestionService {

    List<Question> getQuestions();
}
