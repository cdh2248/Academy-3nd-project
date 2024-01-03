package party.people.web.controller.analysis;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import party.people.domain.Place;
import party.people.repository.place.PlaceInterface;

import java.util.ArrayList;
import java.util.List;

import static party.people.web.controller.category.CategoryController.loginCheck;


@Controller
@RequiredArgsConstructor
@Slf4j
public class analcontroller {

    @GetMapping("bup")
    public String bup(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/bup";
    }
    @GetMapping("in_city")
    public String in_city(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/in_city";
    }
    @GetMapping("in_t")
    public String in_t(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/in_t";
    }
    @GetMapping("juan")
    public String juan(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/juan";
    }
    @GetMapping("k_in_univer")
    public String k_in_univer(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/k_in_univer";
    }
    @GetMapping("songdo")
    public String songdo(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/songdo";
    }
    @GetMapping("sucknam")
    public String sucknam(HttpServletRequest request, Model model){
        /* side lnb 출력용 */
        model.addAttribute("category","analysis");

        /* 로그인 유무 체크 */
        /* 함수 정보는 함수 이름 컨트롤 클릭 */
        loginCheck(request, model);

        return "analysis/sucknam";
    }
}