package party.people.domain;

import lombok.Data;

@Data
public class SearchResult {
    Long sequenceId;
    String result;
    String sortKeyword;


    public SearchResult(){}

    public SearchResult(String result, String sortKeyword) {
        this.result = result;
        this.sortKeyword = sortKeyword;
    }
}
