package party.people.web.controller.client.formAndDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FindPwForm {

    /* Password찾기 Validation 및 매핑용 폼 */
    @NotBlank(message = "ID는 필수 입력 사항입니다.")
    private String clientId;
    @NotBlank(message = "Email은 필수 입력 사항입니다")
    private String clientEmail;
}
