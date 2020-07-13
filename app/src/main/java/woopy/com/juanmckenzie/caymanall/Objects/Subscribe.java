package woopy.com.juanmckenzie.caymanall.Objects;

import java.util.ArrayList;

public class Subscribe {

    private Boolean Check;
    private String category, FCM;
    private ArrayList<String> subcategory;

    public Boolean getCheck() {
        return Check;
    }

    public void setCheck(Boolean check) {
        Check = check;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(ArrayList<String> subcategory) {
        this.subcategory = subcategory;
    }

    public String getFCM() {
        return FCM;
    }

    public void setFCM(String FCM) {
        this.FCM = FCM;
    }
}
