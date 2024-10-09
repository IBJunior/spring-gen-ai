package fly.intelligent.genai.services.impl;

import fly.intelligent.genai.dto.ChatDTO;
import fly.intelligent.genai.services.DestinationsService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
public class DestinationsServiceImpl implements DestinationsService {
    private final ChatClient chatClient;
    @Value("classpath:prompts/main-system-message.st")
    private Resource mainSystemMessage;

    public DestinationsServiceImpl(ChatClient chatClient) {
        // Mutate the chatClient to include functions
        this.chatClient = chatClient
                .mutate()
                .defaultFunctions("getDestinationBySeasons",
                        "getBudgetByDestinationAndNumberOfDays")
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }

    @Override
    public Flux<String> get(ChatDTO chat) {
        UserMessage userMessage = new UserMessage(chat.question());

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(mainSystemMessage);
        Message systemMessage = systemPromptTemplate
                .createMessage(Map.of("userId", "1", "question_answer_context", ""));

        return this.chatClient
                .prompt()
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, "chatId") // should be dynamic this is used for demo purposes
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .user(userMessage.getContent())
                .system(systemMessage.getContent())
                .stream()
                .content();
    }
}
