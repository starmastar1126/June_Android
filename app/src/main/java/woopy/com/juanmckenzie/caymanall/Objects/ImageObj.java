package woopy.com.juanmckenzie.caymanall.Objects;

public class ImageObj {
    private String ID = "NOIMAGE.png";
    private String Url;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }


    public String getImage64() {
        return "https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Files%2Fthumb@1024_" + getID() + "?alt=media";

//        return "https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Files%2Fthumb@64_" + getID() + "?alt=media";
    }

    public String getImage128() {
        return "https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Files%2Fthumb@1024_" + getID() + "?alt=media";

//        return "https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Files%2Fthumb@128_" + getID() + "?alt=media";
    }


    public String getImage256() {
        return "https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Files%2Fthumb@1024_" + getID() + "?alt=media";

//        return "https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Files%2Fthumb@256_" + getID() + "?alt=media";
    }

    public String getImage512() {
        return "https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Files%2Fthumb@1024_" + getID() + "?alt=media";

//        return "https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Files%2Fthumb@512_" + getID() + "?alt=media";
    }

    public String getImage1024() {
        return "https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Files%2Fthumb@1024_" + getID() + "?alt=media";
    }
}
