package party.people.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class Client {
    /* 클라이언트 관리용 도메인 */

    private Long sequenceId; /* INDEX용 ID */
    @NotBlank(message = "ID는 필수 입력 사항입니다.")
    private String clientId; /* 고객 ID */
    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Size(min=4, message = "비밀번호는 최소 4글자 이상이어야 합니다.")
    private String password; /* CLIENT 비밀번호 */
    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Size(min=4, message = "비밀번호는 최소 4글자 이상이어야 합니다.")
    private String password2; /* 회원가입시에는 일치 확인용 / 수정시에는 새로운 비밀번호 */
    private String password3; /* 수정시에만 사용되는 새로운 비밀번호와 일치하는지 확인용 */
    @NotBlank
    @Email
    private String clientEmail; /* 고객 이메일 */
    private String keyword; /* 검색 키워드를 담을 키워드 */

    public Client(){}

    public Client(String clientId, String password, String clientEmail, String keyword) {
        this.clientId = clientId;
        this.password = password;
        this.clientEmail = clientEmail;
        this.keyword = keyword;
    }
}
