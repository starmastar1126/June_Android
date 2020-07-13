package woopy.com.juanmckenzie.caymanall.Objects;

public class Message {

    private String inboxID, mmessage, createdAt, ID;
    private User Sender, Reciver;
    private Boolean Received = false;
    private Boolean Readed = false;
    ImageObj image;

    public ImageObj getImage() {
        return image;
    }

    public void setImage(ImageObj image) {
        this.image = image;
    }

    public Boolean getReceived() {
        return Received;
    }

    public void setReceived(Boolean received) {
        Received = received;
    }

    public Boolean getReaded() {
        return Readed;
    }

    public void setReaded(Boolean readed) {
        Readed = readed;
    }


    public User getReciver() {
        return Reciver;
    }

    public void setReciver(User reciver) {
        Reciver = reciver;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getSender() {
        return Sender;
    }

    public void setSender(User sender) {
        Sender = sender;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


}
