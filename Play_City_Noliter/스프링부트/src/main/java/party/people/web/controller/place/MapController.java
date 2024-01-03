package party.people.web.controller.place;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import party.people.domain.Place;
import party.people.repository.place.PlaceInterface;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MapController {
    private final PlaceInterface placeInterface;

    // 이건 전체 마커 찍히는 지도용
//    @GetMapping("map")
//    public String map(Model model){
//        List<Place> findAll = placeInterface.findAll();
////        List<Place> findCategory = placeInterface.findByCategory("레저");
////        List<Place> findTitle = placeInterface.findByTitle("강화도");
////        log.info("placeForm] "+findTitle);
//        model.addAttribute("map", findAll);
//
//        return "map";
//    }

    // 이건 추천카드별 마커 찍히는 페이지용
    @GetMapping("map")
    public String map2(Model model){
        // <List<Place>는 한 세트의 마커틀, List<List<Place>> maps 이거는 세트들의 리스트
        List<List<Place>> maps = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            List<Place> random3 = placeInterface.randon3();
            maps.add(random3);
        }

//        List<Place> findCategory = placeInterface.findByCategory("레저");
//        List<Place> findTitle = placeInterface.findByTitle("강화도");
//        log.info("placeForm] "+findTitle);
        model.addAttribute("maps", maps);

        return "map2";
    }
}
