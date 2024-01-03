package party.people.domain;


import lombok.Data;

@Data
public class SearchInput {
    private Long sequenceId;
    private String category;
    private String keyword;
    private String address;

    public SearchInput(){
    }

    public SearchInput(String category, String keyword, String address) {
        this.category = category;
        this.keyword = keyword;
        this.address = address;
    }
}
