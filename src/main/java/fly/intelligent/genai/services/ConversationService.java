package fly.intelligent.genai.services;

import fly.intelligent.genai.dto.ConversationDTO;

import java.util.List;

public interface ConversationService {
    void save(ConversationDTO conversationDTO);
    public List<ConversationDTO> getConversationsHistory();
}
