package fly.intelligent.genai.services.impl;

import fly.intelligent.genai.services.GeneralInfoService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GeneralInfoServiceImpl implements GeneralInfoService {
    private final ChatClient chatClient;
    @Value("classpath:prompts/main-system-message.st")
    private Resource mainSystemMessage;

    public GeneralInfoServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Flux<String> get(String question) {
        return this.chatClient
                .prompt()
                .user(question)
                .system(mainSystemMessage)
                .stream()
                .content();
    }
}
