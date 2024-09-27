package fly.intelligent.genai.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Destination {
    @Id
    private Long id;
    private String country;
    private String bestSeason;

    public Destination() {
    }

    public Destination(Long id, String bestSeason, String country) {
        this.id = id;
        this.country = country;
        this.bestSeason = bestSeason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBestSeason() {
        return bestSeason;
    }

    public void setBestSeason(String bestSeason) {
        this.bestSeason = bestSeason;
    }
}
