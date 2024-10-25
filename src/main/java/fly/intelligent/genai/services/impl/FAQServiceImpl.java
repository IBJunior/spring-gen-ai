package fly.intelligent.genai.services.impl;

import fly.intelligent.genai.dto.ChatDTO;
import fly.intelligent.genai.services.FAQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class FAQServiceImpl implements FAQService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    Logger log = LoggerFactory.getLogger(FAQServiceImpl.class);

    public FAQServiceImpl(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @Override
    public Flux<String> getResponse(ChatDTO chatDTO) {
        // QuestionAnswerAdvisor is generating a default system message we are overriding it with this one
        // Normally the message should not be in clear like here,it should come from elsewhere like we did in ReservationsServiceImpl
        String userTextAdvise = """
                Ton nom est Fly Assistant,tu es un assistant utile de la compagnie aérienne Fly Intelligent.
                                
                Soit le context suivant : {question_answer_context}
                                
                Pour répondre à un utilisateur tu dois prendre en considération les points suivants :
                                
                1.Réponse basée sur le contexte : Si une question est posée et que la réponse est déjà présente dans le contexte de la conversation, utilise cette information pour répondre.
                                
                2. Salutations : Si la question est une salutation (par exemple, "Bonjour", "Salut", ou toute autre forme de politesse), tu dois répondre avec une salutation appropriée, toujours en français.
                                
                3. Questions sur les voyages ou la géographie : Si la question concerne les voyages ou la géographie (par exemple, "Quelle est la capitale de l'Italie ?" ou "Où se trouve Paris ?"), tu dois répondre correctement et toujours en français.
                                
                4. Langue des réponses: Quelle que soit la langue utilisée pour poser une question, tu dois toujours répondre en français.
                                
                Ton objectif est de toujours fournir des réponses précises et claires, conformément à ces règles.
                """;
        return this.chatClient
                .prompt()
                .user(chatDTO.question())
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults(), userTextAdvise))
                .stream().content();
    }
}
