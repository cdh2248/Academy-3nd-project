package party.people.web.controller.analysis;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static party.people.web.controller.category.CategoryController.loginCheck;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AnalysisController {

    @GetMapping("bup")
    public String bup(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* side의 active용 전환 */
        model.addAttribute("lnbInfo","부평역");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/bup";
    }
    @GetMapping("in_city")
    public String in_city(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* side의 active용 전환 */
        model.addAttribute("lnbInfo","시청역");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/in_city";
    }
    @GetMapping("in_t")
    public String in_t(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* side의 active용 전환 */
        model.addAttribute("lnbInfo","터미널역");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/in_t";
    }
    @GetMapping("juan")
    public String juan(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* side의 active용 전환 */
        model.addAttribute("lnbInfo","주안역");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/juan";
    }
    @GetMapping("k_in_univer")
    public String k_in_univer(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* side의 active용 전환 */
        model.addAttribute("lnbInfo","교대역");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/k_in_univer";
    }
    @GetMapping("songdo")
    public String songdo(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* side의 active용 전환 */
        model.addAttribute("lnbInfo","공원역");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/songdo";
    }
    @GetMapping("sucknam")
    public String sucknam(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* side의 active용 전환 */
        model.addAttribute("lnbInfo","석남역");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/sucknam";
    }
}