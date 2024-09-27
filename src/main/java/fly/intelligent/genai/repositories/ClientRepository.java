package fly.intelligent.genai.repositories;

import fly.intelligent.genai.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
