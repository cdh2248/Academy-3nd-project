package party.people.domain;

import lombok.Data;

import java.util.List;

@Data
public class MainCardDomain {
    private String category;
    private List<Place>mainPlace;
    private List<String>cloudTitle;
    private List<Integer>cloudValues;

    public MainCardDomain(){
    }

    public MainCardDomain(String category, List<Place> mainPlace, List<String> cloudTitle, List<Integer> cloudValues) {
        this.category = category;
        this.mainPlace = mainPlace;
        this.cloudTitle = cloudTitle;
        this.cloudValues = cloudValues;
    }
}
