package fly.intelligent.genai.services;

import reactor.core.publisher.Flux;

public interface GeneralInfoService {
    Flux<String> get(String question);
}
