package fly.intelligent.genai.controllers;

import fly.intelligent.genai.dto.ChatDTO;
import fly.intelligent.genai.services.FAQService;
import fly.intelligent.genai.services.GeneralInfoService;
import fly.intelligent.genai.services.ReservationsService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/assistant")
@CrossOrigin(value = {"http://localhost:4200/"})
public class AssistantController {
    private final GeneralInfoService generalInfoService;
    private final FAQService faqService;
    private final ReservationsService reservationsService;

    public AssistantController(GeneralInfoService generalInfoService, FAQService faqService, ReservationsService reservationsService) {
        this.generalInfoService = generalInfoService;
        this.faqService = faqService;
        this.reservationsService = reservationsService;
    }

    @PostMapping("/info")
    public Flux<String> getInformation(@RequestBody ChatDTO chat) {
        return this.generalInfoService.get(chat.question());
    }

    @PostMapping("/faq")
    public Flux<String> getFaqResponse(@RequestBody ChatDTO chat) {
        return this.faqService.getResponse(chat);
    }

    @PostMapping("/reservations")
    public Flux<String> getReservations(@RequestBody ChatDTO chat) {
        return this.reservationsService.get(chat);
    }

}
