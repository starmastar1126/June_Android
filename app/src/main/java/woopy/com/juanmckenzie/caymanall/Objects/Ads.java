package woopy.com.juanmckenzie.caymanall.Objects;

import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ads {
    private String ID, title,
            currency, category, subcategory, video, videoThumbnail, ADStatus = "Pending",
            description, condition, reportMessage, createdAt;
    private List<String> likedBy = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private int likes, comments;
    private Boolean isReported;
    private User sellerPointer;
    private ParseGeoPoint location;
    private Double price;
    private Boolean Type = true;
    private Boolean featured = false;
    private Boolean paymentdone = false;
    private List<CommentsObj> CommentsObjcs;
    private Boolean AllDone = false;
    private List<String> tags = new ArrayList<>();
    private ImageObj image1 = new ImageObj(), image2 = new ImageObj(), image3 = new ImageObj(), image4 = new ImageObj(), image5 = new ImageObj(), image6 = new ImageObj();


    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    private HashMap<String, String> views;

    public HashMap<String, String> getViews() {
        return views;
    }

    public void setViews(HashMap<String, String> views) {
        this.views = views;
    }


    public Boolean getPaymentdone() {
        return paymentdone;
    }

    public void setPaymentdone(Boolean paymentdone) {
        this.paymentdone = paymentdone;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getAllDone() {
        return AllDone;
    }

    public void setAllDone(Boolean allDone) {
        AllDone = allDone;
    }

    public String getADStatus() {
        return ADStatus;
    }

    public void setADStatus(String ADStatus) {
        this.ADStatus = ADStatus;
    }

    public Boolean getType() {
        return Type;
    }

    public void setType(Boolean type) {
        Type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public User getSellerPointer() {
        return sellerPointer;
    }

    public void setSellerPointer(User sellerPointer) {
        this.sellerPointer = sellerPointer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public ParseGeoPoint getLocation() {
        return location;
    }

    public void setLocation(ParseGeoPoint location) {
        this.location = location;
    }

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

    public List<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<CommentsObj> getCommentsAll() {
        return CommentsObjcs;
    }

    public void setCommentsAll(List<CommentsObj> commentsAll) {
        CommentsObjcs = commentsAll;
    }

    public Boolean getReported() {
        return isReported;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public List<CommentsObj> getCommentsObjcs() {
        return CommentsObjcs;
    }

    public void setCommentsObjcs(List<CommentsObj> commentsObjcs) {
        CommentsObjcs = commentsObjcs;
    }

    public void setReported(Boolean reported) {
        isReported = reported;
    }
}

