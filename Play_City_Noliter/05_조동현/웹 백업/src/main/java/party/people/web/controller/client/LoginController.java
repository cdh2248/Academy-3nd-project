package party.people.web.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import party.people.domain.Client;
import party.people.repository.client.ClientInterface;
import party.people.service.keyword.KeywordInSomething;
import party.people.service.login.LoginInterface;
import party.people.web.controller.client.formAndDto.LoginForm;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("client")
public class LoginController {

    private final LoginInterface loginInterface;
    private final ClientInterface clientInterface;
    private final KeywordInSomething keywordInSomething;

    /* Thymeleaf onclick:login 수행시 페이지 이동*/
    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        /* DB를 정렬하는 로직 */
//        keywordInSomething.allPlaceSort();
        log.info("loginForm] "+ form);

        return "login/loginForm";
    }

    /* 로그인 기능 수행 */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                        Model model, HttpServletRequest request){

        /* Validation 체크 => 밸리데이션 오류시 login페이지로 리턴 오류메시지(LoginForm 확인) 출력 */
        if (bindingResult.hasErrors()){
            return "login/loginForm";
        }


        /* html단에서 입력받은 id, password를 사용해 DB에 있는지 확인 */
        Client login = loginInterface.login(form.getClientId(), form.getPassword());
        /* login 객체는 DB에 일치하는 정보가 있으면 Client 객체 반환, 일치 정보가 없으면 null 반환 */
        log.info("login] "+login);
        /* html단에 login객체 전송  */
        model.addAttribute("client",login);

        /* DB에 일치하는 정보가 없으면 로그인창으로 다시 이동 */
        /* ID 혹은 Password가 일치하지 않는다고 validation 오류 thymeleaf단으로 전달 */
        if (login == null) {
            bindingResult.rejectValue("password","password.invalid","아이디 혹은 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }

        /* logininfo로 세션 객체 생성 */
        HttpSession loginInfo = request.getSession();
        /* "로그인"으로 호출하면 Client login 객체 로딩 */
        loginInfo.setAttribute("로그인",login);

        /* DB에 일치하는 정보가 있으면 로그인 성공 페이지로 이동 */
        /* 해당 결과는 HomeController에서 처리 */
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        /* HomeController 참고*/
        /* 세션 정보 가져오기 */
        HttpSession loginInfo = request.getSession(false);

        /* loginInfo 세션이 널이 아닐 경우 세션 정보 초기화(null로 바꿈) */
        if(loginInfo!=null){
            loginInfo.invalidate();
        }
        /* 해당 결과는 HomeController에서 처리 */
        return "redirect:/";
    }

}
