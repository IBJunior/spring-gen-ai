package fly.intelligent.genai.controllers;

import fly.intelligent.genai.dto.ChatDTO;
import fly.intelligent.genai.services.GeneralInfoService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/assistant")
@CrossOrigin(value = {"http://localhost:4200/"})
public class AssistantController {
    private final GeneralInfoService generalInfoService;

    public AssistantController(GeneralInfoService generalInfoService) {
        this.generalInfoService = generalInfoService;
    }

    @PostMapping("/info")
    public Flux<String> getInformation(@RequestBody ChatDTO chat) {
        return this.generalInfoService.get(chat.question());
    }
}
