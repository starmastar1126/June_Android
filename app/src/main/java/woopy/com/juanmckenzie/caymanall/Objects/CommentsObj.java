package woopy.com.juanmckenzie.caymanall.Objects;

public class CommentsObj {
    private User userPointer;
    private String createdAt, comment, ID;
    private Ads adPointer;

    public User getUserPointer() {
        return userPointer;
    }

    public void setUserPointer(User userPointer) {
        this.userPointer = userPointer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Ads getAdPointer() {
        return adPointer;
    }

    public void setAdPointer(Ads adPointer) {
        this.adPointer = adPointer;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

