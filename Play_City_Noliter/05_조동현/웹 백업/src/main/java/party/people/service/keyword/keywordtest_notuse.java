package party.people.service.keyword;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class keywordtest_notuse {
    public static void main(String[] args) {

        /* 우리 로직은 () 형식이 아니라 폐기 */


        // 리스트 형식으로 키워드 값을 가져올 시 어떻게 개수 체크를 할 것인가
        List<String>keyword = new ArrayList<>();
        // () 가로형식 정규식 테스트
        keyword.add("낮잠(3), 여름(4),부평구(2)");

        // 첫번째 테스트 해시맵으로 해보자!
        Map<String, Integer>mapKeyword = new HashMap<>();

        System.out.println("전체 리스트 : "+keyword.get(0));
        /* ,로 keyword를 하나 하나 쪼개서 리스트 재 생성 */
        List splitKeyword = Arrays.stream(keyword.get(0).split(",")).toList();
        /* 쪼개진 키워드들을 정규식을 이용해 해시맵에 키워드를 키로 개수를 밸류로 입력*/
        for (int index = 0; index < splitKeyword.size(); index++) {
            /* 정규식 적용 대상 예) 낮잠(3) */
            String regexTitle = splitKeyword.get(index).toString();
            System.out.println("쪼개진 리스트 : "+regexTitle);
            /* 정규식 한글만 추출 */
            String regexTextPattern = "([가-힣]+)";
            /* 정규식 숫자만 추출 */
            String regexNumberPattern = "\\d+";
            /* text는 한글, number는 숫자를 Pattern 객체로 생성 */
            Pattern textPattern = Pattern.compile(regexTextPattern);
            Pattern numberPattern = Pattern.compile(regexNumberPattern);
            /* 생성한 객체에 적용대상 매개변수를 matcher함수에 입력 */
            Matcher textMatcher = textPattern.matcher(regexTitle);
            Matcher numberMatcher = numberPattern.matcher(regexTitle);
            /* 매처에서 해당되는 영역을 찾기 */
            textMatcher.find();
            numberMatcher.find();
            /* 각각 결과를 group()을 통해 result에 저장 */
            String textResult = textMatcher.group();
            int numberResult = Integer.parseInt(numberMatcher.group());
            /* mapKeyword에 키와, 밸류를 각각 저장 */
            mapKeyword.put(textResult, numberResult);
        }
        System.out.println("맵으로 쪼갠결과"+mapKeyword);
        System.out.println(mapKeyword.size());

        System.out.println("키값으로 내림차순 정렬결과");
        // Stream과 map.Entry를 이용하면 정렬도 된다!
        List<Map.Entry<String, Integer>> entries = mapKeyword.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println("Key: " + entry.getKey() + ", "
                    + "Value: " + entry.getValue());
        }




    }
}
