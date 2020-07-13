package woopy.com.juanmckenzie.caymanall.Objects;

public class Feedbacks {
    private String adTitle, stars, text,ID,CreatedAt;
    private User sellerPointer,reviewerPointer;

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public User getReviewerPointer() {
        return reviewerPointer;
    }

    public void setReviewerPointer(User reviewerPointer) {
        this.reviewerPointer = reviewerPointer;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public User getSellerPointer() {
        return sellerPointer;
    }

    public void setSellerPointer(User sellerPointer) {
        this.sellerPointer = sellerPointer;
    }
}
