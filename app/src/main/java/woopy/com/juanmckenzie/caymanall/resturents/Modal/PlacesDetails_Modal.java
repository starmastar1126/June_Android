package woopy.com.juanmckenzie.caymanall.resturents.Modal;

/**
 * Created by samarthkejriwal on 10/08/17.
 */

public class PlacesDetails_Modal {

    public String address, phone_no, distance, name, photourl, ID;
    public float rating;


    public PlacesDetails_Modal(String address, String phone_no, float rating, String distance, String name, String photurl, String ID) {
        this.address = address;
        this.phone_no = phone_no;
        this.rating = rating;
        this.distance = distance;
        this.name = name;
        this.photourl = photurl;
        this.ID = ID;
    }

}
