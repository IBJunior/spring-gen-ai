package fly.intelligent.genai.functions;


import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import fly.intelligent.genai.entities.Destination;
import fly.intelligent.genai.entities.Seasons;
import fly.intelligent.genai.repositories.DestinationRepository;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service("getDestinationBySeasons")
@Description("get best destination based on a list of seasons")
public class BestDestinationFunction implements Function<BestDestinationFunction.BestDestinationRequest,
        BestDestinationFunction.BestDestinationResponse> {

    private final DestinationRepository destinationRepository;

    public BestDestinationFunction(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @Override
    public BestDestinationResponse apply(BestDestinationRequest request) {
        var seasonsList = request.seasons();
        Seasons seasons = Seasons.getValue(seasonsList.getFirst());
        if (seasons == null) return null;
        List<Destination> destinations = destinationRepository.findAllByBestSeason(seasons.name());
        return new BestDestinationResponse(destinations.stream().map(Destination::getCountry)
                .toList());
    }

    @JsonClassDescription("get destination of a season")
    public record BestDestinationRequest(@JsonProperty(required = true) List<String> seasons) {
    }

    @JsonClassDescription("contains the destinations(countries) to visit based on the seasons")
    public record BestDestinationResponse(List<String> destinations) {
    }

}
