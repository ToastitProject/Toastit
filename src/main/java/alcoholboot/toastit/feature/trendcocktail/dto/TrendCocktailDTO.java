package alcoholboot.toastit.feature.trendcocktail.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrendCocktailDTO {
    private String startDate;
    private String endDate;
    private String timeUnit;
    private List<Result> results;

    public void setResults(List<Result> results) {
        this.results = results;
    }
    @JsonCreator
    public TrendCocktailDTO(@JsonProperty("startDate") String startDate,
                            @JsonProperty("endDate") String endDate,
                            @JsonProperty("timeUnit") String timeUnit,
                            @JsonProperty("results") List<Result> results) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeUnit = timeUnit;
        this.results = results;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Result {
        private String title;
        private List<String> keywords;
        private List<Data> data;

        @JsonCreator
        public Result(@JsonProperty("title") String title,
                      @JsonProperty("keywords") List<String> keywords,
                      @JsonProperty("data") List<Data> data) {
            this.title = title;
            this.keywords = keywords;
            this.data = data;
        }

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Data {
        private String period;
        private double ratio;


        @JsonCreator
        public Data(@JsonProperty("period") String period,
                    @JsonProperty("ratio") double ratio) {
            this.period = period;
            this.ratio = ratio;
        }
    }
}
