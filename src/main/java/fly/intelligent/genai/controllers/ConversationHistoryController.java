package fly.intelligent.genai.controllers;

import fly.intelligent.genai.dto.ConversationDTO;
import fly.intelligent.genai.services.ConversationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversation-history")
@CrossOrigin(value = {"http://localhost:4200/"})
public class ConversationHistoryController {
    private final ConversationService conversationService;

    public ConversationHistoryController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ResponseEntity<Void> saveConversation(@RequestBody ConversationDTO conversationDTO) {
        this.conversationService.save(conversationDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ConversationDTO>> getConversations() {
        return new ResponseEntity<>(conversationService.getConversationsHistory(), HttpStatus.OK);
    }
}
