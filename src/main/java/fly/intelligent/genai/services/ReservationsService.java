package fly.intelligent.genai.services;

import fly.intelligent.genai.dto.ChatDTO;
import reactor.core.publisher.Flux;

public interface ReservationsService {
    Flux<String> get(ChatDTO chat);
}
