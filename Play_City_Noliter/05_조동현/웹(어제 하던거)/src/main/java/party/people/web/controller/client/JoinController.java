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
import party.people.web.controller.client.formAndDto.LoginForm;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("client")
public class JoinController {

    private final ClientInterface clientInterface;

    /* 회원가입 페이지로 연결 */
    @GetMapping("/join")
    /* client 객체는 Validation 검증용으로 전송해야 에러 안 뜸 */
    /* thymeleaf단에서 th:object로 client를 받기 때문으로 추정됨.. */
    public String joinForm(Client client, Model model){
        /* 체크 박스 메서드(최하단(메서드이름 컨트롤 클릭도 가능)에서 메서드 확인 가능) */
        /* 요약 : 체크박스 리스트를 thymeleaf단에 제공 */
        checkbox(model);
        return "client/joinForm";
    }


    /* 회원가입 페이지에서 회원가입 클릭시 기능 수행 */
    @PostMapping("/join")
    /* @Valid => Valid 어노테이션 적용 */
    /* BindingResult => 오류메세지를 타임리프단에 전달 */
    /* RequestedParam => 체크박스 리스트 선택결과를 리스트로 가져오기,
       requird=false는 해당 list가 null일 경우 에러메시지 방지 */
    public String join(@Valid @ModelAttribute Client client, BindingResult bindingResult, Model model,
                       @RequestParam(value = "selectedItems", required = false) List<String> selectedItems){

        /* 체크 박스 메서드(최하단(메서드이름 컨트롤 클릭도 가능)에서 메서드 확인 가능) */
        /* 요약 : 체크박스 리스트를 thymeleaf단에 제공 */
        checkbox(model);

        /* 받아온 체크 박스 정보 처리(최하단 확인) */
        String regionKeywords = makeKeywords(selectedItems);


        /* 체크리스트 키워드를 client 객체 keyword에 추가 */
        client.setKeyword(regionKeywords);
        log.info("클라이언트 객체 확인 "+client);

        /* 아이디 중복 검사 */
        Client duplicateId = clientInterface.findByClientId(client.getClientId()).orElse(null);
        if (duplicateId!=null){
            bindingResult.rejectValue("clientId","clientId.invalid","해당 ID는 이미 존재합니다");
            return "client/joinForm";
        }
        log.info("중복검사에서 에러니?");

        /* 비밀번호, 비밀번호2 일치 확인 */
        Boolean pass = (client.getPassword()).equals(client.getPassword2());
        if (!pass){
            bindingResult.rejectValue("password2","password2.invalid","두 비밀번호가 일치하지 않습니다.");
            return "client/joinForm";
        }
        /* 여기서 에러니? */

        /* Validation 체크 => 밸리데이션 오류시 join페이지로 리턴 오류메시지(client domain 확인) 출력 */
        if(bindingResult.hasErrors()) {
            return "client/joinForm";
        }

        /* Validation 오류 없으면 DB에 CLIENT 객체를 저장 */
        clientInterface.save(client);

        /* 저장 완료 후 초기 홈페이지로 이동 */
        return "redirect:/";

    }

    /* 체크박스 키워드를 DB에 담을 형식(keyword/count)으로 전환하는 메서드 */
    public static String makeKeywords(List<String> selectedItems) {
        /* 체크리스트를 키워드에 담을 빈 변수 생성 */
        String regionKeywords = "";
        int count = 0;
        /* 체크리스트가 null이 아닐 경우에만 기능수행(에러 방지) */
        if (selectedItems !=null) {
            /* 체크박스 선택된 리스트를 위 변수에 입력 */
            for (String regionKeyword : selectedItems) {
                if (count == 0) {
                    regionKeywords = regionKeywords + regionKeyword+"/1";
                } else {
                    regionKeywords = regionKeywords + "," + regionKeyword+"/1";
                }
                count++;
            }
            log.info("체크리스트 확인 " + regionKeywords);
        }
        return regionKeywords;
    }

    /* 체크박스 메서드화 */
    /* GetMapping, PostMapping에서 모두 사용하기 때문에 중복 방지용으로 메서드로 만듬 */
    public static void checkbox(Model model) {
        /* CHECKBOX리스트를 동적 생성하기 위한 리스트 생성 */
        List<String> regionCheckbox = Arrays.asList("연수구","남동구","부평구","계양구","서구","동구","중구","미추홀구","강화군","옹진군");
        /* 체크박스 LIST를 THYMELEAF단으로 전송 */
        model.addAttribute("regionCheckbox", regionCheckbox);

        /* 위와 동일 */
        List<String> interestCheckbox = Arrays.asList("관광","자연","레저","쇼핑","음식","숙박","문화");
        model.addAttribute("interestCheckbox", interestCheckbox);
    }



}
