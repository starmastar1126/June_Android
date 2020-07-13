package woopy.com.juanmckenzie.caymanall.TinderType.Objects;


import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;

public class TUser {
    private String FirebaseID, Joindate, ID, age = "22", username, education, gander = "Male", expectinggander = "both", email, FCM, fullName, location, aboutMe, website, reportMessage;
    private List<String> hasBlocked = new ArrayList<String>();
    private List<String> feedbacks = new ArrayList<String>();
    private List<String> likes = new ArrayList<String>();
    private Double distance;
    private List<String> languages = new ArrayList<String>();
    private List<String> hobies = new ArrayList<String>();
    private Boolean isReported, emailVerified, subscrbefortindertypenotifications, visibility = true, paid = false, bandage = false, showdistance = true;
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

    public Double getDistance() {
        return distance;
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

    public Boolean getShowdistance() {
        return showdistance;
    }

    public void setShowdistance(Boolean showdistance) {
        this.showdistance = showdistance;
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

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Boolean getBandage() {
        return bandage;
    }

    public void setBandage(Boolean bandage) {
        this.bandage = bandage;
    }


    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public String getExpectinggander() {
        return expectinggander;
    }

    public void setExpectinggander(String expectinggander) {
        this.expectinggander = expectinggander;
    }

    public String getGander() {
        return gander;
    }

    public void setGander(String gander) {
        this.gander = gander;
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

    private ParseGeoPoint latlong;

    public Boolean getSubscrbefortindertypenotifications() {
        return subscrbefortindertypenotifications;
    }

    public void setSubscrbefortindertypenotifications(Boolean subscrbefortindertypenotifications) {
        this.subscrbefortindertypenotifications = subscrbefortindertypenotifications;
    }

    public ParseGeoPoint getLatlong() {
        return latlong;
    }

    public void setLatlong(ParseGeoPoint latlong) {
        this.latlong = latlong;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

