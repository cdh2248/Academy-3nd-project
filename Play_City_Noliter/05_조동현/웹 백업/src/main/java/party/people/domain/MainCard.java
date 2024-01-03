package party.people.domain;

import lombok.Data;

@Data
public class MainCard {
    private Long sequenceId;
    private String category;
    private String result;

    public MainCard(){
    }

    public MainCard(String category, String result) {
        this.category = category;
        this.result = result;
    }
}
