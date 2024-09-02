package alcoholboot.toastit.feature.trendcocktail.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CocktailData {
    private String startDate;
    private String endDate;
    private String timeUnit;
    private List<Result> results;

    // 기본 생성자
    public CocktailData() {}

    // Getters and Setters
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
    @JsonCreator
    public CocktailData(@JsonProperty("startDate") String startDate,
                        @JsonProperty("endDate") String endDate,
                        @JsonProperty("timeUnit") String timeUnit,
                        @JsonProperty("results") List<Result> results) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeUnit = timeUnit;
        this.results = results;
    }

    public static class Result {
        private String title;
        private List<String> keywords;
        private List<Data> data;

        // 기본 생성자
        public Result() {}

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }

        @JsonCreator
        public Result(@JsonProperty("title") String title,
                      @JsonProperty("keywords") List<String> keywords,
                      @JsonProperty("data") List<Data> data) {
            this.title = title;
            this.keywords = keywords;
            this.data = data;
        }

    }

    public static class Data {
        private String period;
        private double ratio;

        // 기본 생성자
        public Data() {}

        // Getters and Setters
        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public double getRatio() {
            return ratio;
        }

        public void setRatio(double ratio) {
            this.ratio = ratio;
        }

        @JsonCreator
        public Data(@JsonProperty("period") String period,
                    @JsonProperty("ratio") double ratio) {
            this.period = period;
            this.ratio = ratio;
        }
    }
}
