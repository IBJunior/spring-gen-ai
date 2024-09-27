package fly.intelligent.genai.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Client {
    @Id
    private Long id;
    private String name;
    private Long flightId;

    public Long getId() {
        return id;
    }

    public Client() {

    }

    public Client(Long id, Long flightId, String name) {
        this.id = id;
        this.flightId = flightId;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }
}
