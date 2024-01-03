package party.people.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Place {

    /* 필드  */
    private Long id;
    private String category;
    private String smallCategory;
    private String title;
    private String address;
    private String tel;
    private Float latitude;
    private Float longitude;
    private String petInfo;
    private String parkingInfo;
    private String restDayInfo;
    private String runTimeInfo;
    private String feeInfo;
    private String toiletInfo;
    private String languageInfo;
    private String insuranceInfo;
    private String salesItemInfo;
    private String amenityInfo;
    private String menuInfo;
    private String wrapInfo;
    private String checkinInfo;
    private String checkoutInfo;
    private String otherAmenityInfo;
    private String keyword;
    private String imageAdd1;
    private String imageAdd2;


    /* 기본 생성자 */
    public Place() {}

    /* 생성자 */
    public Place(Long id, String category, String smallCategory, String title, String address, String tel, Float latitude, Float longitude, String petInfo, String parkingInfo, String restDayInfo, String runTimeInfo, String feeInfo, String toiletInfo, String languageInfo, String insuranceInfo, String salesItemInfo, String amenityInfo, String menuInfo, String wrapInfo, String checkinInfo, String checkoutInfo, String otherAmenityInfo, String keyword, String imageAdd1, String imageAdd2) {
        this.id = id;
        this.category = category;
        this.smallCategory = smallCategory;
        this.title = title;
        this.address = address;
        this.tel = tel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.petInfo = petInfo;
        this.parkingInfo = parkingInfo;
        this.restDayInfo = restDayInfo;
        this.runTimeInfo = runTimeInfo;
        this.feeInfo = feeInfo;
        this.toiletInfo = toiletInfo;
        this.languageInfo = languageInfo;
        this.insuranceInfo = insuranceInfo;
        this.salesItemInfo = salesItemInfo;
        this.amenityInfo = amenityInfo;
        this.menuInfo = menuInfo;
        this.wrapInfo = wrapInfo;
        this.checkinInfo = checkinInfo;
        this.checkoutInfo = checkoutInfo;
        this.otherAmenityInfo = otherAmenityInfo;
        this.keyword = keyword;
        this.imageAdd1 = imageAdd1;
        this.imageAdd2 = imageAdd2;
    }

    /* 매서드 */

    public List<String> viewList() {
        List a = new ArrayList<>();

//        if(address != null)
        a.add(address);
//        else a.add("_");

//        if(runTimeInfo != null)
        a.add(runTimeInfo);
//        else a.add('_');

        if (parkingInfo != null) {
            if (parkingInfo.equals("null") != true){  a.add("주차시설 : " + parkingInfo);}
            else{ a.add('_');}
        }
            return a;
    }



    public List<String> viewAllList(){
        List a = new ArrayList<>();
        if(petInfo != null){
            a.add("애완동물 정보 : "+petInfo);
        }
        if(parkingInfo != null){
        a.add("주차정보 : "+parkingInfo);}
        if(runTimeInfo != null){
        a.add("영업시간 : "+runTimeInfo);}

        if(restDayInfo != null){
            a.add("휴일정보 : "+restDayInfo);
        }
        if(tel != null){
            a.add("전화번호 : "+tel);
        }

        if(feeInfo != null){
            a.add("요금정보 : "+feeInfo);
        }
        if(toiletInfo != null){
            a.add("화장실정보 : "+toiletInfo);
        }
        if(languageInfo != null){
            a.add("언어정보 : "+languageInfo);
        }
        if(insuranceInfo != null){
            a.add("사고보험정보 : "+insuranceInfo);
        }
        if(salesItemInfo != null){
            a.add("판매품목 : "+ salesItemInfo);
        }
        if(amenityInfo != null){
            a.add("편의 시설 : "+amenityInfo);
        }
        if(menuInfo != null){
            a.add("메뉴 정보 : "+menuInfo);
        }
        if(wrapInfo != null){
            a.add("포장 가능 : "+wrapInfo);
        }
        if(checkinInfo != null){
            a.add("입실 : "+checkinInfo);
        }
        if(checkoutInfo != null){
            a.add("퇴실 : "+checkoutInfo);
        }
        return a;


    }

    public String keyWord5(){

//        List<String> goString= new ArrayList<>();

        if (keyword != null) {
            String s = keyword.replace("/", "");
            String x = s.replace("0", "");

            for (int count = 0; count < 10; count++) {
                s = s.replace(Integer.toString(count), "");
            }


            String goString[] = s.split(",");

            return "#" +goString[0]+"  #"+ goString[1]+"  #"+ goString[2]+"  #"+ goString[3];

        }

    return "";


    }



    public String keyWordTitle(){

        String s = keyword.replace("/","");
        String x = s.replace("0","");

        for (int count = 0; count < 10; count++) {
            s = s.replace(Integer.toString(count), "");
        }



        String goString[] = s.split(",");


        return getCategory()+"/"+getSmallCategory()+"/"+getTitle();

    }





}

