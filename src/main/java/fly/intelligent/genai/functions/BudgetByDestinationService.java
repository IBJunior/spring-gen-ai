package fly.intelligent.genai.functions;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service("getBudgetByDestinationAndNumberOfDays")
@Description("get budget for a destination based on number days")
public class BudgetByDestinationService implements Function<BudgetByDestinationService.DestinationBudgetRequest,
        BudgetByDestinationService.DestinationBudgetResponse> {
    @Override
    public DestinationBudgetResponse apply(DestinationBudgetRequest destinationBudget) {
        if (destinationBudget.destination() == null) return new DestinationBudgetResponse(0L);
        long dailyCost = 0;
        switch (destinationBudget.destination().toUpperCase()) {
            case "CAMEROUN" -> dailyCost = 23;
            case "AUSTRALIE" -> dailyCost = 57;
            case "AFRIQUE DU SUD" -> dailyCost = 36;
            case "ANGLETERRE" -> dailyCost = 60;
        }
        return new DestinationBudgetResponse(destinationBudget.numberOfDays() * dailyCost);
    }

    @JsonClassDescription("Get budget for a destination based on number of days and the destination name")
    public record DestinationBudgetRequest(@JsonProperty(required = true) String destination,
                                           @JsonProperty(required = true) Integer numberOfDays) {
    }

    @JsonClassDescription("contains the budget cost for a destination based on number of days")
    public record DestinationBudgetResponse(Long cost) {
    }
}
