package woopy.com.juanmckenzie.caymanall.TinderType.Objects;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;

public class ChatsT {


    public TUser getSender() {
        return sender;
    }

    public void setSender(TUser sender) {
        this.sender = sender;
    }

    public TUser getReceiver() {
        return receiver;
    }

    public void setReceiver(TUser receiver) {
        this.receiver = receiver;
    }

    private String inboxID, mmessage, image, createdAt;
    private Boolean reads, readr = false;
    private TUser sender, receiver;
    private Ads adPointer;


    public Boolean getReads() {
        return reads;
    }

    public void setReads(Boolean reads) {
        this.reads = reads;
    }

    public Boolean getReadr() {
        return readr;
    }

    public void setReadr(Boolean readr) {
        this.readr = readr;
    }

    public Ads getAdPointer() {
        return adPointer;
    }

    public void setAdPointer(Ads adPointer) {
        this.adPointer = adPointer;
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


