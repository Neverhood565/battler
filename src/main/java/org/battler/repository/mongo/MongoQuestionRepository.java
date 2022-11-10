package org.battler.repository.mongo;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.battler.model.question.Question;
import org.battler.model.session.QuestionType;
import org.battler.repository.QuestionRepository;
import org.battler.repository.mongo.entity.QuestionEntity;
import org.battler.repository.mongo.mapper.QuestionMapper;
import org.bson.BsonDocument;
import org.bson.Document;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.bson.BsonDocument.parse;

/**
 * Created by romanivanov on 06.11.2022
 */
@Slf4j
@ApplicationScoped
public class MongoQuestionRepository implements QuestionRepository, ReactivePanacheMongoRepository<QuestionEntity> {

    public static final String SAMPLE_AGGREGATION = "{ $sample: { size: %d } }";
    public static final String MATCH_AGGREGATION = "{ $match: { type: '%s' } }";
    @Inject
    QuestionMapper mapper;

    @Override
    public CompletionStage<List<Question>> getNextRandomQuestions(final int amount, QuestionType questionType) {
        return mongoCollection()
                .aggregate(getAggregationPipeline(amount, questionType))
                .map(mapper::toModel)
                .collect()
                .asList()
                .subscribeAsCompletionStage();
    }

    private static List<BsonDocument> getAggregationPipeline(final int amount, QuestionType questionType) {
        List<BsonDocument> aggregations = new ArrayList<>();
        if (questionType != null) {
            aggregations.add(parse(format(MATCH_AGGREGATION, questionType)));
        }
        aggregations.add(parse(format(SAMPLE_AGGREGATION, amount)));
        return aggregations;
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
