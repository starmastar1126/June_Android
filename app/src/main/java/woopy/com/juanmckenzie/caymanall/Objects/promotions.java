package woopy.com.juanmckenzie.caymanall.Objects;

import com.parse.ParseGeoPoint;

import java.util.HashMap;

public class promotions {
    private String message, website, id, type = "img", imageId;
    private String title, country = "all",
            createdAt;
    private User sellerPointer;
    private ParseGeoPoint location;


    ImageObj image1 = new ImageObj();

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    private HashMap<String, String> views;

    public HashMap<String, String> getViews() {
        return views;
    }

    public void setViews(HashMap<String, String> views) {
        this.views = views;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    private Boolean Status;

    public ImageObj getImage1() {
        return image1;
    }

    public void setImage1(ImageObj image1) {
        this.image1 = image1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
