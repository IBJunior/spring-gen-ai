package fly.intelligent.genai.services.impl;

import fly.intelligent.genai.dto.ChatDTO;
import fly.intelligent.genai.services.DestinationsService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DestinationsServiceImpl implements DestinationsService {
    private final ChatClient chatClient;
    @Value("classpath:prompts/main-system-message.st")
    private Resource mainSystemMessage;

    public DestinationsServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Flux<String> get(ChatDTO chat) {
        UserMessage userMessage = new UserMessage(chat.question());

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(mainSystemMessage);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("userId", "1", "question_answer_context", ""));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage), OpenAiChatOptions.builder()
                .withFunctions(Set.of("getDestinationBySeasons",
                        "getBudgetByDestinationAndNumberOfDays"))
                .build());

        return this.chatClient
                .prompt(prompt)
                .stream()
                .content();
    }
}
