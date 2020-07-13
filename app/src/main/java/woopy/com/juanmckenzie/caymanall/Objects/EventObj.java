package woopy.com.juanmckenzie.caymanall.Objects;

import com.parse.ParseGeoPoint;

import java.util.HashMap;
import java.util.List;

public class EventObj {

    private String ID, title,
            currency, category, video, videoThumbnail,
            description, condition, reportMessage, createdAt, Phone, Webiste, Startingtime;
    private int likes, comments;
    private Boolean isReported = false;
    private User sellerPointer;
    private String daysleft;
    private ParseGeoPoint location;
    private Double price;
    private Boolean AllDone = false;
    private ImageObj image1 = new ImageObj(), image2 = new ImageObj(), image3 = new ImageObj(), image4 = new ImageObj(), image5 = new ImageObj(), image6 = new ImageObj();


    private HashMap<String, String> attend;

    public HashMap<String, String> getAttend() {
        return attend;
    }

    public void setAttend(HashMap<String, String> attend) {
        this.attend = attend;
    }

    private HashMap<String, String> views;

    public HashMap<String, String> getViews() {
        return views;
    }

    public void setViews(HashMap<String, String> views) {
        this.views = views;
    }

    public Boolean getAllDone() {
        return AllDone;
    }

    public void setAllDone(Boolean allDone) {
        AllDone = allDone;
    }

    private List<CommentsObjNews> CommentsObjcs;

    public ImageObj getImage1() {
        return image1;
    }

    public void setImage1(ImageObj image1) {
        this.image1 = image1;
    }

    public ImageObj getImage2() {
        return image2;
    }

    public void setImage2(ImageObj image2) {
        this.image2 = image2;
    }

    public ImageObj getImage3() {
        return image3;
    }

    public void setImage3(ImageObj image3) {
        this.image3 = image3;
    }

    public ImageObj getImage4() {
        return image4;
    }

    public void setImage4(ImageObj image4) {
        this.image4 = image4;
    }

    public ImageObj getImage5() {
        return image5;
    }

    public void setImage5(ImageObj image5) {
        this.image5 = image5;
    }

    public ImageObj getImage6() {
        return image6;
    }

    public void setImage6(ImageObj image6) {
        this.image6 = image6;
    }

    public String getDaysleft() {
        return daysleft;
    }

    public void setDaysleft(String daysleft) {
        this.daysleft = daysleft;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getWebiste() {
        return Webiste;
    }

    public void setWebiste(String webiste) {
        Webiste = webiste;
    }

    public String getStartingtime() {
        return Startingtime;
    }

    public void setStartingtime(String startingtime) {
        Startingtime = startingtime;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public Boolean getReported() {
        return isReported;
    }

    public void setReported(Boolean reported) {
        isReported = reported;
    }

    public User getSellerPointer() {
        return sellerPointer;
    }

    public void setSellerPointer(User sellerPointer) {
        this.sellerPointer = sellerPointer;
    }

    public ParseGeoPoint getLocation() {
        return location;
    }

    public void setLocation(ParseGeoPoint location) {
        this.location = location;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<CommentsObjNews> getCommentsObjcs() {
        return CommentsObjcs;
    }

    public void setCommentsObjcs(List<CommentsObjNews> commentsObjcs) {
        CommentsObjcs = commentsObjcs;
    }
}


