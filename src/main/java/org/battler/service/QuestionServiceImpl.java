package org.battler.service;

import org.battler.model.question.Answer;
import org.battler.model.question.Question;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by romanivanov on 31.08.2022
 */
@ApplicationScoped
public class QuestionServiceImpl implements QuestionService {

    @Override
    public List<Question> getQuestions() {
        return getStubQuestions();
    }

    private List<Question> getStubQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(Question.builder()
                              .id(UUID.randomUUID().toString())
                              .title("А как какать?")
                              .correctAnswer(Answer.builder()
                                                   .id(UUID.randomUUID().toString())
                                                   .title("Это вертолет")
                                                   .build()).build());
        questions.add(Question.builder()
                              .id(UUID.randomUUID().toString())
                              .title("В чем разница между JS и Java?")
                              .correctAnswer(Answer.builder()
                                                   .id(UUID.randomUUID().toString())
                                                   .title("Java - для натуралов")
                                                   .build()).build());
        questions.add(Question.builder()
                              .id(UUID.randomUUID().toString())
                              .title("Где джокер?")
                              .correctAnswer(Answer.builder()
                                                   .id(UUID.randomUUID().toString())
                                                   .title("Я - джокер")
                                                   .build()).build());
        return questions;
    }
}
