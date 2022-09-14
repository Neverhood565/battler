package org.battler.repository;

import org.battler.model.question.Answer;
import org.battler.model.question.Question;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * Created by romanivanov on 31.08.2022
 */
@ApplicationScoped
public class InMemoryQuestionRepository implements QuestionRepository {

    private final List<Question> questions = new ArrayList<>();

    {
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

    }

    @Override
    public List<Question> getNextRandomQuestions(final int amount) {
        List<Question> result = new ArrayList<>();
        IntStream.range(0, amount)
                 .forEach(ignored -> result.add(questions.get(new Random().nextInt(questions.size()))));
        return result;
    }

    @Override
    public void persistQuestion(final Question question) {
        questions.add(question);
    }

    @Override
    public List<Question> findAllQuestions() {
        return questions;
    }
}
