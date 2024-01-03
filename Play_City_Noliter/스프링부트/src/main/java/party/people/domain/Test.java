package party.people.domain;

import lombok.Data;

@Data
public class Test {
    /* DB접속 테스트용 도메인 */
    private String testString;
    private Long testNumber;

    /* 기본 생성자 */
    public Test(){};

    /* 생성자 */
    public Test(String testString, Long testNumber) {
        this.testString = testString;
        this.testNumber = testNumber;
    }
}
