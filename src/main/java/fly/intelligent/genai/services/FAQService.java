package fly.intelligent.genai.services;

import fly.intelligent.genai.dto.ChatDTO;
import reactor.core.publisher.Flux;

public interface FAQService {
    Flux<String> getResponse(ChatDTO chatDTO);
}
