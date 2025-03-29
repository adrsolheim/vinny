package no.vinny.nightfly.brewfather.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RecipeJson {
    @JsonProperty("_id")
    private String brewfatherId;
    @JsonProperty("_type")
    private String recipeType;
    private Double abv;
    private Double attenuation;
    private String author;
    private Integer batchSize;
    private Integer boilSize;
    private Integer boilTime;
    private Double buGuRatio;
    private Double carbonation;
    private Double color;
    // add defaults class
    private Double efficiency;
    // add equipment?
    private List<Fermentable> fermentables;
    private List<Hop> hops;
    private Double hopsTotalAmount;
    private Double ibu;
    // add img
    private Double mashEfficiency;
    private String name;
    private String notes;
    private Double og;
    private Double postBoilGravity;
    private Double preBoilGravity;
    private Double primaryTemp;
    // add style?
    // add tags
    @JsonProperty("type")
    private String brewingStyle;
    // add water

    @Data
    public static class Fermentable {
        @JsonProperty("_id")
        private String brewfatherId;
        private Double attenuation;
        private Double color;
        private String name;
        private Boolean notFermentable;
        private String notes;
        private String origin;
        private Double percentage;
        private String supplier;
        private String type; // TODO: enum
        private String userNotes;
    }

    @Data
    public static class Hop {
        @JsonProperty("_id")
        private String brewfatherId;
        private Double alpha;
        private Double amount;
        private Double ibu;
        private String name;
        private String notes;
        private String origin;
        private Double temp;
        private Integer time;
        private String pellet; // TODO: enum
        private String usage; // TODO: enum
        private String use; // TODO: enum
        private String userNotes;
        private String year; // TODO: type?
    }

    @Data
    public static class Yeast {
        @JsonProperty("_id")
        private String brewfatherId;
        private Double amount;
        private Double attenuation;
        private String description;
        private String flocculation; // TODO: enum
        private String laboratory;
        private Double maxAbv;
        private Double maxAttenuation;
        private Double maxTemp;
        private Double minAttenuation;
        private Double minTemp;
        private String name;
        private String type; // TODO: enum
        private String unit; // TODO: enum
        private String userNotes;
    }
}
