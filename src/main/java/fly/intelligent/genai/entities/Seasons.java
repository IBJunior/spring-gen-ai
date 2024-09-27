package fly.intelligent.genai.entities;


public enum Seasons {
    WINTER("hiver"),
    SPRING("printemps"),
    FALL("automne"),
    SUMMER("été");
    private final String frName;

    Seasons(String frName) {
        this.frName = frName;
    }

    public static Seasons getValue(String season) {
        for (Seasons s : Seasons.values()) {
            if (s.name().equalsIgnoreCase(season) || s.frName.equalsIgnoreCase(season)) return s;
        }
        return null;
    }
}
