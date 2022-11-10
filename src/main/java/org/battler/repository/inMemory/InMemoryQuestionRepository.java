package org.battler.repository.inMemory;

import io.quarkus.arc.DefaultBean;
import org.battler.model.question.Question;
import org.battler.model.session.QuestionType;
import org.battler.repository.QuestionRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.IntStream;

/**
 * Created by romanivanov on 31.08.2022
 */
@DefaultBean
@ApplicationScoped
public class InMemoryQuestionRepository implements QuestionRepository {

    private final List<Question> questions = new ArrayList<>();

    {
        questions.add(Question.builder()
                              .id(UUID.randomUUID().toString())
                              .title("А как какать?")
                              .correctAnswer("Это вертолет").build());
        questions.add(Question.builder()
                              .id(UUID.randomUUID().toString())
                              .title("В чем разница между JS и Java?")
                              .correctAnswer("Java - для натуралов").build());
        questions.add(Question.builder()
                              .id(UUID.randomUUID().toString())
                              .title("Где джокер?")
                              .correctAnswer("Я - джокер").build());

    }

    @Override
    public CompletionStage<List<Question>> getNextRandomQuestions(final int amount, QuestionType questionType) {
        return CompletableFuture.supplyAsync(() -> {
            List<Question> result = new ArrayList<>();
            IntStream.range(0, amount)
                     .forEach(ignored -> result.add(questions.get(new Random().nextInt(questions.size()))));
            return result;
        });
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
