package woopy.com.juanmckenzie.caymanall.Objects;

public class ReportUser {

    private String Message, Id;
    private User user;
    private Ads ads;
    private EventObj eventObj;
    private EventObj newsObj;


    public EventObj getEventObj() {
        return eventObj;
    }

    public void setEventObj(EventObj eventObj) {
        this.eventObj = eventObj;
    }

    public EventObj getNewsObj() {
        return newsObj;
    }

    public void setNewsObj(EventObj newsObj) {
        this.newsObj = newsObj;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ads getAds() {
        return ads;
    }

    public void setAds(Ads ads) {
        this.ads = ads;
    }
}
