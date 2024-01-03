package party.people.domain;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.sql.Time;
import java.util.Date;

@Data
@Validated
public class InviteCard {

    private Long sequenceId;
    private String clientId;
    private Integer placeId1;
    private Integer placeId2;
    private Integer placeId3;
    private String title;
    private String targetDate;
    private String targetTime;
    private String meetingContent;
    private String meetingParticipants;
    private String cardSkin;

    public InviteCard(){}

    public InviteCard(String clientId, Integer placeId1, Integer placeId2, Integer placeId3, String title, String targetDate, String targetTime, String meetingContent, String meetingParticipants, String cardSkin) {
        this.clientId = clientId;
        this.placeId1 = placeId1;
        this.placeId2 = placeId2;
        this.placeId3 = placeId3;
        this.title = title;
        this.targetDate = targetDate;
        this.targetTime = targetTime;
        this.meetingContent = meetingContent;
        this.meetingParticipants = meetingParticipants;
        this.cardSkin = cardSkin;
    }
}
