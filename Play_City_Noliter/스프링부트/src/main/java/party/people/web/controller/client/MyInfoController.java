package party.people.web.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import party.people.domain.Client;
import party.people.repository.client.ClientInterface;
import party.people.web.controller.client.formAndDto.ClientUpdateDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 각 클래스에서 만든 메서드 호출 */
import static party.people.web.controller.client.JoinController.checkbox;
import static party.people.web.controller.client.JoinController.makeKeywords;
import static party.people.service.keyword.keywordToMapLogic.keywordToMap;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/client")
public class MyInfoController {

    private final ClientInterface clientInterface;

    @GetMapping("/myInfo")
    /* 회원 정보 수정시 로그인한 회원의 정보를 가져오기 위해 세션 불러오기용으로 request호출 */
    public String myInfoForm(Model model,HttpServletRequest request){

        /* JoinController 참고(메서드 컨트롤 클릭으로 메서드 확인 가능) */
        /* 요약 : 체크박스 동적 생성 메서드 */
        checkbox(model);

        /* 기존 세션정보 호출 */
        HttpSession loginInfo = request.getSession(false);

        /* 세션정보를 Client객체로 형변환 */
        Client loginClient = (Client) loginInfo.getAttribute("로그인");

        /* 로그인한 사람의 keyword 호출 */
        String keywords = loginClient.getKeyword();

        /* 키워드 정보가 null일 경우 처리, 안 하면 thymeleaf단에서 selectedKey가 Null이라 오류 발생 */
        if (keywords!=null) {
            /* keywordToMap 메서드 호출 service->keyword->test2 참고(메서드 컨트롤 클릭으로 메서드 확인 가능) */
            /* 메서드로 호출된 맵에서 key값 목록만 List로 변환 */
            Map<String, Integer>map = new HashMap<>();
            List<String> selectedKey = keywordToMap(map, keywords).keySet().stream().toList();
            log.info("myInfoForm] " + selectedKey);
            /* 사용자가 선택한 정보만 체킹하기 위해 선택된 리스트 thymeleaf단에 전달 */
            /* th:checked="${selectedKey.contains(item) ? 'checked' : null}"> */
            /* 위와 같이 리스트에 존재하면 check, 존재하지 않으면 non-check */
            model.addAttribute("selectedKey",selectedKey);
        } else {
            /* null일 경우 빈 리스트 생성 */
            List<String>noneKey = new ArrayList<>();
            model.addAttribute("selectedKey",noneKey);
        }
        model.addAttribute("client", loginClient);

        return "client/myInfo";
    }

    @PostMapping("/myInfo")
    /* 정보수정을 위한 Dto객체, 정보 수정을 위한 selectedItem 리스트 객체 수집 */
    public String myInfo(ClientUpdateDto clientUpdateParam, Model model, HttpServletRequest request,
                         @RequestParam(value = "selectedItems", required = false) List<String> selectedItems,
                         @Valid Client client, BindingResult bindingResult){
        /* JoinController 참고(메서드 컨트롤 클릭으로 메서드 확인 가능) */
        /* 요약 : 체크박스 동적 생성 메서드 */
        checkbox(model);

        /* 기존 세션 정보 호출*/
        HttpSession loginInfo = request.getSession(false);

        /* 세션 정보 Client객체로 형변환 */
        Client loginClient = (Client) loginInfo.getAttribute("로그인");

        /* 로그인한 사람의 keyword 호출 */
        String keywords = loginClient.getKeyword();

        /* keywordToMap 메서드 호출 service->keyword->test2 참고(메서드 컨트롤 클릭으로 메서드 확인 가능) */
        /* 메서드로 호출된 맵에서 key값 목록만 List로 변환 */
        Map<String, Integer>map = new HashMap<>();
        List<String> selectedKey = keywordToMap(map, keywords).keySet().stream().toList();
        model.addAttribute("selectedKey",selectedKey);

        log.info("세션에서 받아온 녀석 "+loginClient);
        log.info("html에서 받아온 녀석 "+clientUpdateParam);


        /* 체크리스트를 keyword화 해주는 메서드 호출 */
        /* JoinController 참고(메서드 컨트롤 클릭으로 메서드 확인 가능) */
        /* 요약 : 전달받은 리스트를 DB 약속한 형태로 변환 */
        String regionKeywords = makeKeywords(selectedItems);


        /* 체크리스트 키워드를 clientDTO 객체 keyword에 추가 */
        clientUpdateParam.setKeyword(regionKeywords);
        log.info("클라이언트 객체 확인 "+clientUpdateParam);


        /* NULL값이면 DB적재시 오류 발생하므로 ""로 변환 => ""해도 DB에서 NULL로 보이므로 같은 결과 NO오류 */
        if (clientUpdateParam.getKeyword()==null){
            clientUpdateParam.setKeyword("");
        }

        /* 기존 비밀번호 일치 확인*/
        Boolean pass = loginClient.getPassword().equals(clientUpdateParam.getPassword());
        if (!pass){
            bindingResult.rejectValue("password","password.invalid","기존 비밀번호와 일치하지 않습니다.");
            return "client/myInfo";
        }

        /* 비밀번호2, 비밀번호3 일치 확인 */
        Boolean pass2 =(clientUpdateParam.getPassword2()).equals(clientUpdateParam.getPassword3());

        if (!pass2){
            bindingResult.rejectValue("password3","password3.invalid","새 비밀번호가 재확인 비밀번호와 일치하지 않습니다.");
            return "client/myInfo";
        }

        /* 유효성 검증 결과 이상 있을시 내정보 수정 창으로 오류결과 메시지와 함께 리턴 */
        if (bindingResult.hasErrors()){
            return "client/myInfo";
        }

        /* UPDATE 함수 실행 */
        /* 세션객체의 GET함수 사용으로 로그인된 사람의 정보 바꾸기 */
        clientInterface.update(loginClient.getClientId(), clientUpdateParam);

        /* 수정된 객체 확인 */
        Client updateCheck = clientInterface.findByClientId(loginClient.getClientId()).orElse(null);
        log.info("수정여부 확인이요 " + updateCheck);

        /* 수정된 객체 정보를 세션에 재설정 */
        loginInfo.setAttribute("로그인",updateCheck);

        return "redirect:/";
    }

}
