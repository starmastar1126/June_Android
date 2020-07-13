package woopy.com.juanmckenzie.caymanall.Objects;

import android.media.Image;

import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.TinderType.Objects.userReact;

public class User {
    private String FirebaseID, Joindate = "1573585811865", ID, education, age = "22", username, gander = "Male", expectinggander = "both", email, FCM, fullName, location, aboutMe, website, reportMessage;

    private List<String> hasBlocked = new ArrayList<String>();
    private List<String> feedbacks = new ArrayList<String>();
    private List<String> likes = new ArrayList<String>();
    private List<String> languages = new ArrayList<String>();
    private List<String> hobies = new ArrayList<String>();
    private Boolean isReported, emailVerified, subscrbefortindertypenotifications, visibility, paid = false;
    private HashMap<String, userReact> likesbyothers = new HashMap<>();
    private HashMap<String, userReact> likesbyme = new HashMap<>();

    private ImageObj image2 = new ImageObj(), image3 = new ImageObj(), avatar = new ImageObj();

    public ImageObj getImage2() {
        return image2;
    }

    public void setImage2(ImageObj image2) {
        this.image2 = image2;
    }

    public ImageObj getImage3() {
        return image3;
    }

    public void setImage3(ImageObj image3) {
        this.image3 = image3;
    }

    public ImageObj getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageObj avatar) {
        this.avatar = avatar;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getHobies() {
        return hobies;
    }

    public void setHobies(List<String> hobies) {
        this.hobies = hobies;
    }


    public String getGander() {
        return gander;
    }

    private ParseGeoPoint latlong;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getExpectinggander() {
        return expectinggander;
    }

    public void setExpectinggander(String expectinggander) {
        this.expectinggander = expectinggander;
    }

    public Boolean getSubscrbefortindertypenotifications() {
        return subscrbefortindertypenotifications;
    }

    public void setSubscrbefortindertypenotifications(Boolean subscrbefortindertypenotifications) {
        this.subscrbefortindertypenotifications = subscrbefortindertypenotifications;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public HashMap<String, userReact> getLikesbyothers() {
        return likesbyothers;
    }

    public void setLikesbyothers(HashMap<String, userReact> likesbyothers) {
        this.likesbyothers = likesbyothers;
    }

    public HashMap<String, userReact> getLikesbyme() {
        return likesbyme;
    }

    public void setLikesbyme(HashMap<String, userReact> likesbyme) {
        this.likesbyme = likesbyme;
    }

    public ParseGeoPoint getLatlong() {
        return latlong;
    }

    public void setLatlong(ParseGeoPoint latlong) {
        this.latlong = latlong;
    }

    public void setGander(String gander) {
        this.gander = gander;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFirebaseID() {
        return FirebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        FirebaseID = firebaseID;
    }

    public String getJoindate() {
        return Joindate;
    }

    public void setJoindate(String joindate) {
        Joindate = joindate;
    }

    public void setHasBlocked(List<String> hasBlocked) {
        this.hasBlocked = hasBlocked;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public List<String> getHasBlocked() {
        return hasBlocked;
    }

    public Boolean getReported() {
        return isReported;
    }

    public void setReported(Boolean reported) {
        isReported = reported;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getFCM() {
        return FCM;
    }

    public List<String> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<String> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public void setFCM(String FCM) {
        this.FCM = FCM;
    }
}

