package party.people.web.controller.place;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import party.people.domain.Place;
import party.people.repository.place.PlaceInterface;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PlaceInfoController {
    private final PlaceInterface placeInterface;

    @GetMapping("place/{pNum}")
    public String festival(@PathVariable Long pNum, Model model) {

        Place place = placeInterface.idSearch(1L);


        model.addAttribute("place", place);
        return "place";
    }


}
