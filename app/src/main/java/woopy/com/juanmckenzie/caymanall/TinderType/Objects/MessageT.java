package woopy.com.juanmckenzie.caymanall.TinderType.Objects;


import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;

public class MessageT {

    private String inboxID, mmessage, createdAt, ID;
    private TUser Sender, Reciver;
    private Boolean Received = false;
    private Boolean Readed = false;
    private ImageObj image;



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

    public TUser getSender() {
        return Sender;
    }

    public void setSender(TUser sender) {
        Sender = sender;
    }

    public TUser getReciver() {
        return Reciver;
    }

    public void setReciver(TUser reciver) {
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

    public ImageObj getImage() {
        return image;
    }

    public void setImage(ImageObj image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


}
