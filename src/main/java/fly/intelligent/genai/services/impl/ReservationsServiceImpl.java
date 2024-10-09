package fly.intelligent.genai.services.impl;

import fly.intelligent.genai.dto.ChatDTO;
import fly.intelligent.genai.services.ReservationsService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class ReservationsServiceImpl implements ReservationsService {
    private final ChatClient chatClient;
    @Value("classpath:prompts/main-system-message.st")
    private Resource mainSystemMessage;

    public ReservationsServiceImpl(ChatClient chatClient, ChatClient.Builder builder, ChatMemory chatMemory) {
        // Mutating the chatClient to add the getFlightsByUser function definition
        this.chatClient = chatClient
                .mutate()
                .defaultFunctions("getFlightsByUser")
                .build();
    }

    @Override
    public Flux<String> get(ChatDTO chat) {
        UserMessage userMessage = new UserMessage(chat.question());

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(mainSystemMessage);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("userId", "1", "question_answer_context", ""));

        return this.chatClient
                .prompt()
                .user(userMessage.getContent())
                .system(systemMessage.getContent())
                .stream()
                .content();
    }
}
