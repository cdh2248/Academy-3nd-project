package party.people.web.controller.client;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import party.people.domain.Client;
import party.people.repository.client.ClientInterface;
import party.people.web.controller.client.formAndDto.FindPwForm;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/client")
public class FindIdPwController {

    private final ClientInterface clientInterface;

    /* ID찾기 페이지로 이동 */
    @GetMapping("/findId")
    public String findIdForm(Client client){
        return "login/findIdForm2";
    }

    @PostMapping("/findId")
    public String findId(@RequestParam("clientEmail") String clientEmail,@Valid Client client,
                         BindingResult bindingResult,Model model){
        log.info("전달받은 이메일] "+clientEmail);
        /* 제공 받은 이메일 정보로 일치하는 데이터가 있는지 확인 없을 경우 NULL 반환 */
        Client findId =clientInterface.findByClientEmail(clientEmail).orElse(null);
        log.info("findId] "+findId);
        /* 일치하는 정보가 없을 경우 에러메시지 출력후 아이디 찾기 창으로 전송 */
        if (findId==null){
            bindingResult.rejectValue("clientEmail","clientEmail.invalid","해당하는 아이디가 없습니다.");
//            model.addAttribute("client",client);
            return "login/findIdForm2";
        }

        /* 이메일 정보와 일치하는 데이터가 있을경우 데이터 전송 정보는 thymeleaf에서 처리 */
        model.addAttribute("client", findId);

        /* id결과 페이지로 이동 */
        return "login/findIdResult2";
    }

    /* 비밀번호 찾기 페이지로 이동 */
    @GetMapping("/findPw")
    /* Validation 및 thymeleaf 정보 제공을 위해 form 제공 */
    public String findPwForm(@ModelAttribute("findPwForm") FindPwForm form){
        log.info("findPwForm] "+form);
        return "login/findPwForm2";
    }

    @PostMapping("/findPw")
    public String findPw(@Valid @ModelAttribute FindPwForm form, Model model, BindingResult bindingResult){
        /* form정보로 아이디와 이메일을 받아 DB에 해당 데이터가 있는지 탐색 없을 경우 NULL 반환 */
        log.info("전달받은 아이디 + 이메일] "+form.getClientId() + "+" + form.getClientEmail());
        Client findPw = clientInterface.findPassword(form).orElse(null);
        log.info("findPw] "+findPw);

        /* 해당 정보가 없을 경우 에러메시지 출력 후 비밀번호 찾기 페이지로 전송 */
        if (findPw==null) {
            bindingResult.rejectValue("clientId","clientId.invalid","제공해주신 ID와 Email과 일치하는 정보가 없습니다.");
//            model.addAttribute("client", client);
            return "login/findPwForm2";
        }

        /* 일치하는 데이터가 있을 경우 thymeleaf에 전송 */
        model.addAttribute("client",findPw);

        return "login/findPwResult2";
    }
}
