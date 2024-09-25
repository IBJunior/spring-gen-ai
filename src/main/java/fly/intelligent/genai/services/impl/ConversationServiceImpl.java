package fly.intelligent.genai.services.impl;

import fly.intelligent.genai.dto.ConversationDTO;
import fly.intelligent.genai.entities.ConversationHistory;
import fly.intelligent.genai.repositories.ConversationRepository;
import fly.intelligent.genai.services.ConversationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationServiceImpl(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public void save(ConversationDTO conversationDTO) {
        ConversationHistory history = new ConversationHistory();
        history.setQuestion(conversationDTO.question());
        history.setAnswer(conversationDTO.answer());
        this.conversationRepository.save(history);
    }

    @Override
    public List<ConversationDTO> getConversationsHistory(){
        return this.conversationRepository.findAll()
                .stream()
                .map(convo -> new ConversationDTO(convo.getQuestion(), convo.getAnswer()))
                .collect(Collectors.toList());
    }
}
