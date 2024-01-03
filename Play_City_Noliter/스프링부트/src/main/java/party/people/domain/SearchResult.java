package party.people.domain;

import lombok.Data;

@Data
public class SearchResult {
    Long sequenceId;
    String result;


    public SearchResult(){}

    public SearchResult(String result) {
        this.result = result;
    }
}
