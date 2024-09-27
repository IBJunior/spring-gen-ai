package fly.intelligent.genai.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class Flight {
    @Id
    private Long id;

    private LocalDate date;

    private String destination;
    private String departure;

    private int number;

    public Flight(Long id, LocalDate date, String destination, String departure, int number) {
        this.id = id;
        this.date = date;
        this.destination = destination;
        this.departure = departure;
        this.number = number;
    }

    public Flight() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Date:" + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                "Destination:" + destination + "\n" +
                "Number " + number;
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
