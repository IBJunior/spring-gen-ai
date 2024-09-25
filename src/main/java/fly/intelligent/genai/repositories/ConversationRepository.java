package fly.intelligent.genai.repositories;

import fly.intelligent.genai.entities.ConversationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<ConversationHistory, Long> {
}
