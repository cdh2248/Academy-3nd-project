package party.people.domain;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.sql.Time;
import java.util.Date;

@Data
@Validated
public class InviteCard {

    private Long x;
    private String         CLIENT_ID;
    private Integer  PLACE_ID_1;
    private Integer         PLACE_ID_2;
    private Integer PLACE_ID_3;
    private String        TITLE;
    private Date TAGET_DATE;
    private String TAGET_TIME;
    private String      MEETING_CONTENT;
    private String              MEETING_PARTICIPANTS;
    private String     CARD_SKIN;

}
