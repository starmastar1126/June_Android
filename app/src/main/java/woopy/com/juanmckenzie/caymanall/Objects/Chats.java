package woopy.com.juanmckenzie.caymanall.Objects;

public class Chats {
    private String inboxID, mmessage, image, createdAt;
    private User sender, receiver;
    private Ads adPointer;


    public Ads getAdPointer() {
        return adPointer;
    }

    public void setAdPointer(Ads adPointer) {
        this.adPointer = adPointer;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getInboxID() {
        return inboxID;
    }

    public void setInboxID(String inboxID) {
        this.inboxID = inboxID;
    }

    public String getMmessage() {
        return mmessage;
    }

    public void setMmessage(String mmessage) {
        this.mmessage = mmessage;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}


