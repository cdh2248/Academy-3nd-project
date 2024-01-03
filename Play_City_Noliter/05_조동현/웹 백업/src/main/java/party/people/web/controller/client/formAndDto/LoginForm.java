package party.people.web.controller.client.formAndDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginForm {
    /* HTML 로그인 단에서 제공받는 ID, PASSWORD를 담을 객체 생성용 클래스 */
    @NotBlank(message = "ID는 필수 입력 사항입니다.")
    private String clientId;
    @NotBlank(message = "비밀번호는 필수 입력 사항입니다")
    @Size(min=4, message = "비밀번호는 최소 4글자 이상이어야 합니다.")
    private String password;
}
