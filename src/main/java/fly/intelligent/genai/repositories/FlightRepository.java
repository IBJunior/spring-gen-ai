package fly.intelligent.genai.repositories;

import fly.intelligent.genai.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
