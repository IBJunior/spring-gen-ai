package fly.intelligent.genai.repositories;


import fly.intelligent.genai.entities.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    Destination findByBestSeason(String season);

    List<Destination> findAllByBestSeason(String season);
}
