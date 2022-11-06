package org.battler.repository.mongo;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.battler.model.question.Question;
import org.battler.repository.QuestionRepository;
import org.battler.repository.mongo.entity.QuestionEntity;
import org.battler.repository.mongo.mapper.QuestionMapper;
import org.bson.BsonDocument;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.bson.BsonDocument.parse;

/**
 * Created by romanivanov on 06.11.2022
 */
@Slf4j
@ApplicationScoped
public class MongoQuestionRepository implements QuestionRepository, ReactivePanacheMongoRepository<QuestionEntity> {

    public static final String SAMPLE_AGGREGATION = "{ $sample: { size: %d } }";
    @Inject
    QuestionMapper mapper;

    @Override
    public CompletionStage<List<Question>> getNextRandomQuestions(final int amount) {
        return mongoCollection()
                .aggregate(getAggregationPipeline(amount))
                .map(mapper::toModel)
                .collect()
                .asList()
                .subscribeAsCompletionStage();
    }

    private static List<BsonDocument> getAggregationPipeline(final int amount) {
        return singletonList(parse(format(SAMPLE_AGGREGATION, amount)));
    }

    @Override
    public void persistQuestion(final Question question) {
        persist(mapper.toEntity(question)).subscribe().with(questionEntity -> log.info("Question persisted"));
    }

    @Override
    public List<Question> findAllQuestions() {
        return null;
    }
}
