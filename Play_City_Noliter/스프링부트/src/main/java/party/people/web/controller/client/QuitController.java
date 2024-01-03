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

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/client")
public class QuitController {
    private final ClientInterface clientInterface;


    @GetMapping("/quit")
    public String quitForm(Client client, Model model, HttpServletRequest request){
        /* 기존 세션정보 호출 */
        HttpSession loginInfo = request.getSession(false);
        /* 세션정보를 Client객체로 형변환 */
        Client loginClient = (Client) loginInfo.getAttribute("로그인");
        model.addAttribute("client", loginClient);
        return "client/quit";
    }

    @PostMapping("/quit")
    public String quit(@Valid @ModelAttribute Client client, BindingResult bindingResult, Model model,
                        HttpServletRequest request){

        log.info("quit] "+client);

        /* 기존 세션정보 호출 */
        HttpSession loginInfo = request.getSession(false);
        /* 세션정보를 Client객체로 형변환 */
        Client loginClient = (Client) loginInfo.getAttribute("로그인");

        /* 입력한 비밀번호가 DB상 비밀번호와 일치하는지 확인*/
        Boolean pass = loginClient.getPassword().equals(client.getPassword());
        if (!pass){
            bindingResult.rejectValue("password","password.invalid","비밀번호가 일치하지 않습니다.");
            return "client/quit";
        }

        /* 두 비밀번호가 일치하는지 확인*/
        Boolean pass2 = client.getPassword().equals(client.getPassword2());
        if (!pass2){
            bindingResult.rejectValue("password2","password2.invalid","두 비밀번호가 일치하지 않습니다.");
            return "client/quit";
        }

        if(loginInfo!=null){
            loginInfo.invalidate();
        }

        clientInterface.quit(client);


        return "redirect:/";
    }



}
