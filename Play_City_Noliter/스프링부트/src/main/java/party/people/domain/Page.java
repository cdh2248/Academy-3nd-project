package party.people.domain;

import lombok.Data;

@Data
public class Page {
    private int pageNum; // 페이지 번호
    private int amount;  // 한 페이지당 데이터 개수

    public Page() {
        this(1, 1); // 기본 값을 1페이지 10개로 지정
    }

    public Page(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
    }
}
