package woopy.com.juanmckenzie.caymanall.Objects;

import com.parse.ParseGeoPoint;

import java.util.HashMap;
import java.util.List;

public class Banner {
    private String ID, title,
            packagetype, category, subcategory = "All", url = "", country = "all",
            reportMessage, createdAt, starteddate, Days;
    private User sellerPointer;
    private ParseGeoPoint location;
    private Boolean Status;
    private HashMap<String, String> views;
    private ImageObj image1;

    public HashMap<String, String> getViews() {
        return views;
    }

    public void setViews(HashMap<String, String> views) {
        this.views = views;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
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

    public String getPackagetype() {
        return packagetype;
    }

    public void setPackagetype(String packagetype) {
        this.packagetype = packagetype;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ImageObj getImage1() {
        return image1;
    }

    public void setImage1(ImageObj image1) {
        this.image1 = image1;
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

    public String getStarteddate() {
        return starteddate;
    }

    public void setStarteddate(String starteddate) {
        this.starteddate = starteddate;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLocation(ParseGeoPoint location) {
        this.location = location;
    }
}
