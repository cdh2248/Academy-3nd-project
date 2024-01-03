package party.people.domain;

import lombok.Data;

@Data
public class PageDto {
    private int startPage;
    private int endPage;

    private boolean prev,next;

    private int total;
    private Page page;

    public PageDto(Page page,int total){
        this.page =page;
        this.total = total;

        this.endPage = (int)(Math.ceil(page.getPageNum()/5.0))*5;
        this.startPage = this.endPage - 4;

        int realEnd = (int)(Math.ceil((total*1.0)/page.getAmount()));

        if(realEnd < this.endPage){
            this.endPage = realEnd;
        }

        this.prev = this.startPage > 1;
        this.next = this.endPage < realEnd;

    }

}
