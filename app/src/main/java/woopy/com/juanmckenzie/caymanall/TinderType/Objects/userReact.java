package woopy.com.juanmckenzie.caymanall.TinderType.Objects;

public class userReact {
    private String ID, UserID, Date;
    private Boolean Like;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Boolean getLike() {
        return Like;
    }

    public void setLike(Boolean like) {
        Like = like;
    }
}
