package fly.intelligent.genai.functions;


import com.fasterxml.jackson.annotation.JsonClassDescription;
import fly.intelligent.genai.entities.Client;
import fly.intelligent.genai.entities.Flight;
import fly.intelligent.genai.repositories.ClientRepository;
import fly.intelligent.genai.repositories.FlightRepository;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service("getFlightsByUser")
@Description("get user flights based on his userId")
public class FlightsFunction implements Function<FlightsFunction.FlightRequest, List<FlightsFunction.FlightResponse>> {
    private final FlightRepository flightRepository;
    private final ClientRepository clientRepository;

    public FlightsFunction(FlightRepository flightRepository, ClientRepository clientRepository) {
        this.flightRepository = flightRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<FlightResponse> apply(FlightRequest flightRequest) {
        Optional<Client> client = clientRepository.findById(Long.parseLong(flightRequest.userId));
        if (client.isEmpty()) return null;
        Optional<Flight> flight = flightRepository.findById(client.get().getFlightId());
        return flight.map(value -> List.of(
                new FlightResponse(value.getDeparture(), value.getDestination(), value.getNumber(), value.getFormattedDate())
        )).orElse(null);
    }

    @JsonClassDescription("Get user flights")
    public static record FlightRequest(String userId) {
    }

    @JsonClassDescription("user flight with destination, flight number and flight date")
    public static record FlightResponse(String departure, String destination, int number, String date) {
    }

}
