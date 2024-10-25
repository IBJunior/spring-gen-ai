package fly.intelligent.genai.controllers;

import fly.intelligent.genai.entities.Seasons;
import fly.intelligent.genai.repositories.DestinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.ai.model.Content;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AssistantControllerTest {

    @Autowired
    private ChatModel chatModel;
    @Autowired
    private ChatClient.Builder evaluatorChatClientBuilder;
    @Autowired
    private VectorStore vectorStore;
    @Value("classpath:prompts/main-system-message.st")
    private Resource mainSystemMessage;
    @Autowired
    private DestinationRepository destinationRepository;

    @BeforeEach
    void setUp() {
        // Use different model for evaluation
        evaluatorChatClientBuilder = evaluatorChatClientBuilder
                .defaultOptions(OpenAiChatOptions.builder()
                        .withModel(OpenAiApi.ChatModel.GPT_4_O)
                        .build());
    }

    @Test
    @DisplayName("Should evaluate accuracy of general information response")
    void getGeneralInformationsTest() {
        String userText = "Quelles sont les plus belles plages en Afrique ?";

        ChatResponse response = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .user(userText)
                .system(mainSystemMessage)
                .call()
                .chatResponse();

        RelevancyEvaluator relevancyEvaluator = new RelevancyEvaluator(evaluatorChatClientBuilder);

        EvaluationRequest evaluationRequest = new EvaluationRequest(userText, List.of(),
                response.getResult().getOutput().getContent());
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        assertTrue(evaluationResponse.isPass(), "Response is not relevant to the question");
        // Au moins 80% de précision dans les réponses
        assertThat(evaluationResponse.getScore()).isGreaterThan(0.8f);

    }

    @Test
    @DisplayName("Should evaluate the accuracy of FAQ question based on the response generated")
    void getFaqResponseTest() {
        String userText = "Quelle est votre politique de remboursement";

        ChatResponse response = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .user(userText)
                .call()
                .chatResponse();

        var relevancyEvaluator = new RelevancyEvaluator(evaluatorChatClientBuilder);
        EvaluationRequest evaluationRequest = new EvaluationRequest(userText,
                response.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS),
                response.getResult().getOutput().getContent());
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        assertTrue(evaluationResponse.isPass(), "Response is not relevant to the question");
        // Accuracy should be at least 80%
        assertThat(evaluationResponse.getScore()).isGreaterThan(0.8f);


    }

    @Test
    @DisplayName("Should evaluate the accuracy when getting best destination")
    void getBestDestinationTest() {
        String userText = "Quelles sont vos meilleures destinations en été ?";

        ChatResponse response = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .options(OpenAiChatOptions
                        .builder().withFunction("getDestinationBySeasons")
                        .build())
                .user(userText)
                .system(mainSystemMessage)
                .call()
                .chatResponse();

        var relevancyEvaluator = new RelevancyEvaluator(evaluatorChatClientBuilder);
        EvaluationRequest evaluationRequest = new EvaluationRequest(userText,
                getBestDestinationBySeasonFromDatabase("été"),
                response.getResult().getOutput().getContent());
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        assertTrue(evaluationResponse.isPass(), "Response is not relevant to the question");
        assertThat(evaluationResponse.getScore()).isGreaterThan(0.8f);
    }

    private List<Content> getBestDestinationBySeasonFromDatabase(String season) {
        // on récupère les destinations par saison
        String bestDestinations = destinationRepository.findAllByBestSeason(Seasons.getValue(season).name())
                .stream()
                .map(dest -> "Id=" + dest.getId() + "\t Saison=" + season + "\t Destination=" + dest.getCountry())
                .collect(Collectors.joining("\n"));

        Document document = new Document("Voilà nos meilleures destinations : \n" + bestDestinations);

        return List.of(document);
    }
}