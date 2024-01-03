package party.people.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import party.people.repository.place.PlaceInterface;
import party.people.repository.place.PlaceRepository;

import java.util.Date;
import java.util.List;

@Data
public class InviteCardDomain {

    private Long sequenceId;
    private String clientId;
    private Place placeId1;
    private Place placeId2;
    private Place placeId3;
    private List<Place> placeList;
    private String title;
    private String targetDate;
    private String targetTime;
    private String meetingContent;
    private String meetingParticipants;
    private String cardSkin;

    public InviteCardDomain(){}

    public InviteCardDomain(InviteCard other, List<Place>placeList, Place placeId1, Place placeId2, Place placeId3) {
        this.sequenceId = other.getSequenceId();
        this.clientId = other.getClientId();
        this.placeId1 = placeId1;
        this.placeId2 = placeId2;
        this.placeId3 = placeId3;
        this.placeList = placeList;
        this.title = other.getTitle();
        this.targetDate = other.getTargetDate();
        this.targetTime = other.getTargetTime();
        this.meetingContent = other.getMeetingContent();
        this.meetingParticipants = other.getMeetingParticipants();
        this.cardSkin = other.getCardSkin();
    }


}
