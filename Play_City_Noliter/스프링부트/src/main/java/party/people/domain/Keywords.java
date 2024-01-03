package party.people.domain;

import lombok.Data;

@Data
public class Keywords {
    /* 통합 관리용 키워드 도메인 */

    private Long indexId;
    private String category;
    private String keywords;

    public Keywords(){
    }

    public Keywords(String category, String keywords) {
        this.category = category;
        this.keywords = keywords;
    }
}
