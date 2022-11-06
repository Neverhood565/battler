package org.battler.mock;

import org.battler.model.question.Question;
import org.battler.model.session.GameSession;
import org.battler.repository.GameSessionRepository;
import org.battler.repository.QuestionRepository;
import org.battler.socket.SocketEndpoint;
import org.battler.socket.dto.request.Request;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by romanivanov on 14.09.2022
 */
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
@Path("/battler")
public class TestEndpoint {

    @Inject
    SocketEndpoint socketEndpoint;

    @Inject
    GameSessionRepository gameSessionRepository;

    @Inject
    QuestionRepository questionRepository;

    @POST
    @Path("/{token}")
    public void onMessage(@PathParam("token") String userId, Request request) {
        socketEndpoint.handleRequest(request, userId);
    }

    @GET
    @Path("gameSessions")
    public CompletionStage<List<GameSession>> getAllGameSessions() {
        return gameSessionRepository.findAllGameSessions();
    }

    @DELETE
    @Path("/gameSessions")
    public void onMessage() {
        gameSessionRepository.clearGameSessions();
    }

    @POST
    @Path("/question")
    public void saveQuestion(Question question) {
        questionRepository.persistQuestion(question);
    }

    @GET
    @Path("/question")
    public List<Question> getQuestions() {
        return questionRepository.findAllQuestions();
    }

    @GET
    @Path("/question:random")
    public CompletionStage<List<Question>> getQuestionsRandom(@QueryParam("amount") Integer amount) {
        return questionRepository.getNextRandomQuestions(amount);
    }
}
