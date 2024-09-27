package fly.intelligent.genai.services;

import fly.intelligent.genai.dto.ChatDTO;
import reactor.core.publisher.Flux;

public interface DestinationsService {
    Flux<String> get(ChatDTO chat);
}
